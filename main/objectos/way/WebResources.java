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
package objectos.way;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import objectos.way.Http.Exchange;

final class WebResources implements AutoCloseable, Web.Resources {

  static final Note.Ref1<String> TRAVERSAL = Note.Ref1.create(Web.Resources.class, "Traversal detected", Note.ERROR);

  private static final OpenOption[] OPEN_CREATE = new OpenOption[] {StandardOpenOption.CREATE_NEW};

  private final Map<String, String> contentTypes;

  private final String defaultContentType;

  private final Note.Sink noteSink;

  private final Path rootDirectory;

  private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

  private final Lock readLock = rwl.readLock();

  private final Lock writeLock = rwl.writeLock();

  WebResources(WebResourcesConfig builder) {
    this.contentTypes = builder.contentTypes;

    this.defaultContentType = builder.defaultContentType;

    this.noteSink = builder.noteSink;

    this.rootDirectory = builder.rootDirectory;
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

  @Override
  public final boolean deleteIfExists(String path) throws IOException {
    Check.notNull(path, "path == null");

    Path file;
    file = resolve(path);

    checkTraversal(path, file);

    writeLock.lock();

    try {
      return Files.deleteIfExists(file);
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public final void handle(Http.Exchange http) {
    String pathName;
    pathName = http.path();

    Path file;
    file = resolve(pathName);

    if (!file.startsWith(rootDirectory)) {
      noteSink.send(TRAVERSAL, pathName);

      return;
    }

    readLock.lock();

    try {
      handle(http, file);
    } finally {
      readLock.unlock();
    }
  }

  private void handle(Exchange http, Path file) {
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

    byte method;
    method = http.method();

    if (method != Http.GET && method != Http.HEAD) {
      http.methodNotAllowed();

      return;
    }

    String etag;
    etag = etag(attributes);

    Http.Request.Headers headers;
    headers = http.headers();

    String ifNoneMatch;
    ifNoneMatch = headers.first(Http.IF_NONE_MATCH);

    if (etag.equals(ifNoneMatch)) {
      http.status(Http.Status.NOT_MODIFIED);

      http.dateNow();

      http.header(Http.ETAG, etag);

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

    http.header(Http.CONTENT_TYPE, contentType);

    http.header(Http.CONTENT_LENGTH, attributes.size());

    http.dateNow();

    http.header(Http.ETAG, etag);

    if (method == Http.GET) {
      http.send(file);
    } else {
      http.send();
    }
  }

  @Override
  public final void writeCharWritable(String path, Lang.CharWritable contents, Charset charset) throws IOException {
    Check.notNull(path, "path == null");
    Check.notNull(contents, "contents == null");
    Check.notNull(charset, "charset == null");

    Path file;
    file = resolve(path);

    checkTraversal(path, file);

    writeLock.lock();

    try (BufferedWriter writer = Files.newBufferedWriter(file, charset, OPEN_CREATE)) {
      contents.writeTo(writer);
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public final void writeString(String path, CharSequence contents, Charset charset) throws IOException {
    Check.notNull(path, "path == null");
    Check.notNull(contents, "contents == null");
    Check.notNull(charset, "charset == null");

    Path file;
    file = resolve(path);

    checkTraversal(path, file);

    writeLock.lock();

    try {
      Files.writeString(file, contents, OPEN_CREATE);
    } finally {
      writeLock.unlock();
    }
  }

  private void checkTraversal(String path, Path file) throws IOException {
    if (!file.startsWith(rootDirectory)) {
      throw new IOException("Traversal detected: " + path);
    }
  }

  @Override
  public final void close() throws IOException {
    Io.deleteRecursively(rootDirectory);
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