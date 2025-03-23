/*
 * Copyright (C) 2025 Objectos Software LTDA.
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

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchService;
import org.testng.annotations.Test;

public class AppReloaderTest {

  @Test
  public void testCase01() throws Exception {
    try (AppReloaderHelper helper = AppReloaderHelper.of()) {
      // module-info
      helper.writeJavaFile(
          Path.of("module-info.java"),

          """
          module test.way {
            exports test;

            requires objectos.way;
          }
          """
      );

      // first subject version
      Path subjectSrc;
      subjectSrc = Path.of("test", "Subject.java");

      helper.writeJavaFile(
          subjectSrc,

          """
          package test;

          public class Subject implements objectos.way.Http.Handler {
            public void handle(objectos.way.Http.Exchange http) {}
            public String get() {
              return "A";
            }
          }
          """
      );

      assertTrue(helper.compile());

      FileSystem fileSystem;
      fileSystem = FileSystems.getDefault();

      try (WatchService watchService = fileSystem.newWatchService();
          App.Reloader reloader = App.Reloader.create(config -> {
            config.directory(helper.classOutput());
            config.handlerFactory(helper);
            config.moduleName("test.way");
            config.noteSink(TestingNoteSink.INSTANCE);
            config.watchService(watchService);
          })) {
        assertEquals(helper.get(), "A");

        // second subject version
        helper.writeJavaFile(
            subjectSrc,

            """
            package test;

            public class Subject implements objectos.way.Http.Handler {
              public void handle(objectos.way.Http.Exchange http) {}
              public String get() {
                return "B";
              }
            }
            """
        );

        assertTrue(helper.compileAndWait());

        assertEquals(helper.get(), "B");
      }
    }
  }

  @Test
  public void testCase02() throws Exception {
    try (AppReloaderHelper helper = AppReloaderHelper.of()) {
      // module-info
      helper.writeJavaFile(
          Path.of("module-info.java"),

          """
          module test.way {
            exports test;

            requires objectos.way;
          }
          """
      );

      // first subject version
      Path subjectSrc;
      subjectSrc = Path.of("test", "Subject.java");

      helper.writeJavaFile(
          subjectSrc,

          """
          package test;

          public class Subject implements objectos.way.Http.Handler {
            public void handle(objectos.way.Http.Exchange http) {}
            public String get() {
              return Delegate.SUBJECT;
            }
          }
          """
      );

      Path delegateSrc;
      delegateSrc = Path.of("test", "Delegate.java");

      helper.writeJavaFile(
          delegateSrc,

          """
          package test;

          @objectos.way.App.DoNotReload
          class Delegate {
            static final String SUBJECT = "A";
          }
          """
      );

      assertTrue(helper.compile());

      FileSystem fileSystem;
      fileSystem = FileSystems.getDefault();

      try (WatchService watchService = fileSystem.newWatchService();
          App.Reloader reloader = App.Reloader.create(config -> {
            config.directory(helper.classOutput());
            config.handlerFactory(helper);
            config.moduleName("test.way");
            config.noteSink(TestingNoteSink.INSTANCE);
            config.watchService(watchService);
          })) {
        assertEquals(helper.get(), "A");

        // second subject version
        helper.writeJavaFile(
            delegateSrc,

            """
            package test;

            @objectos.way.App.DoNotReload
            class Delegate {
              static final String SUBJECT = "B";
            }
            """
        );

        assertTrue(helper.compileAndWait());

        assertEquals(helper.get(), "A");
      }
    }
  }

  @Test(description = """
  Previously watched directory is deleted
  """)
  public void testCase03() throws Exception {
    try (AppReloaderHelper helper = AppReloaderHelper.of()) {
      // module-info
      helper.writeJavaFile(
          Path.of("module-info.java"),

          """
          module test.way {
            exports test;

            requires objectos.way;
          }
          """
      );

      // first subject version
      Path subjectSrc;
      subjectSrc = Path.of("test", "Subject.java");

      helper.writeJavaFile(
          subjectSrc,

          """
          package test;

          public class Subject implements objectos.way.Http.Handler {
            public void handle(objectos.way.Http.Exchange http) {}
            public String get() {
              return "A";
            }
          }
          """
      );

      assertTrue(helper.compile());

      FileSystem fileSystem;
      fileSystem = FileSystems.getDefault();

      try (WatchService watchService = fileSystem.newWatchService();
          App.Reloader reloader = App.Reloader.create(config -> {
            config.directory(helper.classOutput());
            config.handlerFactory(helper);
            config.moduleName("test.way");
            config.noteSink(TestingNoteSink.INSTANCE);
            config.watchService(watchService);
          })) {

        assertEquals(helper.get(), "A");

        Path packageDir;
        packageDir = helper.classOutput().resolve("test");

        TestingDir.deleteRecursively(packageDir);

        // second subject version
        helper.writeJavaFile(
            subjectSrc,

            """
            package test;

            public class Subject implements objectos.way.Http.Handler {
              public void handle(objectos.way.Http.Exchange http) {}
              public String get() {
                return "B";
              }
            }
            """
        );

        assertTrue(helper.compileAndWait());

        assertEquals(helper.get(), "B");
      }
    }
  }

}