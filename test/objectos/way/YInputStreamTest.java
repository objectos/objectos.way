/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.testng.Assert;
import org.testng.annotations.Test;

public class YInputStreamTest {

  @Test
  public void testCase01() {
    try (InputStream input = Y.inputStream("abc")) {
      byte[] buf;
      buf = new byte[3];

      assertEquals(input.read(buf), 3);
      assertEquals(ascii(buf), "abc");
      assertEquals(input.read(buf), -1);
    } catch (IOException e) {
      Assert.fail("It should not have thrown", e);
    }
  }

  @Test
  public void testCase02() {
    try (InputStream input = Y.inputStream("abc")) {
      byte[] buf;
      buf = new byte[10];

      assertEquals(input.read(buf), 3);
      assertEquals(ascii(buf, 0, 3), "abc");
      assertEquals(input.read(buf), -1);
    } catch (IOException e) {
      Assert.fail("It should not have thrown", e);
    }
  }

  @Test
  public void testCase04() {
    try (InputStream input = Y.inputStream("abc123")) {
      byte[] buf;
      buf = new byte[3];

      assertEquals(input.read(buf), 3);
      assertEquals(ascii(buf), "abc");
      assertEquals(input.read(buf), 3);
      assertEquals(ascii(buf), "123");
      assertEquals(input.read(buf), -1);
    } catch (IOException e) {
      Assert.fail("It should not have thrown", e);
    }
  }

  @Test
  public void testCase03() {
    try (InputStream input = Y.inputStream("abc", "123")) {
      byte[] buf;
      buf = new byte[10];

      assertEquals(input.read(buf), 3);
      assertEquals(ascii(buf, 0, 3), "abc");
      assertEquals(input.read(buf), 3);
      assertEquals(ascii(buf, 0, 3), "123");
      assertEquals(input.read(buf), -1);
    } catch (IOException e) {
      Assert.fail("It should not have thrown", e);
    }
  }

  @Test
  public void testCase05() {
    IOException readError;
    readError = new IOException("On read");

    try (InputStream input = Y.inputStream("abc", readError)) {
      byte[] buf;
      buf = new byte[3];

      assertEquals(input.read(buf), 3);
      assertEquals(ascii(buf, 0, 3), "abc");
      assertEquals(input.read(buf), 3);
      Assert.fail("It should have thrown");
    } catch (IOException e) {
      assertSame(e, readError);
    }
  }

  @Test
  public void testCase06() {
    IOException closeError;
    closeError = new IOException("On close");

    try (InputStream input = Y.inputStream(config -> {
      config.add("abc");

      config.add("123");

      config.onClose(closeError);
    })) {

      byte[] buf;
      buf = new byte[3];

      assertEquals(input.read(buf), 3);
      assertEquals(ascii(buf), "abc");
      assertEquals(input.read(buf), 3);
      assertEquals(ascii(buf), "123");
      assertEquals(input.read(buf), -1);
    } catch (IOException e) {
      assertSame(e, closeError);
    }
  }

  @Test
  public void testCase07() {
    byte[] hello = "Hello".getBytes(StandardCharsets.UTF_8);
    byte[] world = "World".getBytes(StandardCharsets.UTF_8);

    try (InputStream input = Y.inputStream(hello, world)) {
      byte[] buf;
      buf = new byte[5];

      assertEquals(input.read(buf), 5);
      assertEquals(ascii(buf), "Hello");
      assertEquals(input.read(buf), 5);
      assertEquals(ascii(buf), "World");
      assertEquals(input.read(buf), -1);
    } catch (IOException e) {
      Assert.fail("It should not have thrown", e);
    }
  }

  @Test
  public void testCase08() {
    try (InputStream src = Y.inputStream("Hello World!");
        InputStream input = new BufferedInputStream(src);
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      int c;

      while ((c = input.read()) != -1) {
        out.write(c);
      }

      byte[] bytes;
      bytes = out.toByteArray();

      assertEquals(ascii(bytes), "Hello World!");
    } catch (IOException e) {
      Assert.fail("It should not have thrown", e);
    }
  }

  private String ascii(byte[] bytes) {
    return new String(bytes, StandardCharsets.US_ASCII);
  }

  private String ascii(byte[] bytes, int offset, int len) {
    return new String(bytes, offset, len, StandardCharsets.US_ASCII);
  }

}