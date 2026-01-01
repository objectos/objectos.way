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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.testng.Assert;
import org.testng.annotations.Test;

public class YSlowStreamTest {

  @Test
  public void testCase01() {
    try (InputStream input = Y.slowStream(1, "abc")) {
      byte[] buf;
      buf = new byte[4];

      assertEquals(input.read(buf, 0, 4), 1);
      assertEquals(input.read(buf, 1, 3), 1);
      assertEquals(input.read(buf, 2, 2), 1);
      assertEquals(input.read(buf, 3, 1), -1);
      assertEquals(new String(buf, 0, 3, StandardCharsets.US_ASCII), "abc");
    } catch (IOException e) {
      Assert.fail("It should not have thrown", e);
    }
  }

}