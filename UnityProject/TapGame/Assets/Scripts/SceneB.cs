using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SceneB : MonoBehaviour
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
