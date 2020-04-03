using UnityEngine;

public class CausticsMovement : MonoBehaviour
{
    private const float UpdateWaitSec = 0.025f;

    private readonly Vector3 _moveVector = new Vector2(-0.025f, -0.01f);

    private Vector3 _position;

    private float _lastUpdate;

    public void Start()
    {
        _position = new Vector3(10.0f, 10.0f, -2.0f);

        _lastUpdate = Time.time;
    }

    public void LateUpdate()
    {
        if (Time.time < _lastUpdate + UpdateWaitSec)
        {
            return;
        }

        _lastUpdate = Time.time;

        _position += _moveVector;
        if (_position.x < -5.0f)
        {
            _position = new Vector3(_position.x + 20.0f, _position.y, _position.z);
        }

        if (_position.y < -5.0f)
        {
            _position = new Vector3(_position.x, _position.y + 20.0f, _position.z);
        }

        transform.position = _position;
    }
}
