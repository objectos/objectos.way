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

public class EnumDeclarationTest {

  //  @Test(description = """
  //  constants
  //  """)
  //  public void testCase01() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _enum("Test1");
  //          body(
  //            enumConstant("ONE")
  //          );
  //
  //          _enum("Test2");
  //          body(
  //            enumConstant("ONE"),
  //
  //            enumConstant("TWO")
  //          );
  //
  //          _enum("Test3");
  //          body(
  //            enumConstant("ONE"),
  //
  //            enumConstant("TWO"),
  //
  //            enumConstant("THREE")
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      enum Test1 {
  //        ONE;
  //      }
  //
  //      enum Test2 {
  //        ONE,
  //
  //        TWO;
  //      }
  //
  //      enum Test3 {
  //        ONE,
  //
  //        TWO,
  //
  //        THREE;
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  constants + args
  //  """)
  //  public void testCase02() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _enum("Test1");
  //          body(
  //            enumConstant("ONE", s("abc"))
  //          );
  //
  //          _enum("Test2");
  //          body(
  //            enumConstant("ONE", s("abc"), n("field"))
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      enum Test1 {
  //        ONE("abc");
  //      }
  //
  //      enum Test2 {
  //        ONE("abc", field);
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  modifiers (class level)
  //  """)
  //  public void testCase03() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _public();
  //          _enum("Test");
  //          body(
  //            enumConstant("ONE")
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      public enum Test {
  //        ONE;
  //      }
  //      """
  //    );
  //
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          at(t(Deprecated.class));
  //          _public();
  //          _enum("Test");
  //          body(
  //            enumConstant("ONE")
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      @java.lang.Deprecated
  //      public enum Test {
  //        ONE;
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  implements cause
  //  """)
  //  public void testCase04() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          _enum("Test1");
  //          _implements();
  //          t(Serializable.class);
  //          body(
  //            enumConstant("ONE")
  //          );
  //
  //          _enum("Test2");
  //          _implements();
  //          t(AutoCloseable.class);
  //          t(Serializable.class);
  //          body(
  //            enumConstant("ONE")
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      enum Test1 implements java.io.Serializable {
  //        ONE;
  //      }
  //
  //      enum Test2 implements java.lang.AutoCloseable, java.io.Serializable {
  //        ONE;
  //      }
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  Enum class declarations TC05
  //
  //  - enum
  //  """)
  //  public void testCase05() {
//    // @formatter:off
//    assertEquals(
//      new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          _public(); _enum("Test"); _implements(); t("test", "Iface"); body(
//            enumConstant("A", s("a")),
//
//            enumConstant("B", s("b")),
//
//            _private(), _final(), t(String.class), field("value"),
//
//            _private(), constructor(t(String.class), id("value")), block(
//              assign(_this().n("value"), n("value"))
//            ),
//
//            at(t(Override.class)),
//            _public(), _final(), t(String.class), method("toString"), block(
//              _return(), n("value")
//            )
//          );
//        }
//      }.toString(),
//      """
//      public enum Test implements test.Iface {
//        A("a"),
//
//        B("b");
//
//        private final java.lang.String value;
//
//        private Test(java.lang.String value) {
//          this.value = value;
//        }
//
//        @java.lang.Override
//        public final java.lang.String toString() {
//          return value;
//        }
//      }
//      """
//      // @formatter:on
  //    );
  //  }

}