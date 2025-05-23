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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;
import java.util.Objects;

final class WebResourcesConfig implements Web.Resources.Config {

  private sealed interface ResourceFile {

    Path file();

  }

  private record BinaryFile(Path file, byte[] contents) implements ResourceFile {}

  private record TextFile(Path file, CharSequence contents, Charset charset) implements ResourceFile {}

  private final Note.Ref1<Path> created = Note.Ref1.create(Web.Resources.Config.class, "ADD", Note.DEBUG);

  Map<String, String> contentTypes = Map.of();

  final String defaultContentType = "application/octet-stream";

  final List<Path> directories = Util.createList();

  final List<ResourceFile> files = Util.createList();

  Note.Sink noteSink = new Note.NoOpSink();

  private final Path rootDirectory;

  WebResourcesConfig() throws IOException {
    rootDirectory = Files.createTempDirectory("way-web-resources-").normalize();
  }

  public WebResourcesKernel build() throws IOException {
    for (Path directory : directories) {
      CopyRecursively copyRecursively;
      copyRecursively = new CopyRecursively(rootDirectory, directory);

      Files.walkFileTree(directory, copyRecursively);
    }

    final OpenOption[] writeOptions;
    writeOptions = new OpenOption[] {StandardOpenOption.CREATE_NEW};

    for (ResourceFile f : files) {
      final Path file;
      file = f.file();

      final Path parent;
      parent = file.getParent();

      Files.createDirectories(parent);

      switch (f) {
        case BinaryFile binary -> Files.write(file, binary.contents, writeOptions);

        case TextFile text -> Files.writeString(file, text.contents, text.charset, writeOptions);
      }
    }

    return new WebResourcesKernel(
        contentTypes,

        defaultContentType,

        WebResources.Notes.create(),

        noteSink,

        rootDirectory
    );
  }

  @Override
  public final void addDirectory(Path directory) {
    Check.argument(Files.isDirectory(directory), "Path " + directory + " does not represent a directory");

    directories.add(directory);
  }

  @Override
  public final void addBinaryFile(String pathName, byte[] contents) {
    final Path path;
    path = toPath(pathName);

    final byte[] copy;
    copy = contents.clone(); // implicit null-check

    final ResourceFile file;
    file = new BinaryFile(path, copy);

    files.add(file);
  }

  @Override
  public final void addTextFile(String pathName, CharSequence contents, Charset charset) {
    final Path path;
    path = toPath(pathName);

    Objects.requireNonNull(contents, "contents == null");
    Objects.requireNonNull(charset, "charset == null");

    ResourceFile file;
    file = new TextFile(path, contents, charset);

    files.add(file);
  }

  private Path toPath(String pathName) {
    final int length;
    length = pathName.length();

    if (length < 1 || pathName.charAt(0) != '/') {
      throw new IllegalArgumentException("pathName must start with the '/' character");
    }

    final String relative;
    relative = pathName.substring(1);

    Path file;
    file = rootDirectory.resolve(relative);

    file = file.normalize();

    if (!file.startsWith(rootDirectory)) {
      throw new IllegalArgumentException("Traversal detected: " + pathName);
    }

    return file;
  }

  @Override
  public final void contentTypes(String propertiesString) {
    Map<String, String> map;
    map = Util.parsePropertiesMap(propertiesString);

    contentTypes = map;
  }

  @Override
  public final void noteSink(Note.Sink noteSink) {
    this.noteSink = Check.notNull(noteSink, "noteSink == null");
  }

  private class CopyRecursively extends SimpleFileVisitor<Path> {

    private final Path rootDirectory;

    private final Path source;

    public CopyRecursively(Path rootDirectory, Path source) {
      this.rootDirectory = rootDirectory;

      this.source = source;
    }

    @Override
    public final FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
      Path path;
      path = source.relativize(dir);

      Path dest;
      dest = rootDirectory.resolve(path);

      try {
        Files.copy(dir, dest);
      } catch (FileAlreadyExistsException e) {
        if (!Files.isDirectory(dest)) {
          throw e;
        }
      }

      return FileVisitResult.CONTINUE;
    }

    @Override
    public final FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
      Path path;
      path = source.relativize(file);

      Path dest;
      dest = rootDirectory.resolve(path);

      Files.copy(file, dest, StandardCopyOption.COPY_ATTRIBUTES);

      noteSink.send(created, dest);

      return FileVisitResult.CONTINUE;
    }

  }

}