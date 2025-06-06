/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
import org.testng.annotations.Test;

public class YMediaTextTest {

  @Test(description = """
  It fits in the first line
  """)
  public void testCase01() {
    test(
        Y.mediaTextOf(32),

        """
        12345678901234567890123456789012"""
    );
  }

  @Test(description = """
  It fits in two lines
  """)
  public void testCase02() {
    test(
        Y.mediaTextOf(82),

        """
        .................................................
        12345678901234567890123456789012"""
    );
  }

  private void test(Media.Text writable, String expected) {
    try {
      final StringBuilder sb;
      sb = new StringBuilder();

      writable.writeTo(sb);

      final String result;
      result = sb.toString();

      assertEquals(result, expected);
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw", e);
    }
  }

}