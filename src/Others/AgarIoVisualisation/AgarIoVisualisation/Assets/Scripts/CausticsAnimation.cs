using UnityEngine;

public class CausticsAnimation : MonoBehaviour
{
    public int ImageWidth = 512;

    public int ImageHeight = 512;

    private const float UpdateWaitSec = 0.025f;

    private const string MainTextureName = "_MainTex";

    private Renderer _renderer;

    private int _texOffsetX;

    private int _texOffsetY;

    private Texture _texture;

    private float _lastUpdate;

    public void Start()
    {
        _renderer = GetComponent<Renderer>();
        _texture = _renderer.material.GetTexture(MainTextureName);
        _renderer.material.SetTextureScale(MainTextureName, PixelsToCoords(ImageWidth, ImageHeight));

        _texOffsetX = 0;
        _texOffsetY = 0;

        _lastUpdate = Time.time;
    }

    public void LateUpdate()
    {
        if (Time.time < _lastUpdate + UpdateWaitSec)
        {
            return;
        }

        _lastUpdate = Time.time;

        if (_renderer.enabled)
        {
            var uvOffset = PixelsToCoords(_texOffsetX, _texOffsetY);
            _renderer.material.SetTextureOffset(MainTextureName, uvOffset);
        }

        _texOffsetX += ImageWidth;
        if (_texOffsetX >= _texture.width)
        {
            _texOffsetX = 0;
            _texOffsetY += ImageHeight;
            if (_texOffsetY >= _texture.height)
            {
                _texOffsetY = 0;
            }
        }
    }

    private Vector2 PixelsToCoords(int x, int y)
    {
        return new Vector2((float)x / _texture.width, (float)y / _texture.height);
    }
}
