/*
 * Copyright 2022 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.code;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;

public class ObjectosCodeTest {

  @BeforeClass
  public void _beforeClass() {
  }

  @Factory
  public Object[] _factory() {
    return new Object[] {
        new CompilationUnitTest(this)
    };
  }

  void test(JavaTemplate template, int[] pass0, String expectedSource) {
    // noop for now...
  }

  final void testArrays(int[] result, int[] expected, String header) {
    var msg = """

    %s
    actual  =%s
    expected=%s

    """.formatted(header, Arrays.toString(result), Arrays.toString(expected));

    assertEquals(result, expected, msg);
  }

}