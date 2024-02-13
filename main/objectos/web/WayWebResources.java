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

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import objectos.http.HeaderName;
import objectos.http.Method;
import objectos.http.ServerExchange;
import objectos.http.Status;
import objectos.http.UriPath;
import objectos.io.FileVisitors;
import objectos.lang.object.Check;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;
import objectos.util.map.GrowableMap;

public final class WayWebResources implements AutoCloseable, WebResources {

  private final Map<String, String> contentTypes = new GrowableMap<>();

  private final String defaultContentType = "application/octet-stream";

  private NoteSink noteSink = NoOpNoteSink.of();

  private final Path rootDirectory;

  public WayWebResources() throws IOException {
    rootDirectory = Files.createTempDirectory("way-web-resources-");
  }

  // visible for testing
  WayWebResources(Path root) {
    this.rootDirectory = root;
  }

  public final WayWebResources contentType(String extension, String contentType) {
    Check.notNull(extension, "extension == null");
    Check.notNull(contentType, "contentType == null");

    contentTypes.put(extension, contentType);

    return this;
  }

  public final WayWebResources noteSink(NoteSink noteSink) {
    this.noteSink = Check.notNull(noteSink, "noteSink == null");

    return this;
  }

  public final void copyDirectory(Path directory) throws IOException {
    Check.notNull(directory, "directory == null");

    CopyRecursively copyRecursively;
    copyRecursively = new CopyRecursively(directory);

    Files.walkFileTree(directory, copyRecursively);
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

      noteSink.send(CREATED, dest);

      return FileVisitResult.CONTINUE;
    }

  }

  public final void createNew(Path path, byte[] bytes) throws IOException {
    Check.argument(!path.isAbsolute(), "Path must not be absolute");
    Check.notNull(bytes, "bytes == null");

    Path file;
    file = rootDirectory.resolve(path);

    file = file.normalize();

    if (!file.startsWith(rootDirectory)) {
      throw new IllegalArgumentException("Traversal detected: " + path);
    }

    Path parent;
    parent = file.getParent();

    Files.createDirectories(parent);

    Files.write(file, bytes, StandardOpenOption.CREATE_NEW);
  }

  final void setLastModifiedTime(Path path, FileTime fileTime) throws IOException {
    Check.argument(!path.isAbsolute(), "Path must not be absolute");
    Check.notNull(fileTime, "fileTime == null");

    Path file;
    file = rootDirectory.resolve(path);

    file = file.normalize();

    if (!file.startsWith(rootDirectory)) {
      throw new IllegalArgumentException("Traversal detected: " + path);
    }

    Files.setLastModifiedTime(file, fileTime);
  }

  @Override
  public final void handle(ServerExchange http) {
    UriPath path;
    path = http.path();

    String pathName;
    pathName = path.toString();

    String relative;
    relative = pathName.substring(1);

    Path relativePath;
    relativePath = Path.of(relative);

    Path file;
    file = rootDirectory.resolve(relativePath);

    file = file.normalize();

    if (!file.startsWith(rootDirectory)) {
      noteSink.send(TRAVERSAL, relativePath);

      http.notFound();

      return;
    }

    BasicFileAttributes attributes;

    try {
      attributes = Files.readAttributes(file, BasicFileAttributes.class);
    } catch (NoSuchFileException e) {
      http.notFound();

      return;
    } catch (IOException e) {
      http.internalServerError(e);

      return;
    }

    if (!attributes.isRegularFile()) {
      http.notFound();

      return;
    }

    Method method;
    method = http.method();

    if (method != Method.GET && method != Method.HEAD) {
      http.methodNotAllowed();

      return;
    }

    http.status(Status.OK);

    String contentType;
    contentType = defaultContentType;

    String fileName;
    fileName = file.getFileName().toString();

    int lastDotIndex;
    lastDotIndex = fileName.lastIndexOf('.');

    if (lastDotIndex >= 0) {
      String extension;
      extension = fileName.substring(lastDotIndex);

      contentType = contentTypes.getOrDefault(extension, contentType);
    }

    http.header(HeaderName.CONTENT_TYPE, contentType);

    http.header(HeaderName.CONTENT_LENGTH, attributes.size());

    http.dateNow();

    String etag;
    etag = etag(attributes);

    http.header(HeaderName.ETAG, etag);

    if (method == Method.GET) {
      http.send(file);
    } else {
      http.send();
    }
  }

  private String etag(BasicFileAttributes attributes) {
    FileTime lastModifiedFileTime;
    lastModifiedFileTime = attributes.lastModifiedTime();

    long lastModifiedMillis;
    lastModifiedMillis = lastModifiedFileTime.toMillis();

    long size;
    size = attributes.size();

    return Long.toHexString(lastModifiedMillis) + "-" + Long.toHexString(size);
  }

  @Override
  public final void close() throws IOException {
    FileVisitor<Path> deleteRecursively;
    deleteRecursively = FileVisitors.deleteRecursively();

    Files.walkFileTree(rootDirectory, deleteRecursively);
  }

}