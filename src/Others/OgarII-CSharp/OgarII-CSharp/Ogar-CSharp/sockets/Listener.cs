﻿using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.Linq;
using WebSocketSharp.Server;
using System.Threading.Tasks;
using WebSocketSharp;

namespace Ogar_CSharp.Sockets
{
    public class Listener
    {
        public class ClientSocket : WebSocketBehavior
        {
            private readonly Listener listener;
            public Action<CloseEventArgs> onClose;
            public Action<MessageEventArgs> onMessage;
            public ClientSocket(Listener listener) => this.listener = listener;
            protected override void OnClose(CloseEventArgs e) 
                => onClose?.Invoke(e);
            protected override void OnError(ErrorEventArgs e) { /*base.OnError(e);*/ }
            protected override void OnMessage(MessageEventArgs e) 
                => onMessage?.Invoke(e);
            protected override void OnOpen()
            {
                if (listener.VerifyClient(this))
                    listener.OnConnection(this);
            }
            public void Disconnect()
                => Sessions.CloseSession(this.ID);

            public new void Send(byte[] data)
            {
                if (ConnectionState == WebSocketState.Open)
                    base.Send(data);
            }
            public void CloseSocket(ushort code, string reason)
            {
                Console.WriteLine($"closing socket, code : {code}, reason {reason}");
                Sessions.CloseSession(base.ID, code, reason);
            }
            public void RemoveAllListeners()
            {
                onClose = null;
                onMessage = null;
            }
        }
        public WebSocketServer listenerSocket;
        public ServerHandle handle;
        public ChatChannel globalChat;
        public List<Router> routers = new List<Router>();
        public List<Connection> connections = new List<Connection>();        
        public Listener(ServerHandle handle)
        {
            this.handle = handle;
        }
        public int ConnectionCountForIP(string ipAddress)
            => connections.Count((x) => x.remoteAddress.ToString() == ipAddress);
        public Settings Settings
            => handle.Settings;
        public bool Open()
        {
            if (listenerSocket != null)
                return false;
            Console.WriteLine($"Listener opening at {Settings.listeningPort}");
            listenerSocket = new WebSocketServer(Settings.listeningPort, false);
            listenerSocket.AddWebSocketService("/", () => new ClientSocket(this));
            listenerSocket.Start();
            return true;
        }
        public bool Close()
        {
            if (listenerSocket == null)
                return false;
            Console.WriteLine("Listener Closing");
            listenerSocket.Stop();
            return true;
        }
        public bool VerifyClient(ClientSocket socket)
        {
            var address = socket.Context.UserEndPoint.Address.ToString();
            Console.WriteLine($"REQUEST FROM {address}, {(socket.Context.IsSecureConnection ? "" : "not ")}secure, Origin: {socket.Context.Origin}");
            if (connections.Count > Settings.listenerMaxConnections)
            {
                Console.WriteLine("listenerMaxConnections reached, dropping new connections");
                return false;
            }
            var acceptedOrigins = Settings.listenerAcceptedOrigins;
            if (acceptedOrigins.Count > 0 && acceptedOrigins.Contains(socket.Context.Origin))
            {
                Console.WriteLine($"listenerAcceptedOrigins doesn't contain {socket.Context.Origin}");
                return false;
            }
            if (Settings.listenerForbiddenIPs.Contains(address))
            {
                Console.WriteLine($"listenerForbiddenIPs contains {address}, dropping connection");
                return false;
            }
            if (Settings.listenerMaxConnectionsPerIP > 0)
            {
                var count = ConnectionCountForIP(address);
                if (count != 0 && count >= Settings.listenerMaxConnectionsPerIP)
                {
                    Console.WriteLine($"listenerMaxConnectionsPerIP reached for '{address}', dropping its new connections");
                    return false;
                }
            }
            Console.WriteLine("client verification passed");
            return true;
        }
        public void AddRouter(Router router)
            => routers.Add(router);
        public void RemoveRouter(Router router)
           => routers.Remove(router);
        public void OnConnection(ClientSocket client)
        {
            if (!VerifyClient(client))
                client.Disconnect();
            var newConnection = new Connection(this, client);
            Console.WriteLine($"CONNECTION FROM {newConnection.remoteAddress}");
            connections.Add(newConnection);
        }
        public void OnDisconnection(Connection client, ushort code, string reason)
        {
            Console.WriteLine($"DISCONNECTION FROM {client.remoteAddress} ({code} '{reason}'");
            connections.Remove(client);
        }
        public void Update()
        {
            int l;
            int i;
            for (i = 0, l = this.routers.Count; i < l; i++)
            {
                var router = this.routers[i];
                if (!router.ShouldClose) continue;
                router.Close(); i--; l--;
            }
            Task[] awaitedTasks = new Task[l];
            /*for (i = 0; i < l; i++)
                this.routers[i].Player?.up();*/
            Parallel.ForEach(routers, async (x) => { await x.PerformAsyncTick(); });
            for (i = 0; i < l; i++) 
                this.routers[i].Update();
            for (i = 0, l = this.connections.Count; i < l; i++)
            {
                var connection = connections[i];
                if (Settings.listenerForbiddenIPs.Contains(connection.remoteAddress.ToString()))
                    connection.CloseSocket(1003, "Remote address is forbidden");
                //else if (DateTime.Now.Ticks - connection.lastActivityTime.Ticks >= Settings.listenerMaxClientDormancy)
                   // connection.CloseSocket(1003, "Maximum dormancy time exceeded");
            }
        }
    }
}
