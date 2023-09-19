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

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import objectos.code.Code.ImportList;
import org.testng.annotations.Test;

public class ReadmeTest {

  @Test
  public void helloWorld() {
    Code code;
    code = Code.of();

    String packageName;
    packageName = "com.example";

    ImportList importList;
    importList = code.importList(packageName);

    String simpleName;
    simpleName = "HelloWorld";

    assertEquals(
      code."""
      package \{packageName};

      public class \{simpleName} {
        public static void main(String... args) {
          System.out.println("Hello, Objectos Code!");
        }
      }
      """,

      """
      package com.example;

      public class HelloWorld {
        public static void main(String... args) {
          System.out.println("Hello, Objectos Code!");
        }
      }
      """
    );
  }

}
