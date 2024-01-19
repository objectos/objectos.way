/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.web;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.lang.TestingNoteSink;
import objectos.way.Rmdir;
import org.testng.annotations.Test;

public class WebResourcesTest {

  @Test(enabled = false, description = """
  It should copy all of the files from the specified directory
  """)
  public void testCase01() throws IOException {
    Path directory;
    directory = Files.createTempDirectory("way-test-");

    try (WebResources.Bootstrapper resources = WebResources.create()) {
      resources.noteSink(TestingNoteSink.INSTANCE);

      Path a;
      a = Path.of("a.txt");

      Path b;
      b = Path.of("dir1", "b.txt");

      Path c;
      c = Path.of("dir2", "c.txt");

      assertNull(resources.resolve(a));
      assertNull(resources.resolve(b));
      assertNull(resources.resolve(c));

      write(directory, a, "AAAA");
      write(directory, b, "BBBB");
      write(directory, b, "CCCC");

      resources.copyDirectory(directory);

      assertEquals(Files.readString(resources.resolve(a)), "AAAA");
      assertEquals(Files.readString(resources.resolve(b)), "BBBB");
      assertEquals(Files.readString(resources.resolve(c)), "CCCC");
    } finally {
      Rmdir.rmdir(directory);
    }
  }

  private void write(Path directory, Path file, String text) throws IOException {
    Path target;
    target = directory.resolve(file);

    Path parent;
    parent = target.getParent();

    Files.createDirectories(parent);

    Files.writeString(target, text);
  }

}