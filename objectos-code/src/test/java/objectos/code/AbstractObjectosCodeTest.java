/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package objectos.code;

import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.annotations.BeforeClass;

public abstract class AbstractObjectosCodeTest {

  private final ObjectosCodeTest outer;

  AbstractObjectosCodeTest(ObjectosCodeTest outer) {
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

  final Object[] objs(Object... values) { return values; }

  final int[] pass0(int... values) { return values; }

  final int[] pass1(int... values) { return values; }

  final <T> T skip(T value) {
    return null;
  }

  final int[] t(int... values) { return values; }

  final void test(
      JavaTemplate template,
      int[] pass0,
      Object[] objs,
      int[] pass1,
      String expectedSource) {
    outer.test(template, pass0, objs, pass1, expectedSource);
  }

}