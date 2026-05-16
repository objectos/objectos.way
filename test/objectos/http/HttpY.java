/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.testng.Assert;

@SuppressWarnings("exports")
public final class HttpY {

  private HttpY() {}

  public static String cookie(String name, long l0, long l1, long l2, long l3) {
    final HttpToken token;
    token = HttpToken.of32(l0, l1, l2, l3);

    return name + "=" + token.toString();
  }

  public static Socket socket(HttpServer server) throws IOException {
    final InetAddress address;
    address = server.address();

    final int port;
    port = server.port();

    return new Socket(address, port);
  }

  private static final Charset ASCII = StandardCharsets.US_ASCII;

  public static void test(Socket socket, String request, String expectedResponse) throws IOException {
    final OutputStream out;
    out = socket.getOutputStream();

    final byte[] requestBytes;
    requestBytes = request.getBytes(ASCII);

    out.write(requestBytes);

    final InputStream in;
    in = socket.getInputStream();

    final byte[] buf;
    buf = new byte[2048];

    final int read;
    read = in.read(buf);

    if (read < 0) {
      Assert.fail("EOF");
    }

    final String headers;
    headers = new String(buf, 0, read, ASCII);

    final String[] headerLines;
    headerLines = headers.split("\r\n");

    int length = 0;

    if (!request.startsWith("HEAD ")) {

      for (String line : headerLines) {
        if (line.startsWith("HEAD ")) {
          break;
        }

        if (line.startsWith("Content-Length: ")) {
          final String raw;
          raw = line.substring("Content-Length: ".length());

          length = Integer.parseInt(raw);

          break;
        }

        if (line.equals("Transfer-Encoding: chunked")) {
          length = Integer.MIN_VALUE;

          break;
        }
      }

    }

    final String response;

    if (length > 0) {
      final StringBuilder res;
      res = new StringBuilder();

      int idx = 0;

      boolean trailer = false;

      while (idx < headerLines.length) {
        final String line;
        line = headerLines[idx++];

        res.append(line);

        res.append("\r\n");

        if (line.equals("")) {
          trailer = true;

          break;
        }
      }

      if (!trailer) {
        res.append("\r\n");
      }

      int body = 0;

      while (idx < headerLines.length) {
        final String line;
        line = headerLines[idx++];

        body += line.length();

        res.append(line);
      }

      if (body < length) {
        final int remaining;
        remaining = length - body;

        final byte[] bodyBuf;
        bodyBuf = in.readNBytes(remaining);

        final String b;
        b = new String(bodyBuf, ASCII);

        res.append(b);
      }

      response = res.toString();
    }

    else if (length == Integer.MIN_VALUE) {
      final StringBuilder res;
      res = new StringBuilder();

      int idx = 0;

      while (idx < headerLines.length) {
        final String line;
        line = headerLines[idx++];

        res.append(line);

        res.append("\r\n");

        if (line.equals("")) {
          break;
        }
      }

      boolean done = false;

      while (idx < headerLines.length) {
        final String chunkSize;
        chunkSize = headerLines[idx++];

        if (chunkSize.equals("0")) {
          done = true;

          break;
        }

        final String chunk;
        chunk = headerLines[idx++];

        res.append(chunk);
      }

      if (!done) {
        while (true) {
          final int bufLen;
          bufLen = in.read(buf);

          System.out.println(bufLen);

          final String chunk;
          chunk = new String(buf, 0, bufLen, ASCII);

          final String[] chunkLines;
          chunkLines = chunk.split("\r\n");

          assert chunkLines.length == 2;

          final String len;
          len = chunkLines[0];

          if (len.equals("0")) {
            break;
          }

          res.append(chunkLines[1]);
        }
      }

      response = res.toString();
    }

    else {
      response = headers;
    }

    assertEquals(response, expectedResponse);
  }

  // ##################################################################
  // # BEGIN: URL Decode
  // ##################################################################

  public static boolean[] queryValidBytes() {
    final boolean[] valid;
    valid = new boolean[256];

    final String validString;
    validString = Http.unreserved() + Http.subDelims() + ":@/?";

    for (int idx = 0, len = validString.length(); idx < len; idx++) {
      final char c;
      c = validString.charAt(idx);

      valid[c] = true;
    }

    return valid;
  }

  public static Object[][] queryValidProvider() {
    final boolean[] validBytes;
    validBytes = queryValidBytes();

    final List<Object[]> l;
    l = new ArrayList<>();

    l.add(arr("", Map.of(), "empty"));
    l.add(arr("key=value", Map.of("key", "value"), "one"));
    l.add(arr("=value", Map.of("", "value"), "one + empty key"));
    l.add(arr("key=", Map.of("key", ""), "one + empty value"));
    l.add(arr("key", Map.of("key", ""), "one + empty value + no equals"));
    l.add(arr("key1=value1&key2=value2", Map.of("key1", "value1", "key2", "value2"), "two"));
    l.add(arr("=value1&key2=value2", Map.of("", "value1", "key2", "value2"), "two + empty key1"));
    l.add(arr("key1=value1&=value2", Map.of("key1", "value1", "", "value2"), "two + empty key2"));
    l.add(arr("key1=&key2=value2", Map.of("key1", "", "key2", "value2"), "two + empty value1"));
    l.add(arr("key1=value1&key2=", Map.of("key1", "value1", "key2", ""), "two + empty value2"));
    l.add(arr("key1&key2=value2", Map.of("key1", "", "key2", "value2"), "two + empty value1 + no equals"));
    l.add(arr("key1=value1&key2", Map.of("key1", "value1", "key2", ""), "two + empty value2 + no equals"));
    l.add(arr("key=value1&key=value2", Map.of("key", List.of("value1", "value2")), "two + duplicate keys"));

    for (int value = 0; value < validBytes.length; value++) {
      switch (value) {
        case ' ' -> {/* will cause parsing to move to VERSION */}

        case '\n', '\r' -> {/* will trigger 505 not 400 */}

        case '&', '=' -> {/* valid in query string, but has special meaning*/}

        case '+' -> {
          l.add(arr("+=value", Map.of(" ", "value"), "key contains the '+' character"));
          l.add(arr("key=+", Map.of("key", " "), "value contains the '+' character"));
        }

        default -> {
          if (validBytes[value]) {
            l.add(queryValidKey(value));
            l.add(queryValidValue(value));
          }
        }
      }
    }

    return l.toArray(Object[][]::new);
  }

  private static Object[] queryValidKey(int value) {
    final String key;
    key = Character.toString(value);

    return arr(key + "=value", Map.of(key, "value"), "key contains the " + Integer.toHexString(value) + " valid byte");
  }

  private static Object[] queryValidValue(int value) {
    final String val;
    val = Character.toString(value);

    return arr("key=" + val, Map.of("key", val), "value contains the " + Integer.toHexString(value) + " valid byte");
  }

  static Object[] arr(Object... arr) {
    // not safe, oh well...
    return arr;
  }

  // ##################################################################
  // # END: URL Decode
  // ##################################################################

}
