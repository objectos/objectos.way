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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.Test;

public class AppReloaderTest {

  private final Http.TestingExchange http = Http.TestingExchange.create(config -> {});

  @Test
  public void testCase01() throws Exception {
    try (AppReloaderHelper helper = AppReloaderHelper.of()) {
      helper.writeModuleInfo();

      helper.writeSubject("return \"A\";");

      assertTrue(helper.compile());

      try (
          App.Reloader reloader = App.Reloader.create(config -> {
            config.handlerFactory(helper);
            config.module("test.way", helper.classOutput());
            config.noteSink(TestingNoteSink.INSTANCE);
          })
      ) {
        assertEquals(helper.get(), "A");

        helper.writeSubject("return \"B\";");

        assertTrue(helper.compile());

        TimeUnit.MILLISECONDS.sleep(5);

        reloader.handle(http);

        assertEquals(helper.get(), "B");
      }
    }
  }

  @Test
  public void testCase02() throws Exception {
    try (AppReloaderHelper helper = AppReloaderHelper.of()) {
      helper.writeModuleInfo();

      helper.writeSubject("return Delegate.SUBJECT;");

      helper.writeDelegate("A");

      assertTrue(helper.compile());

      try (
          App.Reloader reloader = App.Reloader.create(config -> {
            config.handlerFactory(helper);
            config.module("test.way", helper.classOutput());
            config.noteSink(TestingNoteSink.INSTANCE);
          })
      ) {
        assertEquals(helper.get(), "A");

        helper.writeDelegate("B");

        assertTrue(helper.compile());

        TimeUnit.MILLISECONDS.sleep(5);

        reloader.handle(http);

        assertEquals(helper.get(), "A");
      }
    }
  }

  @Test(description = """
  Previously watched directory is deleted
  """)
  public void testCase03() throws Exception {
    try (AppReloaderHelper helper = AppReloaderHelper.of()) {
      helper.writeModuleInfo();

      helper.writeSubject("return \"A\";");

      assertTrue(helper.compile());

      FileSystem fileSystem;
      fileSystem = FileSystems.getDefault();

      try (
          WatchService watchService = fileSystem.newWatchService();

          App.Reloader reloader = App.Reloader.create(config -> {
            config.handlerFactory(helper);
            config.module("test.way", helper.classOutput());
            config.noteSink(TestingNoteSink.INSTANCE);
            config.watchService(watchService);
          })
      ) {

        assertEquals(helper.get(), "A");

        Path packageDir;
        packageDir = helper.classOutput().resolve("test");

        TestingDir.deleteRecursively(packageDir);

        helper.writeSubject("return \"B\";");

        assertTrue(helper.compile());

        TimeUnit.MILLISECONDS.sleep(5);

        reloader.handle(http);

        assertEquals(helper.get(), "B");
      }
    }
  }

  @Test(description = """
  Trigger reload by change in another directory
  """)
  public void testCase04() throws Exception {
    try (AppReloaderHelper helper = AppReloaderHelper.of()) {
      helper.writeModuleInfo();

      Path resources;
      resources = helper.resources();

      Path txt;
      txt = resources.resolve("test.txt");

      Files.writeString(txt, "A");

      helper.writeSubject("""
      try {
        return java.nio.file.Files.readString(java.nio.file.Path.of(\"%s\"));
      } catch (java.io.IOException e) {
        return "error";
      }
      """.formatted(txt));

      assertTrue(helper.compile());

      try (
          App.Reloader reloader = App.Reloader.create(config -> {
            config.directory(resources);
            config.handlerFactory(helper);
            config.module("test.way", helper.classOutput());
            config.noteSink(TestingNoteSink.INSTANCE);
          })
      ) {

        assertEquals(helper.get(), "A");

        Files.writeString(txt, "B");

        TimeUnit.MILLISECONDS.sleep(5);

        reloader.handle(http);

        assertEquals(helper.get(), "B");
      }
    }
  }

}