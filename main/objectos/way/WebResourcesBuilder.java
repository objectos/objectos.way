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
package objectos.way;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.io.Writer;
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
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

final class WebResourcesBuilder implements Web.Resources.Options {

  private final Note.Ref1<Path> created = Note.Ref1.create(Web.Resources.Options.class, "ADD", Note.DEBUG);

  private final Note.Ref2<String, String> contentTypeRegistered = Note.Ref2.create(Web.Resources.Options.class, "CTR", Note.DEBUG);

  private final Note.Ref3<String, String, String> contentTypeIgnored = Note.Ref3.create(Web.Resources.Options.class, "CTI", Note.DEBUG);

  Map<String, String> contentTypes = Util.createMap();

  final String defaultContentType = "application/octet-stream";

  Note.Sink noteSink = new Note.NoOpSink();

  private final Path rootDirectory;

  private final OpenOption[] writeOptions = new OpenOption[] {StandardOpenOption.CREATE_NEW};

  WebResourcesBuilder() throws IOException {
    rootDirectory = Files.createTempDirectory("way-web-resources-").normalize();
  }

  static String extension(Path file) {
    final Path fileName;
    fileName = file.getFileName();

    final String actualFileName;
    actualFileName = fileName.toString();

    final int lastDotIndex;
    lastDotIndex = actualFileName.lastIndexOf('.');

    final String extension;

    if (lastDotIndex >= 0) {
      extension = actualFileName.substring(lastDotIndex);
    } else {
      extension = "";
    }

    return extension;
  }

  public WebResourcesKernel build() throws IOException {
    return new WebResourcesKernel(
        contentTypes(),

        defaultContentType,

        WebResources.Notes.create(),

        noteSink,

        rootDirectory
    );
  }

  private ConcurrentMap<String, String> contentTypes() {
    return new ConcurrentHashMap<>(contentTypes);
  }

  @Override
  public final void addDirectory(Path directory) {
    try {
      Check.argument(Files.isDirectory(directory), "Path " + directory + " does not represent a directory");

      final CopyRecursively copyRecursively;
      copyRecursively = new CopyRecursively(rootDirectory, directory);

      Files.walkFileTree(directory, copyRecursively);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public final void addFile(String pathName, InputStream in) {
    try {
      final Path path;
      path = toPath(pathName);

      ensureParent(path);

      Files.copy(in, path);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @Override
  public final void addMedia(String pathName, Media media) {
    final Path path;
    path = toPath(pathName);

    final String contentType;
    contentType = media.contentType();

    if (contentType == null) {
      throw new IllegalArgumentException("Provided media returned a null content type");
    }

    final String extension;
    extension = extension(path);

    if (!extension.isEmpty()) {
      register(extension, contentType);
    }

    try {
      ensureParent(path);

      switch (media) {
        case Media.Bytes b -> {
          Files.write(path, b.toByteArray(), writeOptions);
        }

        case Media.Text text -> {
          final Charset charset;
          charset = text.charset();

          try (Writer w = Files.newBufferedWriter(path, charset, writeOptions)) {
            text.writeTo(w);
          }
        }

        case Media.Stream stream -> {
          try (OutputStream out = Files.newOutputStream(path, writeOptions)) {
            stream.writeTo(out);
          }
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private void ensureParent(Path path) throws IOException {
    final Path parent;
    parent = path.getParent();

    Files.createDirectories(parent);
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
    final Map<String, String> map;
    map = Util.parsePropertiesMap(propertiesString);

    for (Map.Entry<String, String> entry : map.entrySet()) {
      final String extension;
      extension = entry.getKey();

      final String contentType;
      contentType = entry.getValue();

      register(extension, contentType);
    }
  }

  @Override
  public final void include(Web.Resources.Library value) {
    value.configure(this);
  }

  @Override
  public final void noteSink(Note.Sink value) {
    noteSink = Objects.requireNonNull(value, "value == null");
  }

  private void register(String extension, String contentType) {
    if (extension.isEmpty()) {
      throw new IllegalArgumentException("File extension must not be empty");
    }

    if (contentType.isEmpty()) {
      throw new IllegalArgumentException("Content type must not be empty");
    }

    final String previous;
    previous = contentTypes.putIfAbsent(extension, contentType);

    if (previous == null) {
      noteSink.send(contentTypeRegistered, extension, contentType);
    }

    else if (!previous.equals(contentType)) {
      noteSink.send(contentTypeIgnored, extension, previous, contentType);
    }
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