using UnityEngine;

public class MainCamera : MonoBehaviour
{
    public void Start()
    {
        var cam = GetComponent<Camera>();

        cam.aspect = 1.0f;
    }
}
