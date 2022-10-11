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
package br.com.objectos.tools;

import static br.com.objectos.tools.Tools.compilationUnit;
import static br.com.objectos.tools.Tools.javac;
import static br.com.objectos.tools.Tools.patchModuleWithTestClasses;
import static br.com.objectos.tools.Tools.processor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class ToolsTest {

  @Test
  public void itShouldCompileJavaSourceFilesAndGenerateClassFile() {
    Compilation compilation;
    compilation = javac(
      compilationUnit(
        "package testing.tools;",
        "class Test {}"
      )
    );

    assertTrue(compilation.wasSuccessful());
    assertTrue(compilation.containsClassFile("testing.tools.Test"));
  }

  @Test
  public void itShouldGenerateFilesFromStandaloneProcessors() {
    Compilation compilation;
    compilation = javac(
      processor(new GenerateInterfaceProcessor()),

      patchModuleWithTestClasses("br.com.objectos.tools"),

      compilationUnit(
        "package testing.tools;",
        "@br.com.objectos.tools.GenerateInterface",
        "class Test {}"
      )
    );

    System.out.println(compilation.getMessage());

    assertTrue(compilation.wasSuccessful());
    assertHasLines(
      compilation.getJavaFile("testing.tools.TestInterface").contents(),
      "package testing.tools;",
      "public interface TestInterface {}"
    );
  }

  private void assertHasLines(String contents, String... expected) {
    String[] split;
    split = contents.split(System.lineSeparator());

    assertEquals(split, expected);
  }

}