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
package objectos.code.internal;

import static org.testng.Assert.assertEquals;

import java.io.Serializable;
import java.util.List;
import org.testng.annotations.Test;

public class CodeImportListTest {

  @Test(description = """
  packageName: com.example;

  import java.io.Serializable;

  java.io.Serializable -> Serializable
  """)
  public void processClassName01() {
    String packageName;
    packageName = "com.example";

    CodeImportList list;
    list = new CodeImportList(packageName);

    InternalClassName type;
    type = InternalClassName.of(Serializable.class);

    assertEquals(list.process(type), "Serializable");
    assertEquals(importList(list), List.of(
      "java.io.Serializable"));
  }

  @Test(description = """
  packageName: com.example;

  com.example.Foo -> Foo
  """)
  public void processClassName02() {
    String packageName;
    packageName = "com.example";

    CodeImportList list;
    list = new CodeImportList(packageName);

    InternalClassName type;
    type = InternalClassName.of(packageName, "Foo");

    assertEquals(list.process(type), "Foo");
    assertEquals(importList(list), List.of());
  }

  @Test(description = """
  packageName: com.example;

  java.lang.Thread -> Thread
  """)
  public void processClassName03() {
    String packageName;
    packageName = "com.example";

    CodeImportList list;
    list = new CodeImportList(packageName);

    InternalClassName type;
    type = InternalClassName.of(Thread.class);

    assertEquals(list.process(type), "Thread");
    assertEquals(importList(list), List.of());
  }

  @Test(description = """
  packageName: com.example;

  import java.util.List;

  com.example.List -> com.example.List
  java.util.List -> List
  """)
  public void processClassName04() {
    String packageName;
    packageName = "com.example";

    CodeImportList list;
    list = new CodeImportList(packageName);

    InternalClassName type1;
    type1 = InternalClassName.of(List.class);

    InternalClassName type2;
    type2 = InternalClassName.of(packageName, "List");

    assertEquals(list.process(type1), "List");
    assertEquals(list.process(type2), "com.example.List");
    assertEquals(importList(list), List.of(
      "java.util.List"));
  }

  private List<String> importList(CodeImportList processor) {
    return List.copyOf(processor.importTypes);
  }

}