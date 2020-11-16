using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerBehaviour : MonoBehaviour
{
    private Rigidbody rigidbody;
    private Renderer renderer;

    void Start()
    {
        rigidbody = GetComponent<Rigidbody>();
        renderer = GetComponent<Renderer>();
        rigidbody.angularVelocity = Random.insideUnitSphere;
    }

    void ChangeColor(string colour)
    {
        Color colorToBeApplied;
        if (ColorUtility.TryParseHtmlString(colour, out colorToBeApplied))
        {
            renderer.material.color = colorToBeApplied;
        }
        else
        {
            renderer.material.color = Color.blue;
        }
    }

    void ToggleRotation(string isPause)
    {
        bool isPaused;
        if (System.Boolean.TryParse(isPause, out isPaused))
        {
            if (isPaused)
            {
                rigidbody.angularVelocity = Vector3.zero;
            }
            else
            {
                rigidbody.angularVelocity = Random.insideUnitSphere;
            }
        }
        else
        {
            Debug.LogError("Incorrect boolean string is passed from Swift. " + isPause);
        }

    }

    public void buttonFinish()
    {
        GameManager.Instance.onGameFinish(0);
    }
}
