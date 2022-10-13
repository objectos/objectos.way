/*
 * Copyright 2022 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.code;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import org.testng.annotations.Test;

public class JavaTemplateTest {

  @Test(enabled = false, description = """
  class Foo {}
  """)
  public void testCase01() {
    test(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _class(id("Foo"));
        }
      },

      codes(
        Code.IDENTIFIER, 0,
        Code.JMP, 0,
        Code.CLASS, Code.JMP, 0, Code.JMP, 0,
        Code.COMPILATION_UNIT, Code.JMP, 0
      ),

      """
      class Foo {}
      """
    );
  }

  final void testArrays(int[] result, int[] expected, String header) {
    var msg = """

    %s
    actual  =%s
    expected=%s

    """.formatted(header, Arrays.toString(result), Arrays.toString(expected));

    assertEquals(result, expected, msg);
  }

  private int[] codes(int... values) {
    return values;
  }

  private void test(JavaTemplate tmpl, int[] codes, String expected) {
    var gen = new JavaGeneratorImpl();

    tmpl.acceptJavaGenerator(gen);

    testArrays(gen.codes(), codes, "Codes");
  }

}