/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
package objectos.lang;

import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import org.testng.annotations.Test;

public class StripDiacritics {

  static void fill(StringBuilder s, StringBuilder r) {
    // Upper
    add(s, r, 'A', '\u00C0', 6);

    add(s, r, 'C', '\u00C7');

    add(s, r, 'E', '\u00C8', 4);

    add(s, r, 'I', '\u00CC', 4);

    //add(s, r, 'D', '\u00D0');

    add(s, r, 'N', '\u00D1');

    add(s, r, 'O', '\u00D2', 5);

    //add(s, r, 'O', '\u00D8');

    add(s, r, 'U', '\u00D9', 4);

    add(s, r, 'Y', '\u00DD');

    int offset = 0x20;

    // Lower
    add(s, r, 'a', '\u00C0' + offset, 6);

    add(s, r, 'c', '\u00C7' + offset);

    add(s, r, 'e', '\u00C8' + offset, 4);

    add(s, r, 'i', '\u00CC' + offset, 4);

    //add(s, r, 'd', '\u00D0' + offset);

    add(s, r, 'n', '\u00D1' + offset);

    add(s, r, 'o', '\u00D2' + offset, 5);

    //add(s, r, 'o', '\u00D8' + offset);

    add(s, r, 'u', '\u00D9' + offset, 4);

    add(s, r, 'y', '\u00DD' + offset);
  }

  private static void add(StringBuilder s, StringBuilder r, char stripped, int c) {
    s.appendCodePoint(c);

    r.append(stripped);
  }

  private static void add(StringBuilder s, StringBuilder r, char stripped, int start, int count) {
    for (int i = 0; i < count; i++) {
      s.appendCodePoint(start + i);

      r.append(stripped);
    }
  }

  @Test(enabled = false)
  public void pseudoCodeGen() {
    StringBuilder s;
    s = new StringBuilder();

    StringBuilder r;
    r = new StringBuilder();

    fill(s, r);

    String source = s.toString();

    char[] array = source.toCharArray();

    Arrays.sort(array);

    assertTrue(Arrays.equals(array, source.toCharArray()));

    String target = r.toString();

    System.out.println(source);

    System.out.println(target);
  }

}