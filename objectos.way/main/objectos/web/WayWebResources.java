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
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import objectos.http.HeaderName;
import objectos.http.Method;
import objectos.http.Status;
import objectos.http.server.ServerExchange;
import objectos.http.server.UriPath;
import objectos.lang.object.Check;
import objectos.notes.NoOpNoteSink;
import objectos.notes.Note1;
import objectos.notes.NoteSink;
import objectos.util.map.GrowableMap;

public final class WayWebResources implements AutoCloseable, WebResources {

  public static final Note1<Path> CREATED;

  private static final Note1<Path> ABSOLUTE;

  private static final Note1<Path> TRAVERSAL;

  static {
    Class<?> source;
    source = WayWebResources.class;

    CREATED = Note1.info(source, "File created");

    ABSOLUTE = Note1.error(source, "Absolute!");

    TRAVERSAL = Note1.error(source, "Traversal!");
  }

  private final Map<String, String> contentTypes = new GrowableMap<>();

  private final String defaultContentType = "application/octet-stream";

  private NoteSink noteSink = NoOpNoteSink.of();

  private final Path root;

  public WayWebResources(Path root) {
    Check.notNull(root, "target == null");
    checkDirectory(root);

    this.root = root;
  }

  private static void checkDirectory(Path dir) {
    if (!Files.isDirectory(dir)) {
      throw new IllegalArgumentException("""
          Invalid directory '%s'

          - the path does not exist
          - the path exists but it is not a directory
          - the path may exist but it is not accessible
          """.formatted(dir));
    }
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
    file = root.resolve(relativePath);

    file = file.normalize();

    if (!file.startsWith(root)) {
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
  public final Path regularFile(Path path) {
    Check.notNull(path, "path == null");

    if (path.isAbsolute()) {
      noteSink.send(ABSOLUTE, path);

      return null;
    }

    Path file;
    file = root.resolve(path);

    file = file.normalize();

    if (!file.startsWith(root)) {
      noteSink.send(TRAVERSAL, path);

      return null;
    }

    if (!Files.isRegularFile(file)) {
      return null;
    }

    return file;
  }

  @Override
  public final void close() throws IOException {
  }

}