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
package objectos.http;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import objectos.http.internal.Http001;
import objectos.http.internal.Http002;
import objectos.http.internal.Http003;
import objectos.http.internal.Http004;
import objectos.http.internal.Http005;
import objectos.http.internal.Http006;
import objectos.http.internal.TestingHandler;
import objectos.http.internal.TestingInput.RegularInput;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HttpTest {

  private InetAddress address;

  private int port;

  private HttpService server;

  @BeforeClass
  public void start() throws Exception {
    address = InetAddress.getLoopbackAddress();

    try (ServerSocket socket = new ServerSocket(0)) {
      port = socket.getLocalPort();
    }

    InetSocketAddress socketAddress;
    socketAddress = new InetSocketAddress(address, port);

    server = HttpService.create(
      socketAddress, TestingHandler.INSTANCE,
      HttpService.bufferSize(512)
    );

    server.startService();
  }

  @Test
  public void http001() throws IOException {
    try (Socket socket = new Socket(address, port)) {
      req(socket, Http001.INPUT);

      assertEquals(
        resp(socket),

        Http001.OUTPUT
      );
    }
  }

  @Test
  public void http002() throws IOException {
    try (Socket socket = new Socket(address, port)) {
      req(socket, Http002.INPUT);

      assertEquals(
        resp(socket),

        Http002.OUTPUT
      );
    }
  }

  @Test
  public void http003() throws IOException {
    try (Socket socket = new Socket(address, port)) {
      req(socket, Http003.INPUT);

      assertEquals(
        resp(socket),

        Http003.OUTPUT
      );
    }
  }

  @Test(timeOut = 1000)
  public void http004() throws IOException {
    try (Socket socket = new Socket(address, port)) {
      req(socket, Http004.INPUT01);

      resp(socket, Http004.OUTPUT01);

      req(socket, Http004.INPUT02);

      resp(socket, Http004.OUTPUT02);
    }
  }

  @Test(timeOut = 1000)
  public void http005() throws IOException {
    try (Socket socket = new Socket(address, port)) {
      req(socket, Http005.INPUT01);

      resp(socket, Http005.OUTPUT01);

      req(socket, Http005.INPUT02);

      resp(socket, Http005.OUTPUT02);
    }
  }

  @Test(timeOut = 1000)
  public void http006() throws IOException {
    try (Socket socket = new Socket(address, port)) {
      req(socket, Http006.INPUT);

      resp(socket, Http006.OUTPUT);
    }
  }

  @AfterClass(alwaysRun = true)
  public void stop() throws Exception {
    if (server != null) {
      server.stopService();
    }
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