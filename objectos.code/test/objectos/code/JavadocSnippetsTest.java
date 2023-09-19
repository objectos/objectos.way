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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import objectos.code.Code.ImportList;
import org.testng.annotations.Test;

public class JavadocSnippetsTest {

  @Test
  public void code() {
    // @start region="Code01"
    // obtain the Code instance
    Code code = Code.of();

    // names for the generated type
    String packageName = "com.example";
    String simpleName = "HelloWorld";

    // imports will be relative to the 'com.example' pkg
    ImportList importList = code.importList(packageName);

    // the name for the java.util.List type
    ClassName LIST = ClassName.of(List.class);

    // the template for our generated type
    String result = code."""
      package \{packageName};
      \{importList}
      public class \{simpleName} {
        public static void main(String[] args) {
          \{LIST}<String> lines = \{LIST}.of("Hello", "Objectos Code!");
          for (var line : lines) {
            System.out.println(line);
          }
        }
      }
      """;

    System.out.println(result);
    // @end

    assertEquals(
      result,

      """
      package com.example;

      import java.util.List;

      public class HelloWorld {
        public static void main(String[] args) {
          List<String> lines = List.of("Hello", "Objectos Code!");
          for (var line : lines) {
            System.out.println(line);
          }
        }
      }
      """
    );
  }

  @Test
  public void code_escape() {
    // @start region="Code-escape"
    Code code = Code.of();

    String textBlock = """
    An "escape" function
    example!
    """;

    String escaped = Code.escape(textBlock);

    String result = code."""
    String asStringLiteral = "\{escaped}";
    """;

    System.out.println(result);
    // @end

    assertEquals(
      result,

      """
      String asStringLiteral = "An \\"escape\\" function\\nexample!\\n";
      """
    );
  }

  @Test
  public void code_importList01() {
    // @start region="ImportList01"
    Code code = Code.of();

    String packageName = "com.example";

    ImportList importList = code.importList(packageName);

    ClassName LIST = ClassName.of(java.util.List.class);
    ClassName LOCAL_DATE = ClassName.of(java.time.LocalDate.class);

    String result = code."""
    package \{packageName};
    \{importList}
    interface Example01 {
      \{LIST}<String> strings();
      \{LOCAL_DATE} localDate();
    }
    """;

    System.out.println(result);
    // @end

    assertEquals(
      result,

      """
      package com.example;

      import java.time.LocalDate;
      import java.util.List;

      interface Example01 {
        List<String> strings();
        LocalDate localDate();
      }
      """
    );
  }

  @Test
  public void code_importList02() {
    // @start region="ImportList02"
    Code code = Code.of();

    String packageName = "com.example";

    ImportList importList = code.importList(packageName);

    // java.lang
    ClassName THREAD = ClassName.of(java.lang.Thread.class);
    // com.example
    ClassName FOO = ClassName.of(packageName, "Foo");

    String result = code."""
    package \{packageName};
    \{importList}
    interface Example02 {
      \{THREAD} thread();
      \{FOO} foo();
    }
    """;

    System.out.println(result);
    // @end

    assertEquals(
      result,

      """
      package com.example;

      interface Example02 {
        Thread thread();
        Foo foo();
      }
      """
    );
  }

  @Test
  public void code_importList03() {
    // @start region="ImportList03"
    Code code = Code.of();

    String packageName = "com.example";

    ImportList importList = code.importList(packageName);

    ClassName LIST_01 = ClassName.of("java.util", "List");
    ClassName LIST_02 = ClassName.of(packageName, "List");
    ClassName LIST_03 = ClassName.of("java.awt", "List");

    String result = code."""
    package \{packageName};
    \{importList}
    interface Example03 {
      \{LIST_01}<String> list01();
      \{LIST_02}<String> list02();
      \{LIST_03}<String> list03();
    }
    """;

    System.out.println(result);
    // @end

    assertEquals(
      result,

      """
      package com.example;

      import java.util.List;

      interface Example03 {
        List<String> list01();
        com.example.List<String> list02();
        java.awt.List<String> list03();
      }
      """
    );
  }

}