/*
 * Copyright 2022 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.code;

import static objectos.code.Pass0.JMP;

import org.testng.annotations.Test;

final class CompilationUnitTest extends AbstractObjectosCodeTest {

  CompilationUnitTest(ObjectosCodeTest outer) { super(outer); }

  @Test(description = """
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

      pass0(
        /* 0*/Pass0.JMP, 12,
        /* 2*/Pass0.IDENTIFIER, 0, JMP, 10,
        /* 6*/Pass0.CLASS, 1, JMP, 2, JMP, 16,
        /*12*/Pass0.COMPILATION_UNIT, 1, JMP, 6, JMP, 18,
        /*18*/Pass0.EOF
      ),

      """
      class Foo {}
      """
    );
  }

}