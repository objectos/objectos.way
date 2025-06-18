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

import org.testng.annotations.Test;

public class AsciiTest {

  @Test
  public void visible() {
    final byte[] table;
    table = new byte[128];

    final String visible;
    visible = Ascii.visible();

    Ascii.fill(table, visible, (byte) 1);

    for (int c = 0; c < table.length; c++) {
      final byte flag;
      flag = table[c];

      if (0x21 <= c && c <= 0x7E) {
        assertEquals(flag, (byte) 1);
      } else {
        assertEquals(flag, (byte) 0);
      }
    }
  }

}