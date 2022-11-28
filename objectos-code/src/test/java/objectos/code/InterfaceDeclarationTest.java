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

import static org.testng.Assert.assertEquals;

import objectos.code.Types.IfaceA;
import objectos.code.Types.IfaceB;
import objectos.code.Types.IfaceC;
import org.testng.annotations.Test;

public class InterfaceDeclarationTest {

  @Test(description = """
  Interface declaration TC01

  - identifier
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _interface(id("A"));
        }
      }.toString(),

      """
      interface A {}
      """
    );
  }

  @Test(description = """
  Interface declaration TC02

  - modifiers
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _interface(_public(), id("A"));

          _interface(annotation(t(TypeAnnotation.class)), id("B"));

          _interface(annotation(t(TypeAnnotation.class)), _protected(), id("C"));

          _interface(_private(), _static(), annotation(t(TypeAnnotation.class)), id("D"));
        }
      }.toString(),

      """
      public interface A {}

      @objectos.code.TypeAnnotation
      interface B {}

      @objectos.code.TypeAnnotation
      protected interface C {}

      private static @objectos.code.TypeAnnotation interface D {}
      """
    );
  }

  @Test(description = """
  Interface declaration TC03

  - extends clause
  """)
  public void testCase03() {
    var a = ClassName.of(IfaceA.class);
    var b = ClassName.of(IfaceB.class);
    var c = ClassName.of(IfaceC.class);

    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _interface(id("A"), _extends(a));

          _interface(id("B"), _extends(a, b));

          _interface(
            id("C"),
            _extends(a, b),
            _extends(c)
          );

          _interface(
            id("D"),
            include(this::interfaceD)
          );
        }

        private void interfaceD() {
          _extends(a);
          _extends(b);
          _extends(c);
        }
      }.toString(),

      """
      interface A extends objectos.code.Types.IfaceA {}

      interface B extends objectos.code.Types.IfaceA, objectos.code.Types.IfaceB {}

      interface C extends objectos.code.Types.IfaceA, objectos.code.Types.IfaceB, objectos.code.Types.IfaceC {}

      interface D extends objectos.code.Types.IfaceA, objectos.code.Types.IfaceB, objectos.code.Types.IfaceC {}
      """
    );
  }

}