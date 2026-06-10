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

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

final class StaticFiles implements Closeable, Handler, BiFunction<Request, Result, Result> {

  private final StaticFilesAttributes staticFilesAttributes;

  private final Function<BasicFileAttributes, String> staticFilesETag;

  private final StaticFilesExtension staticFilesExtension;

  private final StaticFilesRoot staticFilesRoot;

  private final StaticFilesTypes staticFilesTypes;

  StaticFiles(
      StaticFilesAttributes staticFilesAttributes,

      Function<BasicFileAttributes, String> staticFilesETag,

      StaticFilesExtension staticFilesExtension,

      StaticFilesRoot staticFilesRoot,

      StaticFilesTypes staticFilesTypes
  ) {
    this.staticFilesAttributes = staticFilesAttributes;

    this.staticFilesETag = staticFilesETag;

    this.staticFilesExtension = staticFilesExtension;

    this.staticFilesRoot = staticFilesRoot;

    this.staticFilesTypes = staticFilesTypes;
  }

  public static StaticFiles create(Consumer<? super StaticFilesBuilder> opts) throws IOException {
    final StaticFilesBuilder builder;
    builder = new StaticFilesBuilder();

    opts.accept(builder);

    return builder.build();
  }

  @Override
  public final Result apply(Request request, Result initial) {
    return switch (initial) {
      case Request r -> handle(r);

      case StaticFileContent(Content c) -> process(request, c);

      default -> initial;
    };
  }

  private Result process(Request request, Content content) {
    try {
      return process0(request, content);
    } catch (IOException e) {
      //return InternalServerError.of(e);
      throw new UnsupportedOperationException("Implement me", e);
    }
  }

  private Result process0(Request request, Content content) throws IOException {
    final Path file;
    file = staticFilesRoot.resolve(request);

    if (file == null) {
      return HttpStatus.BAD_REQUEST;
    }

    final HttpMethod method;
    method = request.method();

    if (method != HttpMethod.GET && method != HttpMethod.HEAD) {
      return methodNotAllowed();
    }

    try (OutputStream out = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
      content.binaryTo(out);
    }

    final BasicFileAttributes attributes;
    attributes = staticFilesAttributes.delegate(file);

    final String etag;
    etag = staticFilesETag.apply(attributes);

    return ok(file, etag);
  }

  @Override
  public final void close() throws IOException {
    staticFilesRoot.delete();
  }

  @Override
  public final Result handle(Request request) {
    try {
      return handle0(request);
    } catch (IOException e) {
      //return InternalServerError.of(e);
      throw new UnsupportedOperationException("Implement me", e);
    }
  }

  private Result handle0(Request request) throws IOException {
    final Path file;
    file = staticFilesRoot.resolve(request);

    if (file == null) {
      return HttpStatus.BAD_REQUEST;
    }

    final BasicFileAttributes attributes;
    attributes = staticFilesAttributes.read(file);

    if (attributes == null) {
      return request;
    }

    final HttpMethod method;
    method = request.method();

    if (method != HttpMethod.GET && method != HttpMethod.HEAD) {
      return methodNotAllowed();
    }

    final String etag;
    etag = staticFilesETag.apply(attributes);

    final String ifNoneMatch;
    ifNoneMatch = request.header(HttpHeaderName.IF_NONE_MATCH);

    if (etag.equals(ifNoneMatch)) {
      return notModified(etag);
    }

    else {
      return ok(file, etag);
    }
  }

  private static final Response METHOD_NOT_ALLOWED = Response.create(opts -> {
    opts.status(HttpStatus.METHOD_NOT_ALLOWED);

    opts.date();

    opts.header(HttpHeaderName.CONTENT_LENGTH, 0);

    opts.header(HttpHeaderName.ALLOW, "GET, HEAD");
  });

  private Response methodNotAllowed() {
    return METHOD_NOT_ALLOWED;
  }

  private Response notModified(String etag) {
    return Response.create(opts -> {
      opts.status(HttpStatus.NOT_MODIFIED);

      opts.date();

      opts.header(HttpHeaderName.ETAG, etag);

      opts.header(HttpHeaderName.CONTENT_LENGTH, 0);
    });
  }

  private Response ok(Path file, String etag) {
    return Response.create(opts -> {
      opts.status(HttpStatus.OK);

      opts.date();

      opts.header(HttpHeaderName.ETAG, etag);

      final String extension;
      extension = staticFilesExtension.get(file);

      final String contentType;
      contentType = staticFilesTypes.get(extension);

      opts.header(HttpHeaderName.CONTENT_TYPE, contentType);

      opts.send(file);
    });
  }

}
