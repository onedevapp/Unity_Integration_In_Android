﻿using UnityEngine;
using UnityEngine.SceneManagement;

public class GameManager : MonoInstance<GameManager>
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
        
        //UnityPlayer = new AndroidJavaClass("com.OneDevApp.TapGame.OverrideUnityActivity");
        //currentActivity = UnityPlayer.GetStatic<AndroidJavaObject>("instance");

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
            SceneManager.LoadScene(3);
        }

#elif UNITY_EDITOR
        SceneManager.LoadScene(3);
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
