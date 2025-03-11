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
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import java.util.Objects;

record WebResourcesKernel(
    Map<String, String> contentTypes,
    String defaultContentType,
    WebResources.Notes notes,
    Note.Sink noteSink,
    Path rootDirectory
) {

  public final void close() throws IOException {
    Io.deleteRecursively(rootDirectory);
  }

  public final boolean deleteIfExists(String path) throws IOException {
    Objects.requireNonNull(path, "path == null");

    final Path file;
    file = resolve(path);

    checkTraversal(path, file);

    return Files.deleteIfExists(file);
  }

  public final void handle(Http.Exchange http) {
    final String pathName;
    pathName = http.path();

    final Path file;
    file = resolve(pathName);

    if (!file.startsWith(rootDirectory)) {
      noteSink.send(notes.traversal(), pathName);

      return;
    }

    handle(http, file);
  }

  private void handle(Http.Exchange http, Path file) {
    BasicFileAttributes attributes;

    try {
      attributes = Files.readAttributes(file, BasicFileAttributes.class);
    } catch (NoSuchFileException e) {
      return;
    } catch (IOException e) {
      http.internalServerError(e);

      return;
    }

    if (!attributes.isRegularFile()) {
      return;
    }

    Http.Method method;
    method = http.method();

    if (method != Http.Method.GET && method != Http.Method.HEAD) {
      http.methodNotAllowed();

      return;
    }

    String etag;
    etag = etag(attributes);

    String ifNoneMatch;
    ifNoneMatch = http.header(Http.HeaderName.IF_NONE_MATCH);

    if (etag.equals(ifNoneMatch)) {
      http.status(Http.Status.NOT_MODIFIED);

      http.dateNow();

      http.header(Http.HeaderName.ETAG, etag);

      http.send();

      return;
    }

    http.status(Http.Status.OK);

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

    http.header(Http.HeaderName.CONTENT_TYPE, contentType);

    http.header(Http.HeaderName.CONTENT_LENGTH, attributes.size());

    http.dateNow();

    http.header(Http.HeaderName.ETAG, etag);

    if (method == Http.Method.GET) {
      http.send(file);
    } else {
      http.send();
    }
  }

  public final void write(String pathName, byte[] contents) throws IOException {
    final Path file;
    file = toPath(pathName);

    final Path tmp;
    tmp = Files.createTempFile(null, null);

    Files.write(tmp, contents);

    move(tmp, file);
  }

  public final void writeMediaObject(String pathName, Lang.MediaObject contents) throws IOException {
    final Path file;
    file = toPath(pathName);

    final Path tmp;
    tmp = Files.createTempFile(null, null);

    final byte[] bytes;
    bytes = contents.mediaBytes();

    Files.write(tmp, bytes);

    move(tmp, file);
  }

  public final void writeString(String pathName, CharSequence contents, Charset charset) throws IOException {
    final Path file;
    file = toPath(pathName);

    Objects.requireNonNull(contents, "contents == null");
    Objects.requireNonNull(charset, "charset == null");

    final Path tmp;
    tmp = Files.createTempFile(null, null);

    Files.writeString(tmp, contents, charset);

    move(tmp, file);
  }

  private Path toPath(String pathName) throws IOException {
    Objects.requireNonNull(pathName, "pathName == null");

    Http.RequestTarget target;
    target = HttpExchange.parseRequestTarget(pathName);

    String query;
    query = target.rawQuery();

    if (query != null) {
      throw new IllegalArgumentException("Found query component in path name: " + pathName);
    }

    final String path;
    path = target.path();

    final Path file;
    file = resolve(path);

    checkTraversal(path, file);

    checkExists(path, file);

    return file;
  }

  private void checkExists(String path, Path file) throws IOException {
    if (Files.exists(file)) {
      throw new FileAlreadyExistsException(path);
    }
  }

  private void checkTraversal(String path, Path file) throws IOException {
    if (!file.startsWith(rootDirectory)) {
      throw new IOException("Traversal detected: " + path);
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

  private void move(Path tmp, Path file) throws IOException {
    final Path parent;
    parent = file.getParent();

    Files.createDirectories(parent);

    Files.move(tmp, file, StandardCopyOption.ATOMIC_MOVE);
  }

  private Path resolve(String pathName) {
    String relative;
    relative = pathName.substring(1);

    Path relativePath;
    relativePath = Path.of(relative);

    Path file;
    file = rootDirectory.resolve(relativePath);

    file = file.normalize();

    return file;
  }

}