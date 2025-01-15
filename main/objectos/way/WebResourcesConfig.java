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

    String pathName();

  }

  private record BinaryFile(String pathName, byte[] contents) implements ResourceFile {}

  private record TextFile(String pathName, CharSequence contents, Charset charset) implements ResourceFile {}

  private final Note.Ref1<Path> created = Note.Ref1.create(Web.Resources.Config.class, "File created", Note.DEBUG);

  Map<String, String> contentTypes = Map.of();

  final String defaultContentType = "application/octet-stream";

  final List<Path> directories = Util.createList();

  final List<ResourceFile> files = Util.createList();

  Note.Sink noteSink = new Note.NoOpSink();

  Path rootDirectory;

  public Web.Resources build() throws IOException {
    if (rootDirectory == null) {
      rootDirectory = Files.createTempDirectory("way-web-resources-");
    }

    for (Path directory : directories) {
      CopyRecursively copyRecursively;
      copyRecursively = new CopyRecursively(directory);

      Files.walkFileTree(directory, copyRecursively);
    }

    final OpenOption[] writeOptions;
    writeOptions = new OpenOption[] {StandardOpenOption.CREATE_NEW};

    for (ResourceFile f : files) {
      String path;
      path = f.pathName();

      // remove '/'
      path = path.substring(1);

      Path file;
      file = rootDirectory.resolve(path);

      file = file.normalize();

      if (!file.startsWith(rootDirectory)) {
        throw new IllegalArgumentException("Traversal detected: " + path);
      }

      Path parent;
      parent = file.getParent();

      Files.createDirectories(parent);

      switch (f) {
        case BinaryFile(String pathName, byte[] contents)
             -> Files.write(file, contents, writeOptions);

        case TextFile(String pathName, CharSequence contents, Charset charset)
             -> Files.writeString(file, contents, charset, writeOptions);
      }
    }

    return new WebResources(this);
  }

  @Override
  public final void addDirectory(Path directory) {
    Check.argument(Files.isDirectory(directory), "Path " + directory + " does not represent a directory");

    directories.add(directory);
  }

  @Override
  public final void addTextFile(String pathName, CharSequence contents, Charset charset) {
    Http.RequestTarget target;
    target = HttpExchange.parseRequestTarget(pathName);

    String query;
    query = target.rawQuery();

    if (query != null) {
      throw new IllegalArgumentException("Found query component in path name: " + pathName);
    }

    String path;
    path = target.path();

    Objects.requireNonNull(contents, "contents == null");
    Objects.requireNonNull(charset, "charset == null");

    ResourceFile file;
    file = new TextFile(path, contents, charset);

    files.add(file);
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

  @Override
  public final void rootDirectory(Path directory) {
    Check.argument(Files.isDirectory(directory), "Path " + directory + " does not represent a directory");

    rootDirectory = directory;
  }

  private class CopyRecursively extends SimpleFileVisitor<Path> {

    private final Path source;

    public CopyRecursively(Path source) {
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