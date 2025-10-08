/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CssEngine2Test05Scanner {

  @FunctionalInterface
  interface Method {
    void accept(String name, byte[] bytes);
  }

  @Test(description = "it should scan string literals")
  public void testCase01() {
    test(
        """
        package test;

        @objectos.way.Css.Source
        class Subject {
          String s = "margin:0";
        }
        """,

        s -> s::scanIfAnnotated,

        "margin:0"
    );
  }

  @Test(description = "it should skip non-annotated classes")
  public void testCase02() {
    test(
        """
        package test;

        class Subject {
          String s = "margin:0";
        }
        """,

        s -> s::scanIfAnnotated
    );
  }

  @Test(description = "it should scan non-annotated classes")
  public void testCase03() {
    test(
        """
        package test;

        class Subject {
          String s = "margin:0";
        }
        """,

        s -> s::scan,

        "margin:0"
    );
  }

  private void test(String source, Function<CssEngine2.Scanner, Method> f, String... expected) {
    try (Y.JavaProject project = Y.javaProject()) {
      project.writeJavaFile(
          Path.of("module-info.java"),
          """
          module test.way {
            exports test;
            requires objectos.way;
          }
          """
      );

      project.writeJavaFile(Path.of("test", "Subject.java"), source);

      assertTrue(project.compile());

      final Path classOutput;
      classOutput = project.classOutput();

      final Path classFile;
      classFile = classOutput.resolve(Path.of("test", "Subject.class"));

      final byte[] bytes;
      bytes = Files.readAllBytes(classFile);

      final Note.Sink noteSink;
      noteSink = Y.noteSink();

      final Set<String> strings;
      strings = new HashSet<>();

      final CssEngine2.Scanner scanner;
      scanner = new CssEngine2.Scanner(noteSink, strings::add);

      final Method method;
      method = f.apply(scanner);

      method.accept("test/Subject", bytes);

      assertEquals(strings, Set.of(expected));
    } catch (IOException e) {
      Assert.fail("I/O error", e);
    }
  }

}