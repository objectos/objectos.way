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

public class AnnotationTest extends AbstractObjectosCodeTest {

  @Test(description = """
  single element annotation + string literal
  """)
  public void testCase01() {
    var tmpl = new JavaTemplate() {
      final ClassName _Foo = ClassName.of(TEST, "Foo");

      @Override
      protected final void definition() {
        _class(
          annotation(_Foo, s("java")),
          id("Test")
        );
      }
    };

    testDefault(
      tmpl,

      """
      @test.Foo("java")
      class Test {}
      """
    );
  }

}