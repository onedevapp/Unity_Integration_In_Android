# Embed Unity 3D in a Native Android App
This project covers the process of integrating a game made with Unity within a Native Android Application with multi scene.

## Getting Started

### Unity

I have made a game using Unity, in which the score increments on clicking the **TAP** button. On clicking the **Finish** button, the game ends.

Create new java abstract class OverrideUnityActivity.java inside Assets/Plugins/Android folder.

```Java
package {Your App Package Name};
import android.os.Bundle;

import com.unity3d.player.UnityPlayerActivity;

public abstract class OverrideUnityActivity extends UnityPlayerActivity
{
    abstract protected void onGameFinish(int currentScore);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

```
<br>

#### Export your Project

- File → Build Settings
- Select your scene
- In platform section select Android
- Click on player setting button → In Inspector tab 
    - Other setting add your package name under Bundle Identifier (see image below)
    - Change Scripting Backend from Mono to IL2CPP. 
    - Change the Api Compatibility Level* to .NET 4.x and select both the Target Architectures.
- Mark Export and export

This will export your Unity project to Android one.

<br>

### Android Studio

File → New → Import project → Select your export folder from above step.

- In a pop-up asking me if I should use the project SDK or Android SDK I chose to use Android SDK (which is newer than the one of the Unity project).
- In a pop-up asking me if I want to update Gradle and I pressed Update.

Now we will create a sample Android project. Open Android Studio and create a new project inside the root folder. Select the template of Basic Activity.

Now we will integrate Unity as a library into the Android App. Open **settings.gradle** file and add below code at the end.
```gradle
include ':unityLibrary'
project(':unityLibrary').projectDir=new File('..\\TapGame\\unityLibrary')
```
Open **build.gradle(Module: app)** file and add the below in dependencies { block }

```gradle
implementation project(':unityLibrary')
implementation fileTree(dir: project(':unityLibrary').getProjectDir().toString() + ('\\libs'), include: ['*.jar'])
```
Open **build.gradle(Project:app)** and add the below in allprojects{repositories{ block }
```gradle
flatDir {
  dirs "${project(':unityLibrary').projectDir}/libs"
}
```

After completing the changes above, we will see a message asking us to do the Gradle Sync. Click on Sync Now.

If you’ve followed the steps correctly, then you’ll see that the unityLibrary is now also added into the sample Android project.

<br>

### 1. Android (Java) -> Unity (C#)

**In Android**, Create new java class TapGameActivity.java which extends the OverrideUnityActivity.java class. 
```Java
package com.onedevapp.unityintegrationbaseapp;

import android.content.Intent;
import android.os.Bundle;

import {Your App Package Name};

public class TapGameActivity  extends OverrideUnityActivity {

    // Setup activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
        setIntent(intent);
    }

    void handleIntent(Intent intent) {
        if(intent == null || intent.getExtras() == null) return;

        if(intent.getExtras().containsKey("doQuit"))
            if(mUnityPlayer != null) {
                finish();
            }
    }

    @Override
    protected void onGameFinish(int currentScore) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("score", currentScore);
        startActivity(intent);
        finishAffinity();
    }

    @Override public void onUnityPlayerUnloaded() {
        onGameFinish(0);
    }
}
```

Register this activity in the manifest.
```XML
<activity
    android:name=".TapGameActivity"
    android:configChanges="mcc|mnc|locale|touchscreen|keyboard|keyboardHidden|navigation|orientation|screenLayout|uiMode|screenSize|smallestScreenSize|fontScale|layoutDirection|density"
    android:hardwareAccelerated="false"
    android:label="@string/app_name"
    android:process=":Unity"
    android:screenOrientation="fullSensor" />
```

<br>Note:<br>
Please note that the support for the Back button is a little bit tricky. The Application.Quit(); in the unity C# script is closing the UnityPlayerActivity and also the Activity that called her (too bad), so in order to handle we create a middle empty activity.<br><br>

#### Call the Unity Player Activity
start the *TapGameActivity* from your Activity with an Intent and add data to this intent using `putExtra`
```Java
Intent intent = new Intent(this, TapGameActivity.class);
intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
intent.putExtra("highScore", 0);
intent.putExtra("gameScene", "SceneA");
startActivity(intent);
```
<br>

### 2. Unity (C#) -> Android (Java)
**In Unity**, Create a new Scene as NativeSceneManager and Add new C# scripts as NativeSceneManager.cs.
```C#
using UnityEngine;
using UnityEngine.SceneManagement;

public class NativeSceneManager : MonoInstance<NativeSceneManager>
{
#if UNITY_ANDROID && !UNITY_EDITOR
    AndroidJavaClass UnityPlayer;
    AndroidJavaObject currentActivity;
#endif

    public int lastHighScore;

    void Start()
    {
#if UNITY_ANDROID && !UNITY_EDITOR
        UnityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        currentActivity = UnityPlayer.GetStatic<AndroidJavaObject>("currentActivity");

        AndroidJavaObject intent = currentActivity.Call<AndroidJavaObject>("getIntent");
        AndroidJavaObject extras = intent.Call<AndroidJavaObject>("getExtras");
        bool hasExtra_HighScore = intent.Call<bool>("hasExtra", "highScore");

        if (hasExtra_HighScore)
        {
            lastHighScore = extras.Call<int>("getInt", "highScore");
        }
        
        bool hasExtra_gameScene = intent.Call<bool>("hasExtra", "gameScene");

        if (hasExtra_gameScene)
        {
            string gameScene = extras.Call<string>("getString", "gameScene");
            SceneManager.LoadScene(gameScene);
        }
        else
        {
            SceneManager.LoadScene(1);//Default Scene
        }

#elif UNITY_EDITOR
        SceneManager.LoadScene(1);//Default Scene
#endif
    }

    public void onGameFinish(int currentScore)
    {
#if UNITY_ANDROID && !UNITY_EDITOR
        currentActivity.Call("onGameFinish", currentScore);
#endif
        Application.Quit();
    }
}
```

Based on the `putExtra` value passed via **gameScene**, NativeSceneManager instance opens particular scene from the BuildSettings.

#### Passing the score to Android from Unity
```C#
NativeSceneManager.Instance.onGameFinish(tapCount);
```

<br>

Scene A from Unity with SceneA.cs
```C#
using UnityEngine;
using UnityEngine.UI;

public class SceneA : MonoBehaviour
{
    int tapCount;

    [SerializeField]
    private Text scoreText;

    void Start()
    {
        tapCount = 0;
        int highScore = NativeSceneManager.Instance.lastHighScore;
        scoreText.text = "<size=8>HighScore</size>\n" + highScore;
    }

    public void buttonTapped()
    {

        tapCount += 1;
        scoreText.text = "<size=8>Your Score</size>\n" + tapCount;
    }

    public void buttonFinish()
    {
        NativeSceneManager.Instance.onGameFinish(tapCount);
    }
}

```
<br>

### 3. Embed Unity inside Android Fragment
Coming soon.

<br><br>

## Built with

- [Unity 2019.4.20f1](https://unity3d.com/)
- [Android Studio 4.1.2](https://developer.android.com/studio/index.html)


## :page_facing_up: License
```
MIT License

Copyright (c) 2020 Durga Chiranjeevi

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE