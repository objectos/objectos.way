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

public class ClassTypeTest {

  @Test(description = """
  ClassType TC01

  - pkg + name
  """)
  public void testCase01() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          classDeclaration(
            name("ClassType"),

            field(
              ClassTypeName.of("com.example", "Bar"), name("a")
            )
          );
        }
      }.toString(),

      """
      class ClassType {
        com.example.Bar a;
      }
      """
    );
  }

  @Test(description = """
  ClassType TC02

  - nested class
  """)
  public void testCase02() {
    assertEquals(
      new JavaTemplate() {
        @Override
        protected final void definition() {
          packageDeclaration("com.example");

          autoImports();

          classDeclaration(
            name("A"),

            field(
              ClassTypeName.of("com.example", "A", "B"), name("t01")
            )
          );
        }
      }.toString(),

      """
      package com.example;

      class A {
        B t01;
      }
      """
    );
  }

}