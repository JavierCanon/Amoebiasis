using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net.Sockets;
using System.Threading;

using AgarIo.Contract;
using AgarIo.Contract.AdminCommands;
using AgarIo.Contract.GameModes.Classic;

using UnityEngine;
using UnityEngine.UI;

using Action = System.Action;
using Random = UnityEngine.Random;

public class NetworkClient : MonoBehaviour
{
    private class ThreadParams
    {
        public string Host { get; set; }

        public int Port { get; set; }

        public string UserName { get; set; }

        public string Password { get; set; }
    }

    private class PlayerColor
    {
        public PlayerColor(Color mainColor, Color secondaryColor, Color subColor)
        {
            MainColor = mainColor;
            SecondaryColor = secondaryColor;
            SubColor = subColor;
        }

        public Color MainColor { get; private set; }

        public Color SecondaryColor { get; private set; }

        public Color SubColor { get; private set; }
    }

    private static readonly List<PlayerColor> PlayerColors = new List<PlayerColor>
    {
        new PlayerColor(ColorFromHex(0x0000ff, 0.75f), ColorFromHex(0x000080, 0.5f), ColorFromHex(0x0000c0, 0.5f)),
        new PlayerColor(ColorFromHex(0x008080, 0.75f), ColorFromHex(0x55ffff, 0.5f), ColorFromHex(0x00aaaa, 0.5f)),
        new PlayerColor(ColorFromHex(0xff00ff, 0.75f), ColorFromHex(0xff0040, 0.5f), ColorFromHex(0xff0080, 0.5f)),
        new PlayerColor(ColorFromHex(0x008000, 0.75f), ColorFromHex(0x55ff55, 0.5f), ColorFromHex(0x00aa00, 0.5f)),
        new PlayerColor(ColorFromHex(0x800000, 0.75f), ColorFromHex(0xff5555, 0.5f), ColorFromHex(0xaa0000, 0.5f)),
        new PlayerColor(ColorFromHex(0xc4a000, 0.75f), ColorFromHex(0xfce94f, 0.5f), ColorFromHex(0xedd400, 0.5f)),
        new PlayerColor(ColorFromHex(0x8f5902, 0.75f), ColorFromHex(0xe9b96e, 0.5f), ColorFromHex(0xc17d11, 0.5f)),
        new PlayerColor(ColorFromHex(0x204a87, 0.75f), ColorFromHex(0x729fcf, 0.5f), ColorFromHex(0x3465a4, 0.5f)),
        new PlayerColor(ColorFromHex(0x5c3566, 0.75f), ColorFromHex(0xad7fa8, 0.5f), ColorFromHex(0x75507b, 0.5f)),
        new PlayerColor(ColorFromHex(0x2e3436, 0.75f), ColorFromHex(0x888a85, 0.5f), ColorFromHex(0x555753, 0.5f)),
        new PlayerColor(ColorFromHex(0xce5c00, 0.75f), ColorFromHex(0xfcaf3e, 0.5f), ColorFromHex(0xf57900, 0.5f)),
    };

    private static readonly object SynchronizationLock = new object();

    private static readonly Queue<Action> ExecuteOnMainThread = new Queue<Action>();

    private Thread _thread;

    private bool _finish;

    private Dictionary<int, GameObject> _blobObjects;

    private List<GameObject> _blobPrefabs;

    private GameObject _virusPrefab;

    private List<PlayerStatDto> _playerStats;

    private List<GameObject> _playerPanels;

    private GameObject _timerText;

    private GameObject _gameOverSprite;

    public void Start()
    {
        _blobPrefabs = new List<GameObject>();
        _playerPanels = new List<GameObject>();
        _timerText = GameObject.Find("TimerText");
        _gameOverSprite = GameObject.Find("GameOverSprite");
        var blobPrefab = (GameObject)Resources.Load("Blob");
        var virusPrefab = (GameObject)Resources.Load("VirusBlob");
        var playerPanelPrefab = (GameObject)Resources.Load("PlayerPanel");
        var playerListPanel = GameObject.Find("PlayerListPanel");

        for (int i = 0; i < 20; i++)
        {
            var panel = Instantiate(playerPanelPrefab);
            panel.transform.SetParent(playerListPanel.transform);
            var rectTransform = panel.GetComponent<RectTransform>();
            rectTransform.anchoredPosition3D = new Vector3(0, -7 - i * 30, 0);
            rectTransform.localScale = new Vector3(1.0f, 1.0f, 1.0f);
            rectTransform.offsetMax = new Vector2(0, rectTransform.offsetMax.y);

            _playerPanels.Add(panel);
        }

        for (var i = 0; i < 10; i++)
        {
            var clonedPrefab = CloneBlob(blobPrefab);
            _blobPrefabs.Add(clonedPrefab);
        }

        _virusPrefab = CloneBlob(virusPrefab);

        _thread = new Thread(ThreadEntryPoint);
        _finish = false;
        _blobObjects = new Dictionary<int, GameObject>();

        Connect();
    }

    public void Update()
    {
        lock (SynchronizationLock)
        {
            while (ExecuteOnMainThread.Count > 0)
            {
                ExecuteOnMainThread.Dequeue().Invoke();
            }
        }

        foreach (var blobAnimation in _blobPrefabs.Concat(new[] { _virusPrefab }).Select(blobPrefab => blobPrefab.GetComponent<BlobAnimation>()))
        {
            blobAnimation.Distort();
        }
    }

    public void OnDestroy()
    {
        if (_thread != null && _thread.IsAlive)
        {
            _finish = true;
            _thread.Join();
        }
    }

    public void Connect()
    {
        var settings = File.ReadAllText("settings.txt");
        var threadParams = settings.FromJson<ThreadParams>();

        _thread.Start(threadParams);
    }

    private void ThreadEntryPoint(object arg)
    {
        var threadParams = (ThreadParams)arg;

        var tcpClient = new TcpClient { NoDelay = true };
        tcpClient.Connect(threadParams.Host, threadParams.Port);

        using (var writer = new StreamWriter(tcpClient.GetStream()))
        {
            writer.AutoFlush = true;
            using (var reader = new StreamReader(tcpClient.GetStream()))
            {
                SendLoginData(reader, writer, threadParams.UserName, threadParams.Password);
                HandleConnection(reader, writer);
            }
        }
    }

    private void SendLoginData(TextReader reader, TextWriter writer, string userName, string password)
    {
        var loginDto = new LoginDto { Login = userName, Password = password, IsAdmin = true };
        var loginJson = loginDto.ToJson();

        writer.WriteLine(loginJson);
        reader.ReadLine();
    }

    private void HandleConnection(TextReader reader, TextWriter writer)
    {
        var startPushDto = new StartPushingStateAdminCommandDto();
        var startPushJson = startPushDto.ToJson();

        writer.WriteLine(startPushJson);
        reader.ReadLine();

        while (!_finish)
        {
            var pushDataJson = reader.ReadLine();
            var pushData = pushDataJson.FromJson<StatePushDto>();

            var customDataText = pushData.CustomGameModeData;
            var customData = customDataText.FromJson<ClassicGameModeDataDto>();

            _playerStats = customData.PlayerStats.OrderByDescending(x => x.Score).ToList();

            var remainingTime = pushData.TurnEndTime - DateTime.UtcNow;

            lock (SynchronizationLock)
            {
                ExecuteOnMainThread.Enqueue(() =>
                    {
                        var playerIndex = 0;
                        for (; playerIndex < _playerPanels.Count && playerIndex < _playerStats.Count; playerIndex++)
                        {
                            var panel = _playerPanels[playerIndex];
                            panel.SetActive(true);
                            var player = _playerStats[playerIndex];
                            var image = panel.transform.FindChild("Image").gameObject;
                            var imageRenderer = image.GetComponent<Image>();
                            var playerColor = PlayerColors[player.Id % PlayerColors.Count];
                            imageRenderer.canvasRenderer.SetColor(playerColor.MainColor);
                            var playerName = panel.transform.FindChild("NameText").gameObject;
                            var playerScore = panel.transform.FindChild("ScoreText").gameObject;
                            playerName.GetComponent<Text>().text = player.Name;
                            playerScore.GetComponent<Text>().text = player.Score.ToString();
                        }

                        for (; playerIndex < _playerPanels.Count; playerIndex++)
                        {
                            var panel = _playerPanels[playerIndex];
                            panel.SetActive(false);
                        }

                        var timerText = _timerText.GetComponent<Text>();
                        var timerTextOutline = _timerText.GetComponent<Outline>();
                        timerText.text = string.Format("{0}:{1:D2}.{2:D3}", (int)remainingTime.TotalMinutes, remainingTime.Seconds, remainingTime.Milliseconds);
                        if (remainingTime.TotalMilliseconds < 60000)
                        {
                            timerText.color = Color.Lerp(Color.red, Color.white, (float)remainingTime.TotalMilliseconds / 50000.0f);
                            var alpha = 1.0f - (float)remainingTime.TotalMilliseconds / 50000.0f;
                            timerTextOutline.effectColor = Color.Lerp(new Color(0.75f, 0.0f, 0.0f, alpha), new Color(1.0f, 0.5f, 0.0f, alpha), (Mathf.Sin(Time.time * 5.0f) + 1) / 2.0f);
                        }
                        else
                        {
                            timerText.color = Color.white;
                            timerTextOutline.effectColor = new Color(0.0f, 0.0f, 0.0f, 0.0f);
                        }

                        var gameOverSprite = _gameOverSprite.GetComponent<SpriteRenderer>();
                        if (remainingTime.TotalMilliseconds < 3000)
                        {
                            var slerp = Vector3.Slerp(Vector3.right, Vector3.up * 2, (float)remainingTime.TotalMilliseconds / 3000.0f);
                            var alpha = slerp.x;
                            gameOverSprite.color = new Color(1.0f, 1.0f, 1.0f, alpha);
                        }
                        else
                        {
                            gameOverSprite.color = new Color(0.0f, 0.0f, 0.0f, 0.0f);
                        }
                    });
            }

            foreach (var addedBlob in pushData.AddedBlobs)
            {
                var localBlob = addedBlob;
                lock (SynchronizationLock)
                {
                    ExecuteOnMainThread.Enqueue(() =>
                        {
                            var blobPrefab = localBlob.Type == BlobType.Virus ? _virusPrefab : _blobPrefabs[Random.Range(0, _blobPrefabs.Count)];
                            var blob = (GameObject)Instantiate(blobPrefab, new Vector3(), Quaternion.identity);
                            blob.SetActive(true);

                            blob.transform.localRotation = Quaternion.Euler(0.0f, 0.0f, Random.Range(0, 360));
                            var blobAnimation = blob.GetComponent<BlobAnimation>();

                            switch (localBlob.Type)
                            {
                                case BlobType.Player:
                                    var player = _playerStats.FirstOrDefault(x => x.Name == localBlob.Name);
                                    if (player == null)
                                    {
                                        break;
                                    }

                                    var playerColor = PlayerColors[player.Id % PlayerColors.Count];
                                    blobAnimation.SetColors(
                                        playerColor.MainColor, playerColor.SecondaryColor, playerColor.SubColor);
                                    break;
                                case BlobType.Virus:
                                    blobAnimation.SetColors(
                                        ColorFromHex(0xa40000, 0.75f), ColorFromHex(0xef2929, 0.5f), ColorFromHex(0xcc0000, 0.5f));
                                    break;
                                case BlobType.Food:
                                    blobAnimation.SetColors(
                                        ColorFromHex(0x4e9a06, 0.75f), ColorFromHex(0x8ae234, 0.5f), ColorFromHex(0x73d216, 0.5f));
                                    break;
                            }

                            if (_blobObjects.ContainsKey(localBlob.Id))
                            {
                                RemoveBlob(localBlob.Id);
                            }

                            _blobObjects.Add(localBlob.Id, blob);
                            SetBlobData(blob, localBlob, pushData.WorldSize);
                        });
                }
            }

            foreach (var updatedBlob in pushData.UpdatedBlobs)
            {
                lock (SynchronizationLock)
                {
                    var localBlob = updatedBlob;
                    ExecuteOnMainThread.Enqueue(() =>
                        {
                            if (!_blobObjects.ContainsKey(localBlob.Id))
                            {
                                return;
                            }

                            var blob = _blobObjects[localBlob.Id];
                            SetBlobData(blob, localBlob, pushData.WorldSize);
                        });
                }
            }

            foreach (var removedBlob in pushData.RemovedBlobs)
            {
                lock (SynchronizationLock)
                {
                    var localBlob = removedBlob;
                    ExecuteOnMainThread.Enqueue(() =>
                    {
                        RemoveBlob(localBlob.Id);
                    });
                }
            }
        }
    }

    private void RemoveBlob(int blobId)
    {
        if (!_blobObjects.ContainsKey(blobId))
        {
            return;
        }

        var blob = _blobObjects[blobId];
        Destroy(blob);
        _blobObjects.Remove(blobId);
    }

    private void SetBlobData(GameObject blob, BlobDto blobDto, int worldSize)
    {
        var scale = worldSize / 4.5f;
        var radius = (float)(blobDto.Radius / scale);
        blob.transform.localScale = new Vector3(radius, radius, blobDto.Type == BlobType.Virus ? radius : radius * 0.25f);
        blob.transform.position = new Vector2((float)blobDto.Position.X / scale, (float)blobDto.Position.Y / scale);
    }

    private GameObject CloneBlob(GameObject blobPrefab)
    {
        var clonedPrefab = Instantiate(blobPrefab);
        clonedPrefab.SetActive(false);

        var blobAnimation = clonedPrefab.GetComponent<BlobAnimation>();
        blobAnimation.Initialize();
        blobAnimation.MakeUnique();
        return clonedPrefab;
    }

    private static Color ColorFromHex(uint hex, float alpha)
    {
        float r = (hex >> 16) & 0xFF;
        float g = (hex >> 8) & 0xFF;
        float b = hex & 0xFF;
        return new Color(r / 255, g/ 255, b / 255, alpha);
    }
}