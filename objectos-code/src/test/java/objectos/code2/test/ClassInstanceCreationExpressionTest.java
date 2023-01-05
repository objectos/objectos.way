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
package objectos.code2.test;

import static org.testng.Assert.assertEquals;

import objectos.code2.JavaTemplate;
import objectos.code2.test.Fixture.Kind;
import org.testng.annotations.Test;

public class ClassInstanceCreationExpressionTest {

  private final Fixture fix = new Fixture("New", Kind.STATIC_BLOCK);

  @Test(description = """
  Class Instance Creation Expressions TC01

  - new ClassOrInterfaceTypeToInstantiate ( [ArgumentList] )
  """)
  public void testCase01() {
    assertEquals(
      fix.ture(new JavaTemplate() {
        @Override
        protected final void definition() {
          _new(t("objectos.code", "Foo"));

          _new(t("objectos.code", "Foo"), s("a"));

          _new(t("objectos.code", "Foo"), s("a"), s("b"));
        }
      }),

      """
      class New {
        static {
          new objectos.code.Foo();
          new objectos.code.Foo("a");
          new objectos.code.Foo("a", "b");
        }
      }
      """
    );
  }

}