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

import org.testng.annotations.Test;

public class CompilationUnitTest {

  @Test(description = """
    class Foo {}
    """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          code(_class("Foo"), body());
        }
      }.toString(),

      """
      class Foo {}
      """
    );
  }

  @Test(description = """
    package test;

    class Foo {}
    """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          code(
            _package("test"),

            _class("Foo"), body()
          );
        }
      }.toString(),

      """
      package test;

      class Foo {}
      """
    );
  }
  //
  //  @Test(description = """
  //  import test.Bar;
  //
  //  class Foo extends Bar {}
  //  """)
  //  public void testCase03() {
//    // @formatter:off
//    assertEquals(
//      new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          autoImports();
//
//          _class("Foo"); _extends(); t("test", "Bar"); body();
//        }
//      }.toString(),
//
//      """
//      import test.Bar;
//
//      class Foo extends Bar {}
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  autoImports() + java.lang
  //  """)
  //  public void testCase04() {
//    // @formatter:off
//    assertEquals(
//      new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          _package("test");
//
//          autoImports();
//
//          _class("Test"); _extends(); t(Thread.class); body();
//        }
//      }.toString(),
//
//      """
//      package test;
//
//      class Test extends Thread {}
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  autoImports() + same package
  //  """)
  //  public void testCase05() {
//    // @formatter:off
//    assertEquals(
//      new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          _package("test");
//
//          autoImports();
//
//          _class("Test"); _extends(); t("test", "Bar"); body();
//        }
//      }.toString(),
//
//      """
//      package test;
//
//      class Test extends Bar {}
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  Compilation Unit TC06
  //
  //  - multiple import declarations
  //  """)
  //  public void testCase06() {
//    // @formatter:off
//    assertEquals(
//      new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          autoImports();
//
//          _class("Test0"); _extends(); t("test", "A"); body();
//          _class("Test1"); _extends(); t("test", "B"); body();
//          _class("Test2"); _extends(); t("test", "C"); body();
//        }
//      }.toString(),
//
//      """
//      import test.A;
//      import test.B;
//      import test.C;
//
//      class Test0 extends A {}
//
//      class Test1 extends B {}
//
//      class Test2 extends C {}
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  Compilation Unit TC07
  //
  //  - package declaration
  //  - multiple import declarations
  //  """)
  //  public void testCase07() {
//    // @formatter:off
//    assertEquals(
//      new JavaTemplate() {
//        @Override
//        protected final void definition() {
//          _package("foo");
//
//          autoImports();
//
//          _class("Test0"); _extends(); t("test", "A"); body();
//          _class("Test1"); _extends(); t("test", "B"); body();
//          _class("Test2"); _extends(); t("test", "C"); body();
//        }
//      }.toString(),
//
//      """
//      package foo;
//
//      import test.A;
//      import test.B;
//      import test.C;
//
//      class Test0 extends A {}
//
//      class Test1 extends B {}
//
//      class Test2 extends C {}
//      """
//    );
//    // @formatter:on
  //  }
  //
  //  @Test(description = """
  //  Compilation Unit TC08
  //
  //  - import declarations + annotation
  //  """)
  //  public void testCase08() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          code(
  //            _package("foo"),
  //
  //            autoImports(),
  //
  //            at(t("bar", "TypeAnnotation")),
  //            _class("Test"), body()
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      package foo;
  //
  //      import bar.TypeAnnotation;
  //
  //      @TypeAnnotation
  //      class Test {}
  //      """
  //    );
  //  }
  //
  //  @Test(description = """
  //  Compilation Unit TC09
  //
  //  - import declarations + modifier
  //  """)
  //  public void testCase09() {
  //    assertEquals(
  //      new JavaTemplate() {
  //        @Override
  //        protected final void definition() {
  //          code(
  //            _package("foo"),
  //
  //            autoImports(),
  //
  //            _public(), _class("Test"), _extends(), t("bar", "Super"), body()
  //          );
  //        }
  //      }.toString(),
  //
  //      """
  //      package foo;
  //
  //      import bar.Super;
  //
  //      public class Test extends Super {}
  //      """
  //    );
  //  }

}