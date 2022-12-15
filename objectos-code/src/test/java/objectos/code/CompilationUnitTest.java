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
          _class(id("Foo"));
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
          _package("test");

          _class(id("Foo"));
        }
      }.toString(),

      """
      package test;

      class Foo {}
      """
    );
  }

  @Test(description = """
  import test.Bar;

  class Foo extends Bar {}
  """)
  public void testCase03() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          autoImports();

          _class(id("Foo"), _extends(t("test", "Bar")));
        }
      }.toString(),

      """
      import test.Bar;

      class Foo extends Bar {}
      """
    );
  }

  @Test(description = """
  autoImports() + java.lang
  """)
  public void testCase04() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _package("test");

          autoImports();

          _class(id("Test"), _extends(t(Thread.class)));
        }
      }.toString(),

      """
      package test;

      class Test extends Thread {}
      """
    );
  }

  @Test(description = """
  autoImports() + same package
  """)
  public void testCase05() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _package("test");

          autoImports();

          _class(id("Test"), _extends(t("test", "Bar")));
        }
      }.toString(),

      """
      package test;

      class Test extends Bar {}
      """
    );
  }

  @Test(description = """
  Compilation Unit TC06

  - multiple import declarations
  """)
  public void testCase06() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          autoImports();

          _class(id("Test0"), _extends(t("test", "A")));
          _class(id("Test1"), _extends(t("test", "B")));
          _class(id("Test2"), _extends(t("test", "C")));
        }
      }.toString(),

      """
      import test.A;
      import test.B;
      import test.C;

      class Test0 extends A {}

      class Test1 extends B {}

      class Test2 extends C {}
      """
    );
  }

  @Test(description = """
  Compilation Unit TC07

  - package declaration
  - multiple import declarations
  """)
  public void testCase07() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          _package("foo");

          autoImports();

          _class(id("Test0"), _extends(t("test", "A")));
          _class(id("Test1"), _extends(t("test", "B")));
          _class(id("Test2"), _extends(t("test", "C")));
        }
      }.toString(),

      """
      package foo;

      import test.A;
      import test.B;
      import test.C;

      class Test0 extends A {}

      class Test1 extends B {}

      class Test2 extends C {}
      """
    );
  }

}