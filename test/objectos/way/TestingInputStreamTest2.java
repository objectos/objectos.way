/*
 * Copyright (C) 2025 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.way;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class TestingInputStreamTest2 {
  private static final Note.Ref1<String> MSG = Note.Ref1.create(TestingInputStreamTest2.class, "Message", Note.INFO);

  private static final Note.Ref2<String, IOException> IO_ERROR = Note.Ref2.create(TestingInputStreamTest2.class, "I/O error", Note.ERROR);

  public static void main(String[] args) {
    Note.Sink noteSink = Y.noteSink();

    App.ShutdownHook shutdownHook;
    shutdownHook = App.ShutdownHook.create(config -> config.noteSink(noteSink));

    InetAddress localHost;

    int port = 7001;

    ServerSocket serverSocket;

    try {
      localHost = InetAddress.getLoopbackAddress();

      InetSocketAddress socketAddress;
      socketAddress = new InetSocketAddress(localHost, port);

      serverSocket = new ServerSocket();

      shutdownHook.register(serverSocket);

      serverSocket.bind(socketAddress);
    } catch (IOException e) {
      e.printStackTrace();

      System.exit(1);

      return;
    }

    Server server;
    server = new Server(noteSink, serverSocket);

    server.start();

    try (Socket socket = new Socket(localHost, port)) {
      OutputStream outputStream = socket.getOutputStream();

      OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);

      BufferedWriter writer = new BufferedWriter(streamWriter);

      writer.write("MSG 1");

      writer.flush();

      TimeUnit.SECONDS.sleep(15);

      writer.write("MSG 2");

      writer.flush();
    } catch (IOException e) {
      noteSink.send(IO_ERROR, "Socket", e);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static class Server extends Thread {

    private final Note.Sink noteSink;

    private final ServerSocket serverSocket;

    public Server(Note.Sink noteSink, ServerSocket serverSocket) {
      this.noteSink = noteSink;

      this.serverSocket = serverSocket;
    }

    @Override
    public final void run() {
      byte[] buffer;
      buffer = new byte[1024];

      while (!Thread.interrupted()) {
        try (Socket client = serverSocket.accept()) {
          InputStream inputStream;
          inputStream = client.getInputStream();

          int count;
          count = inputStream.read(buffer);

          while (count != -1) {
            String msg;
            msg = new String(buffer, 0, count);

            noteSink.send(MSG, msg);

            count = inputStream.read(buffer);
          }
        } catch (IOException e) {
          noteSink.send(IO_ERROR, "ServerSocket", e);
        }
      }
    }
  }
}
