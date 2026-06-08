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
package objectos.http;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import objectos.y.PathY;
import org.testng.annotations.Test;

public class StaticFilesBuilderRootTest {

  @Test
  public void create01() throws IOException {
    final Set<Path> directories;
    directories = Set.of();

    final StaticFilesBuilderRoot subject;
    subject = new StaticFilesBuilderRoot(directories);

    final Path res;
    res = subject.create();

    assertEquals(
        ls(res),

        """

        """
    );
  }

  @Test
  public void create02() throws IOException {
    final Path fileAtRoot;
    fileAtRoot = PathY.nextDir();

    createFile(fileAtRoot, "file01.txt", "file 01");

    final Path fileAtSub;
    fileAtSub = PathY.nextDir();

    createFile(fileAtSub, "sub/file02.txt", "file 02");

    final Set<Path> directories;
    directories = Set.of(fileAtRoot, fileAtSub);

    final StaticFilesBuilderRoot subject;
    subject = new StaticFilesBuilderRoot(directories);

    final Path res;
    res = subject.create();

    assertEquals(
        ls(res),

        """

        file01.txt
        sub
        sub/file02.txt
        """
    );

    assertEquals(Files.readString(res.resolve("file01.txt")), "file 01");
    assertEquals(Files.readString(res.resolve("sub/file02.txt")), "file 02");
  }

  private void createFile(Path directory, String other, String contents) throws IOException {
    final Path file;
    file = directory.resolve(other);

    final Path parent;
    parent = file.getParent();

    Files.createDirectories(parent);

    Files.writeString(file, contents, StandardCharsets.UTF_8);
  }

  private String ls(Path start) {
    try (Stream<Path> walk = Files.walk(start)) {
      return walk.map(p -> start.relativize(p)).sorted().map(Object::toString).collect(Collectors.joining("\n", "", "\n"));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

}
