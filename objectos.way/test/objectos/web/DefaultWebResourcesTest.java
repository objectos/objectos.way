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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.lang.TestingNoteSink;
import objectos.way.Rmdir;
import org.testng.annotations.Test;

public class DefaultWebResourcesTest {

  @Test(description = """
  It should copy all of the files from the specified directory
  """)
  public void testCase01() throws IOException {
    Path directory;
    directory = Files.createTempDirectory("way-test-");

    Path src;
    src = directory.resolve("src");

    Files.createDirectories(src);

    Path target;
    target = directory.resolve("target");

    Files.createDirectories(target);

    try (DefaultWebResources resources = new DefaultWebResources(target)) {
      resources.noteSink(TestingNoteSink.INSTANCE);

      Path a;
      a = Path.of("a.txt");

      Path b;
      b = Path.of("dir1", "b.txt");

      Path c;
      c = Path.of("dir2", "c.txt");

      assertNull(resources.regularFile(a));
      assertNull(resources.regularFile(b));
      assertNull(resources.regularFile(c));

      write(src, a, "AAAA");
      write(src, b, "BBBB");
      write(src, c, "CCCC");

      resources.copyDirectory(src);

      assertEquals(Files.readString(resources.regularFile(a)), "AAAA");
      assertEquals(Files.readString(resources.regularFile(b)), "BBBB");
      assertEquals(Files.readString(resources.regularFile(c)), "CCCC");
    } finally {
      assertFalse(Files.exists(target));

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