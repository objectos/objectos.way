/*
 * Copyright (C) 2022-2022 Objectos Software LTDA.
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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class StringsTest {

  @Test
  public void nullToEmpty() {
    assertEquals(Strings.nullToEmpty(null), "");
    assertEquals(Strings.nullToEmpty(""), "");
    assertEquals(Strings.nullToEmpty("abc"), "abc");
  }

  @Test
  public void toCodePoints() {
    toCodePointsTest("");
    toCodePointsTest("A");
    toCodePointsTest("字");
    toCodePointsTest("ascii ASCII 0123");
    toCodePointsTest("aáàâãEÉÈÊẼ");
    toCodePointsTest("漢字");
    toCodePointsTest("ストリング");
    toCodePointsTest("𑀐");
    toCodePointsTest("𑀐𑀑");
    toCodePointsTest("𑀐𑀑ascii");
    toCodePointsTest("𐌰𐌱𐌲𐌳");
  }

  private void toCodePointsTest(String source) {
    int[] codePoints;
    codePoints = Strings.toCodePoints(source);

    StringBuilder result;
    result = new StringBuilder();

    for (int i = 0; i < codePoints.length; i++) {
      int codePoint;
      codePoint = codePoints[i];

      result.appendCodePoint(codePoint);
    }

    assertEquals(result.toString(), source);
  }

}