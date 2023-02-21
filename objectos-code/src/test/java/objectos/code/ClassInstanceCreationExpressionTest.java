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

import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import org.testng.annotations.Test;

public class ClassInstanceCreationExpressionTest {

  @Test(description = """
  Class Instance Creation Expressions TC01

  - new ClassOrInterfaceTypeToInstantiate ( [ArgumentList] )
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName FOO = classType("objectos.code", "Foo");

        @Override
        protected final void definition() {
          _class("New");
          body(
            method(
              NEW, t(FOO),

              NEW, t(FOO, s("a")),

              NEW, t(FOO, s("a"), s("b"))
            )
          );
        }
      }.toString(),

      """
      class New {
        void unnamed() {
          new objectos.code.Foo();
          new objectos.code.Foo("a");
          new objectos.code.Foo("a", "b");
        }
      }
      """
    );
  }

  @Test(description = """
  Class Instance Creation Expressions TC02

  - parameterized type
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        static final ParameterizedTypeName HASHMAP = parameterizedType(
          classType(HashMap.class),
          classType(String.class),
          classType(Integer.class)
        );

        @Override
        protected final void definition() {
          _class("New");
          body(
            method(
              NEW, t(HASHMAP)
            )
          );
        }
      }.toString(),

      """
      class New {
        void unnamed() {
          new java.util.HashMap<java.lang.String, java.lang.Integer>();
        }
      }
      """
    );
  }

}