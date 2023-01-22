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

public class InterfaceDeclarationTest {

  //  @Test(description = """
  //  Interface declaration TC01
  //
  //  - identifier
  //  """)
  //  public void testCase01() {
//    // @formatter:off
//    assertEquals(
//      new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          _interface("A"); body();
//        }
//      }.toString(),
//
//      """
//      interface A {}
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  Interface declaration TC02
  //
  //  - modifiers
  //  """)
  //  public void testCase02() {
//    // @formatter:off
//    assertEquals(
//      new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          _public(); _interface("A"); body();
//
//          at(t(TypeAnnotation.class));
//          _interface("B"); body();
//
//          at(t(TypeAnnotation.class));
//          _protected(); _interface("C"); body();
//
//          _private(); _static(); at(t(TypeAnnotation.class)); _interface("D"); body();
//        }
//      }.toString(),
//
//      """
//      public interface A {}
//
//      @objectos.code.TypeAnnotation
//      interface B {}
//
//      @objectos.code.TypeAnnotation
//      protected interface C {}
//
//      private static @objectos.code.TypeAnnotation interface D {}
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  Interface declaration TC03
  //
  //  - extends clause
  //  """)
  //  public void testCase03() {
//    // @formatter:off
//    assertEquals(
//      new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          _interface("A"); _extends(); t(IfaceA.class); body();
//
//          _interface("B"); _extends(); t(IfaceA.class); t(IfaceB.class); body();
//
//          _interface("C"); _extends(); t(IfaceA.class); t(IfaceB.class); t(IfaceC.class); body();
//        }
//      }.toString(),
//
//      """
//      interface A extends objectos.code.Types.IfaceA {}
//
//      interface B extends objectos.code.Types.IfaceA, objectos.code.Types.IfaceB {}
//
//      interface C extends objectos.code.Types.IfaceA, objectos.code.Types.IfaceB, objectos.code.Types.IfaceC {}
//      """
//    );
//    // @formatter:on
  //  }

}