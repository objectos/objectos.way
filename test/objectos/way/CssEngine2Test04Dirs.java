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

import java.nio.file.Path;
import java.util.Set;
import org.testng.annotations.Test;

public class CssEngine2Test04Dirs {

  @Test
  public void scanDirectory01() {
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

      project.writeJavaFile(
          Path.of("test", "Subject.java"),

          """
          package test;

          @objectos.way.Css.Source
          public class Subject extends objectos.way.Html.Template {
            @Override
            protected final void render() {
              div(className("margin:0"));
            }
          }
          """
      );

      assertTrue(project.compile());

      test(project.classOutput(), "module-info", "test/Subject");
    }
  }

  @Test
  public void scanDirectory02() {
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

      project.writeJavaFile(
          Path.of("test", "Subject.java"),

          """
          package test;

          @objectos.way.Css.Source
          public class Subject extends objectos.way.Html.Template {
            @Override
            protected final void render() {
              div(className("margin:0"));
            }
          }
          """
      );

      project.writeJavaFile(
          Path.of("test", "Ignore.java"),

          """
          package test;

          public class Ignore extends objectos.way.Html.Template {
            @Override
            protected final void render() {
              div(className("display:block"));
            }
          }
          """
      );

      assertTrue(project.compile());

      test(project.classOutput(), "module-info", "test/Subject", "test/Ignore");
    }
  }

  private void test(Path directory, String... expected) {
    final CssEngine2ClassFiles tester;
    tester = new CssEngine2ClassFiles();

    final Set<Path> dirs;
    dirs = Set.of(directory);

    final Note.Sink noteSink;
    noteSink = Y.noteSink();

    final CssEngine2.Dirs scanner;
    scanner = new CssEngine2.Dirs(tester, dirs, noteSink);

    scanner.scan();

    assertEquals(tester.scan, Set.of());

    assertEquals(tester.scanIfAnnotated, Set.of(expected));
  }

}