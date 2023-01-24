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

public class IncludeTest {

  //  @Test(description = """
  //  - single include
  //  - single statement
  //  """)
  //  public void testCase01() {
//    // @formatter:off
//    assertEquals(
//      new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          _class("TestCase01"); body(
//            include(this::body0)
//          );
//        }
//
//        private void body0() {
//          _int(); field("a");
//        }
//      }.toString(),
//
//      """
//      class TestCase01 {
//        int a;
//      }
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  - single include
  //  - many statements
  //  """)
  //  public void testCase02() {
//    // @formatter:off
//    assertEquals(
//      new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          _class("Test");
//          body(
//            _static(), block(
//              invoke("test", include(this::body0))
//            )
//          );
//        }
//
//        private void body0() {
//          invoke("a");
//          invoke("b");
//          invoke("c");
//        }
//      }.toString(),
//
//      """
//      class Test {
//        static {
//          test(a(), b(), c());
//        }
//      }
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  - many includes
  //  - same level
  //  """)
  //  public void testCase03() {
//    // @formatter:off
//    assertEquals(
//      new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          _class("Test");
//          body(
//            include(this::body1),
//
//            _int(), field("d"),
//
//            include(this::body2)
//          );
//        }
//
//        private void body1() {
//          _int(); field("a");
//
//          _int(); field("b");
//
//          _int(); field("c");
//        }
//
//        private void body2() {
//          _int(); field("e");
//
//          _int(); field("f");
//        }
//      }.toString(),
//      """
//      class Test {
//        int a;
//
//        int b;
//
//        int c;
//
//        int d;
//
//        int e;
//
//        int f;
//      }
//      """
//    );
//    // @formatter:on
  //  }

}