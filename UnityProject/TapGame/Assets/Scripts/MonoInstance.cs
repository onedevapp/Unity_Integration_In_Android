using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public abstract class MonoInstance<T> : MonoBehaviour where T : MonoBehaviour
{
    /// <summary>
    /// The is persistence.
    /// </summary>
    public bool IsPersistence;

    /// <summary>
    /// The m instance.
    /// </summary>
    protected static T m_Instance;

    /// <summary>
    /// Gets the instance.
    /// </summary>
    /// <value>The instance.</value>
    public static T Instance
    {
        get { return m_Instance; }
    }

    /// <summary>
    /// Awake this instance.
    /// </summary>
    protected virtual void Awake()
    {
        if (IsPersistence)
        {
            if (ReferenceEquals(m_Instance, null))
            {
                m_Instance = this as T;

                DontDestroyOnLoad(gameObject);
            }
            else if (!ReferenceEquals(m_Instance, this as T))
            {
                Destroy(gameObject);
            }
        }
        else
        {
            m_Instance = this as T;
        }
    }

    protected virtual void OnDestroy()
    {
        m_Instance = null;
    }
}
