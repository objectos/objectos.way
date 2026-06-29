/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.dev;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import objectos.way.Y;
import objectos.y.PathY;
import org.testng.annotations.Test;

public class ReloadingFsTest {

  private ReloadingFs of(Path... directories) throws IOException {
    final Set<Path> set;
    set = Set.of(directories);

    return ReloadingFs.of(Y.noteSink(), set);
  }

  @Test(description = "empty -> false")
  public void testCase01() throws IOException {
    try (var fs = of()) {
      assertFalse(fs.changed());
    }
  }

  @Test(description = "no change -> false")
  public void testCase02() throws IOException {
    final Path dir;
    dir = PathY.nextDir();

    try (var fs = of(dir)) {
      assertFalse(fs.changed());
    }
  }

  @Test(description = "file created -> true")
  public void testCase03() throws IOException, InterruptedException {
    final Path dir;
    dir = PathY.nextDir();

    try (var fs = of(dir)) {
      Files.writeString(dir.resolve("1.txt"), "1");

      TimeUnit.MILLISECONDS.sleep(5);

      assertTrue(fs.changed());
    }
  }

  @Test(description = "dir created -> true")
  public void testCase04() throws IOException, InterruptedException {
    final Path dir;
    dir = PathY.nextDir();

    try (var fs = of(dir)) {
      Files.createDirectory(dir.resolve("1"));

      TimeUnit.MILLISECONDS.sleep(5);

      assertTrue(fs.changed());
    }
  }

  @Test(description = "file modified -> true")
  public void testCase05() throws IOException, InterruptedException {
    final Path dir;
    dir = PathY.nextDir();

    final Path file;
    file = dir.resolve("1.txt");

    Files.writeString(file, "1");

    try (var fs = of(dir)) {
      Files.writeString(file, "2");

      TimeUnit.MILLISECONDS.sleep(5);

      assertTrue(fs.changed());
    }
  }

  @Test(description = "file deleted -> false (for now)")
  public void testCase06() throws IOException, InterruptedException {
    final Path dir;
    dir = PathY.nextDir();

    final Path file;
    file = dir.resolve("1.txt");

    Files.writeString(file, "1");

    try (var fs = of(dir)) {
      Files.delete(file);

      TimeUnit.MILLISECONDS.sleep(5);

      assertFalse(fs.changed());
    }
  }

}
