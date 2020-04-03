using UnityEngine;

public class BlobAnimation : MonoBehaviour
{
    private const float SpeedFactor = 1.0f;

    private const float ModificationFactor = 1.0f;

    private Renderer _renderer;

    private Mesh _mesh;

    private Vector3[] _sharedVertices;

    private int _randomizer;

    private int _colorRandomizer;

    private Color _color1 = Color.black;

    private Color _color2 = Color.black;

    private Color _color3 = Color.black;

    public void Start()
    {
        _renderer = GetComponent<Renderer>();
        _colorRandomizer = Random.Range(0, 1000);
    }

    public void Update()
    {
        var animationFrame = Time.time + _colorRandomizer;
        var sample = Mathf.PerlinNoise(animationFrame * SpeedFactor, 0);
        _renderer.material.color = Color.Lerp(_color1, _color2, sample);
        _renderer.material.SetColor("_SubColor", _color3);
    }

    public void SetColors(Color color1, Color color2, Color color3)
    {
        _color1 = color1;
        _color2 = color2;
        _color3 = color3;
    }

    public void Initialize()
    {
        _randomizer = Random.Range(0, 1000);
    }

    public void Distort()
    {
        var vertices = new Vector3[_sharedVertices.Length];
        var animationFrame = Time.time + _randomizer;
        for (var i = 0; i < _sharedVertices.Length; i++)
        {
            var sampleX = Mathf.PerlinNoise(_sharedVertices[i].x + animationFrame * SpeedFactor, _sharedVertices[i].y);
            var sampleY = Mathf.PerlinNoise(_sharedVertices[i].x, _sharedVertices[i].y + animationFrame * SpeedFactor);
            var x = _sharedVertices[i].x * ((sampleX + 1.0f) * 0.5f * ModificationFactor + 1.0f);
            var y = _sharedVertices[i].y * ((sampleY + 1.0f) * 0.5f * ModificationFactor + 1.0f);
            var z = _sharedVertices[i].z;
            vertices[i] = new Vector3(x, y, z);
        }

        _mesh.vertices = vertices;
        _mesh.RecalculateBounds();
        _mesh.RecalculateNormals();
    }

    public void MakeUnique()
    {
        var meshFilter = GetComponent<MeshFilter>();

        _sharedVertices = meshFilter.sharedMesh.vertices;

        meshFilter.sharedMesh = Instantiate(meshFilter.sharedMesh);

        _mesh = meshFilter.sharedMesh;
    }
}
