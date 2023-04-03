/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.asciidoc;

import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.annotations.BeforeClass;

public abstract class AbstractAsciiDocTest {

  private final AsciiDocTest outer;

  AbstractAsciiDocTest(AsciiDocTest outer) {
    this.outer = outer;
  }

  @BeforeClass
  public void _beforeClass() {
    outer._beforeClass();
  }

  final Map<String, String> docAttr(String... pairs) {
    var map = new LinkedHashMap<String, String>(pairs.length);

    var index = 0;

    while (index < pairs.length) {
      var key = pairs[index++];
      var value = pairs[index++];

      map.put(key, value);
    }

    return map;
  }

  final int[] p0(int... values) { return values; }

  final int[] p1(int... values) { return values; }

  final int[][] p2(int[]... values) { return values; }

  final <T> T skip(T value) {
    return null;
  }

  final int[] t(int... values) { return values; }

  final void test(
      String source,
      int[] p0,
      int[] p1, Map<String, String> docAttr,
      int[][] p2,
      String expectedHtml) {
    outer.test(source, p0, p1, docAttr, p2, expectedHtml);
  }

}