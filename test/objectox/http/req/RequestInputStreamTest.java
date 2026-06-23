/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * input://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectox.http.req;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import module java.base;
import objectos.lang.Throwables;
import objectos.y.SocketY;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RequestInputStreamTest {

  @Test(description = "Read into initial buffer")
  public void read01() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final RequestInputStream input;
    input = input(128, req1);

    assertEquals(read0(input, 64), req1);
  }

  @Test(description = "Read into initial buffer")
  public void readByte01() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final RequestInputStream input;
    input = input(128, req1);

    assertEquals(readByte0(input, 64), req1);
  }

  @Test(description = "Read into initial buffer, subsequent requires resize")
  public void readByte02() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final String req2;
    req2 = "2".repeat(64);

    final String req3;
    req3 = "3".repeat(64);

    final RequestInputStream input;
    input = input(256, req1, req2, req3);

    assertEquals(readByte0(input, 64 + 64 + 64), req1 + req2 + req3);
  }

  @Test(description = "Overflow")
  public void readByte03() throws IOException {
    final String req1;
    req1 = "1".repeat(128);

    final String req2;
    req2 = "2".repeat(64);

    final RequestInputStream input;
    input = input(128, req1, req2);

    assertEquals(readByte0(input, 128), req1);

    try {
      input.readByte();

      Assert.fail();
    } catch (RequestInputStream.Overflow expected) {
      // noop
    }
  }

  @Test(description = "EOF")
  public void readByte04() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final RequestInputStream input;
    input = input(128, req1);

    assertEquals(readByte0(input, 64), req1);

    try {
      input.readByte();

      Assert.fail();
    } catch (RequestInputStream.Eof expected) {
      // noop
    }
  }

  @Test(description = "IOException")
  public void readByte05() throws IOException {
    final String req1;
    req1 = "1".repeat(64);

    final IOException exception;
    exception = Throwables.trimStackTrace(new IOException("Read Error"), 1);

    final RequestInputStream input;
    input = input(128, req1, exception);

    assertEquals(readByte0(input, 64), req1);

    try {
      input.readByte();

      Assert.fail();
    } catch (IOException expected) {
      assertSame(expected, exception);
    }
  }

  @Test(description = "false on -1")
  public void start01() throws IOException {
    final RequestInputStream input;
    input = input(128, "");

    assertEquals(input.start(), false);
  }

  @Test(description = "start should reset state")
  public void start02() throws IOException {
    final String in1;
    in1 = "1".repeat(64);

    final String in2;
    in2 = "2".repeat(64);

    final RequestInputStream input;
    input = input(64, in1, in2);

    assertEquals(input.start(), true);

    assertEquals(readByte0(input, 64), in1);

    assertEquals(input.bufferIndex(), 64);

    assertEquals(input.start(), true);

    assertEquals(readByte0(input, 64), in2);
  }

  private String read0(RequestInputStream socket, int len) throws IOException {
    final byte[] bytes;
    bytes = new byte[len];

    for (int i = 0; i < len; i++) {
      bytes[i] = (byte) socket.read();
    }

    return new String(bytes, StandardCharsets.UTF_8);
  }

  private String readByte0(RequestInputStream socket, int len) throws IOException {
    final byte[] bytes;
    bytes = new byte[len];

    for (int i = 0; i < len; i++) {
      bytes[i] = socket.readByte();
    }

    return new String(bytes, StandardCharsets.UTF_8);
  }

  private RequestInputStream input(int initial, Object... data) throws IOException {
    return RequestInputStream.of(
        initial,

        SocketY.of(data)
    );
  }

}