/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

import java.lang.reflect.Constructor;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AppReloaderTest {

  @Test
  public void testCase01() throws Exception {
    try (AppReloaderHelper helper = AppReloaderHelper.of()) {
      // first subject version
      Path subjectSrc;
      subjectSrc = Path.of("test", "Subject.java");

      helper.writeJavaFile(
          subjectSrc,

          """
          package test;

          public class Subject implements java.util.function.Supplier<String> {
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
            config.binaryName("test.Subject");
            config.watchService(watchService);
            config.noteSink(TestingNoteSink.INSTANCE);
            config.directory(helper.classOutput());
          })) {
        String firstGet;
        firstGet = newInstanceAndGet(reloader);

        assertEquals(firstGet, "A");

        // second subject version
        helper.writeJavaFile(
            subjectSrc,

            """
            package test;

            public class Subject implements java.util.function.Supplier<String> {
              public String get() {
                return "B";
              }
            }
            """
        );

        assertTrue(helper.compile());

        TimeUnit.MILLISECONDS.sleep(5);

        String secondGet;
        secondGet = newInstanceAndGet(reloader);

        assertEquals(secondGet, "B");
      }
    }
  }

  @Test
  public void testCase02() throws Exception {
    try (AppReloaderHelper helper = AppReloaderHelper.of()) {
      // first subject version
      Path subjectSrc;
      subjectSrc = Path.of("test", "Subject.java");

      helper.writeJavaFile(
          Path.of("objectos", "way", "App.java"),

          """
          package objectos.way;

          import java.lang.annotation.ElementType;
          import java.lang.annotation.Retention;
          import java.lang.annotation.RetentionPolicy;
          import java.lang.annotation.Target;

          public final class App {
            @Retention(RetentionPolicy.CLASS)
            @Target(ElementType.TYPE)
            public @interface DoNotReload {}
          }
          """
      );

      helper.writeJavaFile(
          subjectSrc,

          """
          package test;

          @objectos.way.App.DoNotReload
          public class Subject implements java.util.function.Supplier<String> {
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
            config.binaryName("test.Subject");
            config.watchService(watchService);
            config.noteSink(TestingNoteSink.INSTANCE);
            config.directory(helper.classOutput());
          })) {

        try {
          newInstanceAndGet(reloader);

          Assert.fail();
        } catch (ClassNotFoundException expected) {

        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private String newInstanceAndGet(App.Reloader reloader) throws Exception {
    Class<?> type;
    type = reloader.get();

    Constructor<?> constructor;
    constructor = type.getConstructor();

    Object instance;
    instance = constructor.newInstance();

    Supplier<String> supplier;
    supplier = (Supplier<String>) instance;

    return supplier.get();
  }

}