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

final class StaticFilesHandler implements Handler {

  private static final Response METHOD_NOT_ALLOWED = Response.create(opts -> {
    opts.status(HttpStatus.METHOD_NOT_ALLOWED);

    opts.date();

    opts.header(HttpHeaderName.CONTENT_LENGTH, 0);

    opts.header(HttpHeaderName.ALLOW, "GET, HEAD");
  });

  private final Map<String, String> contentTypes;

  private final String defaultContentType;

  private final Path rootDirectory;

  StaticFilesHandler(Map<String, String> contentTypes, String defaultContentType, Path rootDirectory) {
    this.contentTypes = contentTypes;

    this.defaultContentType = defaultContentType;

    this.rootDirectory = rootDirectory;
  }

  @Override
  public final Result handle(Request request) {
    final String pathName;
    pathName = request.path();

    final String relative;
    relative = pathName.substring(1);

    final Path resolved;
    resolved = rootDirectory.resolve(relative);

    final Path file;
    file = resolved.normalize();

    if (!file.startsWith(rootDirectory)) {
      return HttpStatus.BAD_REQUEST;
    }

    final BasicFileAttributes attributes;

    try {
      attributes = Files.readAttributes(file, BasicFileAttributes.class);
    } catch (NoSuchFileException e) {
      return request;
    } catch (IOException e) {
      //return InternalServerError.of(e);
      throw new UnsupportedOperationException("Implement me");
    }

    if (!attributes.isRegularFile()) {
      return request;
    }

    final HttpMethod method;
    method = request.method();

    if (method != HttpMethod.GET && method != HttpMethod.HEAD) {
      return METHOD_NOT_ALLOWED;
    }

    final String etag;
    etag = etag(attributes);

    final String ifNoneMatch;
    ifNoneMatch = request.header(HttpHeaderName.IF_NONE_MATCH);

    if (etag.equals(ifNoneMatch)) {
      return Response.create(opts -> {
        opts.status(HttpStatus.NOT_MODIFIED);

        opts.date();

        opts.header(HttpHeaderName.CONTENT_LENGTH, 0);

        opts.header(HttpHeaderName.ETAG, etag);
      });
    }

    final String extension;
    extension = extension(file);

    final String contentType;

    if (!extension.isEmpty()) {
      contentType = contentTypes.getOrDefault(extension, defaultContentType);
    } else {
      contentType = defaultContentType;
    }

    return Response.create(opts -> {
      opts.status(HttpStatus.OK);

      opts.date();

      opts.header(HttpHeaderName.ETAG, etag);

      opts.header(HttpHeaderName.CONTENT_TYPE, contentType);

      opts.send(file);
    });
  }

  private String etag(BasicFileAttributes attributes) {
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

}
