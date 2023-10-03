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
package objectos.http.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class NginxTest {

  private InetAddress address;

  private int port;

  @BeforeClass
  public void setupAddress() throws Exception {
    address = InetAddress.getLoopbackAddress();

    port = 80;
  }

  /*
  
  Normal request (it also accepts LF only line terminators)
  
  GET /index.html HTTP/1.1
  Host: localhost
  Connection: close
  
  HTTP/1.1 200 OK
  Server: nginx/1.23.3
  Date: Fri, 30 Jun 2023 22:57:48 GMT
  Content-Type: text/html
  Content-Length: 25045
  Last-Modified: Wed, 01 Feb 2023 11:26:17 GMT
  Connection: close
  ETag: "63da4c59-61d5"
  Accept-Ranges: bytes
  
  ---
  
  No Host
  
  GET /index.html HTTP/1.1
  Connection: close
  
  HTTP/1.1 400 Bad Request
  Server: nginx/1.23.3
  Date: Fri, 30 Jun 2023 23:01:05 GMT
  Content-Type: text/html
  Content-Length: 157
  Connection: close
  
  <html>
  <head><title>400 Bad Request</title></head>
  <body>
  <center><h1>400 Bad Request</h1></center>
  <hr><center>nginx/1.23.3</center>
  </body>
  </html>
  
  ---
  
  Invalid header/invalid header char

  GET /index.html HTTP/1.1
  Host: localhost
  Connection: close
  Ação: fechar

  HTTP/1.1 200 OK
  Server: nginx/1.23.3
  Date: Fri, 30 Jun 2023 23:03:48 GMT
  Content-Type: text/html
  Content-Length: 25045
  Last-Modified: Wed, 01 Feb 2023 11:26:17 GMT
  Connection: close
  ETag: "63da4c59-61d5"
  Accept-Ranges: bytes

  <!doctype html><html lang="en">

  2023/06/30 20:03:48 [info] 20850#0: *6 client sent invalid header line: "Ação: fechar" while reading client request headers, client: 127.0.0.1, server: localhost, request: "GET /index.html HTTP/1.1", host: "localhost"
  
  ---
  
  Invalid method name
  
  GOT /index.html HTTP/1.1
  Host: localhost
  Connection: close
  
  HTTP/1.1 405 Not Allowed
  Server: nginx/1.23.3
  Date: Fri, 30 Jun 2023 23:06:11 GMT
  Content-Type: text/html
  Content-Length: 157
  Connection: close
  
  ---

  Invalid request line
  
  FOOBAR
  Host: localhost
  Connection: close
  
  HTTP/1.1 400 Bad Request
  Server: nginx/1.23.3
  Date: Fri, 30 Jun 2023 23:08:03 GMT
  Content-Type: text/html
  Content-Length: 157
  Connection: close

  ---
  
  Body in GET

  GET /index.html HTTP/1.1
  Host: localhost

  Hello world!
  
  HTTP/1.1 200 OK
  Server: nginx/1.23.3
  Date: Fri, 30 Jun 2023 23:10:37 GMT
  Content-Type: text/html
  Content-Length: 25045
  Last-Modified: Wed, 01 Feb 2023 11:26:17 GMT
  Connection: keep-alive
  Keep-Alive: timeout=20
  ETag: "63da4c59-61d5"
  Accept-Ranges: bytes

  <html>....

  HTTP/1.1 400 Bad Request
  Server: nginx/1.23.3
  Date: Fri, 30 Jun 2023 23:10:37 GMT
  Content-Type: text/html
  Content-Length: 157
  Connection: close

  <html>...
  
  ---
  
  Invalid char in URI -> accepted the request

  GET /café HTTP/1.1
  Host: localhost
  Connection: close

  HTTP/1.1 404 Not Found
  Server: nginx/1.23.3
  Date: Fri, 30 Jun 2023 23:15:14 GMT
  Content-Type: text/html
  Content-Length: 153
  Connection: close

  ---
  
  Large request header
  
  GET /café HTTP/1.1
  Host: localhost
  Referer: 080980808ASDFJSLDJFLSJDLFJSLJxljdlsjflsjljsldjdlfdjslf08d0s808f0sd8fssdfljsdlfjasldjkfsljflsdjoueorue080980808ASDFJSLDJFLSJDLFJSLJxljdlsjflsjljsldjdlfdjslf08d0s808f0sd8fssdfljsdlfjasldjkfsljflsdjoueorue080980808ASDFJSLDJFLSJDLFJSLJxljdlsjflsjljsldjdlfdjslf08d0s808f0sd8fssdfljsdlfjasldjkfsljflsdjoueorue080980808ASDFJSLDJFLSJDLFJSLJxljdlsjflsjljsldjdlfdjslf08d0s808f0sd8fssdfljsdlfjasldjkfsljflsdjoueorue
  Connection: close
  
  HTTP/1.1 400 Bad Request
  Server: nginx/1.23.3
  Date: Fri, 30 Jun 2023 23:21:18 GMT
  Content-Type: text/html
  Content-Length: 233
  Connection: close
  
  <html>
  <head><title>400 Request Header Or Cookie Too Large</title></head>
  <body>
  <center><h1>400 Bad Request</h1></center>
  <center>Request Header Or Cookie Too Large</center>
  <hr><center>nginx/1.23.3</center>
  </body>
  </html>

  2023/06/30 20:21:18 [info] 2592#0: *3 client sent too long header line: "Referer: 080980808ASDFJSLDJFLSJDLFJSLJxljdlsjflsjljsldjdlfdjslf08d0s808f0sd8fssdfljsdlfjasldjkfsljflsdjoueorue080980808ASDFJSLDJFLSJDLFJSLJxljdlsjflsjljsldjdlfdjslf08d0s808f0sd8fssdfljsdlfjasldjkfsljflsdjoueorue080980808ASDFJSLDJFLSJDLFJSLJxljdlsjflsjljsld..." while reading client request headers, client: 127.0.0.1, server: localhost, request: "GET /café HTTP/1.1", host: "localhost"

  ---
  
  Method lower case

  get /index.html HTTP/1.1
  Host: localhost
  Connection: close

  HTTP/1.1 400 Bad Request
  Server: nginx/1.23.3
  Date: Fri, 30 Jun 2023 23:33:49 GMT
  Content-Type: text/html
  Content-Length: 157
  Connection: close
  
  ---
  
  Protocol version lower case

  get /index.html http/1.1
  Host: localhost
  Connection: close

  HTTP/1.1 400 Bad Request
  Server: nginx/1.23.3
  Date: Fri, 30 Jun 2023 23:34:55 GMT
  Content-Type: text/html
  Content-Length: 157
  Connection: close
  
  ---

  Garbage
  
  Hello World!(no line terminator)
  
  HTTP/1.1 400 Bad Request
  Server: nginx/1.23.3
  Date: Sat, 01 Jul 2023 00:16:41 GMT
  Content-Type: text/html
  Content-Length: 157
  Connection: close

  ---

  No space after colon

  GET /index.html HTTP/1.1
  Host:localhost
  Connection:Close

  HTTP/1.1 200 OK
  Server: nginx/1.23.3

  ---

  More than 1 space after colon
  
  GET /index.html HTTP/1.1
  Host:   localhost
  Connection:  close

  HTTP/1.1 200 OK
  Server: nginx/1.23.3

  ---
  
  Header value w/ trailing spaces
  
  GET /index.html HTTP/1.1
  Host:   localhost\040\040\040
  Connection:  close

  HTTP/1.1 200 OK
  Server: nginx/1.23.3

  ---
  
  Header value w/ trailing whitespace
  
  GET /index.html HTTP/1.1
  Host:   localhost\040\t\040
  Connection:  close

  HTTP/1.1 400 Bad Request
  Server: nginx/1.23.3
  
  ---
  
  HTTP version 1.2

  GET /index.html HTTP/1.2
  Host: localhost
  Connection: close

  HTTP/1.1 200 OK

  ---

  HTTP version 0.9
  
  GET /index.html HTTP/0.9
  Host: localhost
  Connection: close
  
  HTTP/1.1 400 Bad Request
  
  ---
  
  POST /post HTTP/1.1
  Host: localhost
  Connection: close
  Content-Length: -22
  Content-Type: application/x-www-form-urlencoded
  
  email=user@example.com

  HTTP/1.1 400 Bad Request
  Server: nginx/1.24.0
  Date: Sat, 29 Jul 2023 14:23:03 GMT
  Content-Type: text/html
  Content-Length: 157
  Connection: close

  ---

  Target does not start with /

  GET foo/bar HTTP/1.1
  Host: localhost
  Connection: close
  
  HTTP/1.1 400 Bad Request
  Server: nginx/1.24.0
  Date: Tue, 03 Oct 2023 13:49:35 GMT
  Content-Type: text/html
  Content-Length: 157
  Connection: close
  
  */

  @Test(enabled = false)
  public void getOk() throws IOException {
    try (Socket socket = new Socket(address, port)) {
      req(socket, """
      GET foo/bar HTTP/1.1
      Host: localhost
      Connection: close

      """.replace("\n", "\r\n"));

      String resp;
      resp = resp(socket);

      System.out.println(resp);
    }
  }

  private void req(Socket s, String textBlock) throws IOException {
    OutputStream out;
    out = s.getOutputStream();

    byte[] bytes;
    bytes = textBlock.getBytes(StandardCharsets.UTF_8);

    out.write(bytes);
  }

  private String resp(Socket s) throws IOException {
    InputStream in;
    in = s.getInputStream();

    byte[] bytes;
    bytes = in.readAllBytes();

    return new String(bytes, StandardCharsets.UTF_8);
  }

}
