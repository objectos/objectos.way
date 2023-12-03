/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.http.server;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import objectos.http.server.HttpExchange.Option;
import objectos.notes.NoteSink;
import objectox.http.server.Http001;
import objectox.http.server.Http002;
import objectox.http.server.Http003;
import objectox.http.server.Http004;
import objectox.http.server.Http005;
import objectox.http.server.Http006;
import objectox.http.server.TestingHandler;
import objectox.http.server.TestingInput.RegularInput;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HttpTest implements Runnable {

  private ServerSocket serverSocket;

  private Thread server;

  @BeforeClass
  public void start() throws IOException, InterruptedException {
    InetAddress address;
    address = InetAddress.getLoopbackAddress();

    serverSocket = new ServerSocket(0, 50, address);

    server = Thread.ofPlatform().start(this);

    synchronized (this) {
      TimeUnit.SECONDS.timedWait(this, 2);
    }
  }

  @Override
  public final void run() {
    synchronized (this) {
      notifyAll();
    }

    Option bufferSizeOption;
    bufferSizeOption = HttpExchange.Option.bufferSize(512);

    NoteSink noteSink;
    noteSink = TestingNoteSink.INSTANCE;

    Option noteSinkOption;
    noteSinkOption = HttpExchange.Option.noteSink(noteSink);

    while (!Thread.currentThread().isInterrupted()) {
      Socket socket;

      try {
        socket = serverSocket.accept();
      } catch (IOException e) {
        System.err.println("Failed to accept client connection");

        e.printStackTrace();

        return;
      }

      HttpExchange exchange = HttpExchange.of(
          socket, bufferSizeOption, noteSinkOption
      );

      try (exchange) {
        while (exchange.active()) {
          TestingHandler handler;
          handler = TestingHandler.INSTANCE;

          handler.acceptHttpExchange(exchange);
        }
      } catch (IOException e) {
        System.err.println("Failed to close socket");

        e.printStackTrace();
      }
    }

    try {
      serverSocket.close();
    } catch (IOException ignored) {
      // test finished already...
    }
  }

  @Test
  public void http001() throws IOException {
    try (Socket socket = newSocket()) {
      req(socket, Http001.INPUT);

      assertEquals(
          resp(socket),

          Http001.OUTPUT
      );
    }
  }

  @Test
  public void http002() throws IOException {
    try (Socket socket = newSocket()) {
      req(socket, Http002.INPUT);

      assertEquals(
          resp(socket),

          Http002.OUTPUT
      );
    }
  }

  @Test
  public void http003() throws IOException {
    try (Socket socket = newSocket()) {
      req(socket, Http003.INPUT);

      assertEquals(
          resp(socket),

          Http003.OUTPUT
      );
    }
  }

  @Test(timeOut = 1000)
  public void http004() throws IOException {
    try (Socket socket = newSocket()) {
      req(socket, Http004.INPUT01);

      resp(socket, Http004.OUTPUT01);

      req(socket, Http004.INPUT02);

      resp(socket, Http004.OUTPUT02);
    }
  }

  @Test(timeOut = 1000)
  public void http005() throws IOException {
    try (Socket socket = newSocket()) {
      req(socket, Http005.INPUT01);

      resp(socket, Http005.OUTPUT01);

      req(socket, Http005.INPUT02);

      resp(socket, Http005.OUTPUT02);
    }
  }

  @Test(timeOut = 1000)
  public void http006() throws IOException {
    try (Socket socket = newSocket()) {
      req(socket, Http006.INPUT);

      resp(socket, Http006.OUTPUT);
    }
  }

  @Test(timeOut = 1000)
  public void http007() throws IOException {
    try (Socket socket = newSocket()) {
      req(socket, Http006.INPUT);

      resp(socket, Http006.OUTPUT);
    }
  }

  @AfterClass(alwaysRun = true)
  public void stop() throws Exception {
    if (server != null) {
      server.interrupt();
    }
  }

  private Socket newSocket() throws IOException {
    return new Socket(serverSocket.getInetAddress(), serverSocket.getLocalPort());
  }

  private void req(Socket socket, RegularInput input) throws IOException {
    req(socket, input.request());
  }

  private void req(Socket socket, String string) throws IOException {
    OutputStream out;
    out = socket.getOutputStream();

    byte[] bytes;
    bytes = string.getBytes(StandardCharsets.UTF_8);

    out.write(bytes);
  }

  private String resp(Socket s) throws IOException {
    InputStream in;
    in = s.getInputStream();

    byte[] bytes;
    bytes = in.readAllBytes();

    return new String(bytes, StandardCharsets.UTF_8);
  }

  private void resp(Socket socket, String expected) throws IOException {
    byte[] expectedBytes;
    expectedBytes = expected.getBytes(StandardCharsets.UTF_8);

    InputStream in;
    in = socket.getInputStream();

    byte[] bytes;
    bytes = in.readNBytes(expectedBytes.length);

    String res;
    res = new String(bytes, StandardCharsets.UTF_8);

    assertEquals(res, expected);
  }

}