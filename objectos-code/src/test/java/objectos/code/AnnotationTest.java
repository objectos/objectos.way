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

import objectos.code.type.ClassTypeName;
import org.testng.annotations.Test;

public class AnnotationTest {

  @Test(description = """
  single element annotation + string literal
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName FOO = ClassTypeName.of("com.example", "Foo");

        @Override
        protected final void definition() {
          classDeclaration(
            annotation(
              FOO,
              annotationValue(s("java"))
            ),
            name("Test")
          );
        }
      }.toString(),

      """
      @com.example.Foo("java")
      class Test {}
      """
    );
  }

  @Test(description = """
  Annotation TC02:

  annotation(Override.class)
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        static final ClassTypeName OVERRIDE = ClassTypeName.of(Override.class);

        @Override
        protected final void definition() {
          classDeclaration(
            name("Annotation"),

            method(
              annotation(OVERRIDE)
            )
          );
        }
      }.toString(),

      """
      class Annotation {
        @java.lang.Override
        void unnamed() {}
      }
      """
    );
  }

}