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
import java.nio.file.Path;
import java.util.Set;
import org.testng.annotations.Test;

public class CssEngineTest04ScanDirectories {

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

      test(project.classOutput(), "margin:0");
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

      test(project.classOutput(), "margin:0");
    }
  }

  private void test(Path directory, String... expected) {
    try {
      CssEngine engine;
      engine = CssEngine.create(config -> {
        config.noteSink(Y.noteSink());

        config.scanDirectory(directory);
      });

      engine.state(CssEngine.$SCAN);

      while (engine.shouldExecute(CssEngine.$SCAN_JAR)) {
        engine.executeOne();
      }

      assertEquals(engine.tokens(), Set.of(expected));
    } catch (IOException e) {
      throw new AssertionError("Should not have thrown", e);
    }
  }

}