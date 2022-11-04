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

public class ClassTest extends AbstractObjectosCodeTest {

  @Test(description = """
  final class Subject {}
  """)
  public void testCase01() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        _class(_final(), id("Subject"));
      }
    };

    testDefault(
      tmpl,

      """
      final class Subject {}
      """
    );
  }

  @Test(description = """
  single annotation on class

  @java.lang.Deprecated
  class Subject {}
  """)
  public void testCase02() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        _class(
          annotation(Deprecated.class),
          id("Subject")
        );
      }
    };

    testDefault(
      tmpl,

      """
      @java.lang.Deprecated
      class Subject {}
      """
    );
  }

  @Test(description = """
  single method

  class Subject {
    void m0() {}
  }
  """)
  public void testCase03() {
    var tmpl = new JavaTemplate() {
      @Override
      protected final void definition() {
        _class(
          id("Subject"),
          method(id("m0"))
        );
      }
    };

    testDefault(
      tmpl,

      """
      class Subject {
        void m0() {}
      }
      """
    );
  }

}