/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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

public class FieldDeclarationTest {

  //  private final Fixture fix = new Fixture("Fields", Kind.CLASS);
  //
  //  @Test(description = """
  //  typeName + names
  //  """)
  //  public void testCase01() {
//    // @formatter:off
//    assertEquals(
//      fix.ture(new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          t(String.class); field("name");
//
//          t(String.class); field("a"); field("b"); field("c");
//        }
//      }),
//
//      """
//      class Fields {
//        java.lang.String name;
//
//        java.lang.String a, b, c;
//      }
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  modifiers
  //  """)
  //  public void testCase02() {
//    // @formatter:off
//    assertEquals(
//      fix.ture(new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          _private(); t(String.class); field("a");
//
//          _private(); _final(); t(String.class); field("b");
//
//          _private(); _static(); _final(); t(String.class); field("c");
//        }
//      }),
//
//      """
//      class Fields {
//        private java.lang.String a;
//
//        private final java.lang.String b;
//
//        private static final java.lang.String c;
//      }
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  init expression
  //  """)
  //  public void testCase03() {
//    // @formatter:off
//    assertEquals(
//      fix.ture(new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          t(String.class); field("x"); s("init");
//
//          t(String.class); field("a"); s("a"); field("b"); field("c"); s("c");
//
//          t(String.class); field("d"); invoke("d0"); invoke("d1");
//        }
//      }),
//
//      """
//      class Fields {
//        java.lang.String x = "init";
//
//        java.lang.String a = "a", b, c = "c";
//
//        java.lang.String d = d0().d1();
//      }
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  Field declarations TC05
  //
  //  - array type
  //  """)
  //  public void testCase04() {
//    // @formatter:off
//    assertEquals(
//      fix.ture(new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          t(t(Object.class), dim()); field("a");
//        }
//      }),
//
//      """
//      class Fields {
//        java.lang.Object[] a;
//      }
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  Field declarations TC05
  //
  //  - parameterized type
  //  """)
  //  public void testCase05() {
//    // @formatter:off
//    assertEquals(
//      fix.ture(new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          t(t(Map.class), t(String.class), t(Integer.class)); field("map");
//        }
//      }),
//
//      """
//      class Fields {
//        java.util.Map<java.lang.String, java.lang.Integer> map;
//      }
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  Field declarations TC06
  //
  //  - array initializer
  //  """)
  //  public void testCase06() {
//    // @formatter:off
//    assertEquals(
//      fix.ture(new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          t(_int(), dim()); field("a"); ainit();
//
//          t(_int(), dim()); field("b"); ainit(i(0));
//
//          t(_int(), dim()); field("c"); ainit(i(0), i(1));
//        }
//      }),
//
//      """
//      class Fields {
//        int[] a = {};
//
//        int[] b = {0};
//
//        int[] c = {0, 1};
//      }
//      """
//    );
//    // @formatter:on
  //  }

}