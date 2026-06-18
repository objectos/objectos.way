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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Map;
import objectox.http.RequestMethodEnum;

final class HttpHost3StaticFiles implements HttpHandler {

  private final Map<String, String> contentTypes;

  private final String defaultContentType;

  private final Path rootDirectory;

  HttpHost3StaticFiles(Map<String, String> contentTypes, String defaultContentType, Path rootDirectory) {
    this.contentTypes = contentTypes;

    this.defaultContentType = defaultContentType;

    this.rootDirectory = rootDirectory;
  }

  @Override
  public final void handle(HttpExchange http) {
    try {
      final Path file;
      file = resolve(http);

      handle(http, file);
    } catch (HttpTraversalException e) {
      http.error(Status.BAD_REQUEST, e);
    }
  }

  public final Path resolve(HttpExchange http) throws HttpTraversalException {
    final String pathName;
    pathName = http.path();

    final String relative;
    relative = pathName.substring(1);

    final Path resolved;
    resolved = rootDirectory.resolve(relative);

    final Path file;
    file = resolved.normalize();

    if (!file.startsWith(rootDirectory)) {
      throw new HttpTraversalException();
    }

    return file;
  }

  static String etag(BasicFileAttributes attributes) {
    final FileTime lastModifiedFileTime;
    lastModifiedFileTime = attributes.lastModifiedTime();

    final long lastModifiedMillis;
    lastModifiedMillis = lastModifiedFileTime.toMillis();

    final long size;
    size = attributes.size();

    return Long.toHexString(lastModifiedMillis) + "-" + Long.toHexString(size);
  }

  private String extension(Path file) {
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

  private void handle(HttpExchange http, Path file) {
    final BasicFileAttributes attributes;

    try {
      attributes = Files.readAttributes(file, BasicFileAttributes.class);
    } catch (NoSuchFileException e) {
      return;
    } catch (IOException e) {
      http.error(Status.INTERNAL_SERVER_ERROR, e);

      return;
    }

    if (!attributes.isRegularFile()) {
      return;
    }

    final RequestMethod method;
    method = http.method();

    if (method != RequestMethodEnum.GET && method != RequestMethodEnum.HEAD) {
      http.status(Status.METHOD_NOT_ALLOWED);

      http.header(HeaderName.DATE, http.now());

      http.header(HeaderName.CONTENT_LENGTH, 0);

      http.header(HeaderName.ALLOW, "GET, HEAD");

      http.send();

      return;
    }

    String etag;
    etag = etag(attributes);

    String ifNoneMatch;
    ifNoneMatch = http.header(HeaderName.IF_NONE_MATCH);

    if (etag.equals(ifNoneMatch)) {
      http.status(Status.NOT_MODIFIED);

      http.header(HeaderName.DATE, http.now());

      http.header(HeaderName.CONTENT_LENGTH, 0);

      http.header(HeaderName.ETAG, etag);

      http.send();

      return;
    }

    final String extension;
    extension = extension(file);

    final String contentType;

    if (!extension.isEmpty()) {
      contentType = contentTypes.getOrDefault(extension, defaultContentType);
    } else {
      contentType = defaultContentType;
    }

    http.status(Status.OK);

    http.header(HeaderName.CONTENT_TYPE, contentType);

    http.header(HeaderName.CONTENT_LENGTH, attributes.size());

    http.header(HeaderName.DATE, http.now());

    http.header(HeaderName.ETAG, etag);

    http.send(file);
  }

}
