/*
 * Copyright 2022 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
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

  final int[] pass0(int... values) { return values; }

  final <T> T skip(T value) {
    return null;
  }

  final int[] t(int... values) { return values; }

  final void test(
      JavaTemplate template,
      int[] pass0,
      String expectedSource) {
    outer.test(template, pass0, expectedSource);
  }

}