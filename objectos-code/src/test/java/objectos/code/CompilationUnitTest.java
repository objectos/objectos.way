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

import org.testng.annotations.Test;

public class CompilationUnitTest extends AbstractObjectoxCodeTest {

  @Test(description = """
  class Foo {}
  """)
  public void testCase01() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        _class(id("Foo"));
      }
    };

    testDefault(
      tmpl,

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
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        _package("test");

        _class(id("Foo"));
      }
    };

    testDefault(
      tmpl,

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
    var tmpl = new JavaTemplate() {
      final ClassName _Bar = ClassName.of(TEST, "Bar");

      @Override
      protected final void definition() {
        autoImports();

        _class(id("Foo"), _extends(_Bar));
      }
    };

    testDefault(
      tmpl,

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
    var tmpl = new JavaTemplate() {
      final ClassName _Thread = ClassName.of(Thread.class);

      @Override
      protected final void definition() {
        _package("test");

        autoImports();

        _class(id("Test"), _extends(_Thread));
      }
    };

    testDefault(
      tmpl,

      """
      package test;

      class Test extends Thread {}
      """
    );
  }

}