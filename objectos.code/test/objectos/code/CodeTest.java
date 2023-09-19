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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import objectos.code.Code.ImportList;
import org.testng.annotations.Test;

public class CodeTest {

  @Test(description = """
  Interpolation
  """)
  public void generate01() {
    Code code;
    code = Code.of();

    String simpleName;
    simpleName = "Foo";

    assertEquals(
      code."""
      class \{simpleName} {}
      """,

      """
      class Foo {}
      """
    );
  }

  @Test(description = """
  Iteration
  """)
  public void generate02() {
    Code code;
    code = Code.of();

    List<String> constantNames;
    constantNames = List.of("ONE", "TWO", "THREE");

    String constants;
    constants = constantNames.stream().collect(Collectors.joining(",\n\n", "", ";"));

    constants = Code.indent(constants, 2);

    assertEquals(
      code."""
      enum Foo {
      \{constants}
      }
      """,

      """
      enum Foo {
        ONE,

        TWO,

        THREE;
      }
      """
    );
  }

  @Test(description = """
  Support the \\{importList} substitution
  """)
  public void generate03() {
    Code code;
    code = Code.of();

    String packageName;
    packageName = "com.example";

    ImportList importList;
    importList = code.importList(packageName);

    ClassName serializable;
    serializable = ClassName.of(Serializable.class);

    assertEquals(
      code."""
      package \{packageName};
      \{importList}
      public class Foo implements \{serializable} {}
      """,

      """
      package com.example;

      import java.io.Serializable;

      public class Foo implements Serializable {}
      """
    );
  }

  @Test(description = """
  Support the empty \\{importList} substitution
  """)
  public void generate04() {
    Code code;
    code = Code.of();

    String packageName;
    packageName = "com.example";

    ImportList importList;
    importList = code.importList(packageName);

    assertEquals(
      code."""
      package \{packageName};
      \{importList}
      public class Foo {}
      """,

      """
      package com.example;

      public class Foo {}
      """
    );
  }

  @Test(description = """
  Support the escape function
  """)
  public void generate05() {
    Code code;
    code = Code.of();

    String test;
    test = "test is \\,\",\b,\t,\n,\f,\r";

    test = Code.escape(test);

    assertEquals(
      code."""
      String test = "\{test}";
      """,

      "String test = \"test is \\\\,\\\",\\b,\\t,\\n,\\f,\\r\";\n"
    );
  }

  @Test(description = """
  PrimitiveTypeName
  """)
  public void generate06() {
    Code code;
    code = Code.of();

    String packageName;
    packageName = "com.example";

    ImportList importList;
    importList = code.importList(packageName);

    TypeName localDate;
    localDate = ClassName.of(LocalDate.class);

    TypeName bool;
    bool = PrimitiveTypeName.BOOLEAN;

    assertEquals(
      code."""
      package \{packageName};
      \{importList}
      interface Foo {
        \{localDate} a();

        \{bool} b();
      }
      """,

      """
      package com.example;

      import java.time.LocalDate;

      interface Foo {
        LocalDate a();

        boolean b();
      }
      """
    );
  }

}
