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
package objectox.http.media;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiFunction;
import java.util.function.Function;
import objectos.http.Content;
import objectos.http.HeaderName;
import objectos.http.Status;
import objectos.internal.VisibleForTesting;
import objectos.lang.BinaryObject;
import objectos.http.Request;
import objectos.http.Result;

public final class StaticFiles implements BiFunction<Request, Result, Result>, Closeable {

  private final StaticFilesAttributes staticFilesAttributes;

  private final Function<BasicFileAttributes, String> staticFilesETag;

  private final StaticFilesMethod staticFilesMethod;

  private final StaticFilesResponses staticFilesResponses;

  private final StaticFilesRoot staticFilesRoot;

  StaticFiles(
      StaticFilesAttributes staticFilesAttributes,

      Function<BasicFileAttributes, String> staticFilesETag,

      StaticFilesMethod staticFilesMethod,

      StaticFilesResponses staticFilesResponses,

      StaticFilesRoot staticFilesRoot
  ) {
    this.staticFilesAttributes = staticFilesAttributes;

    this.staticFilesETag = staticFilesETag;

    this.staticFilesMethod = staticFilesMethod;

    this.staticFilesResponses = staticFilesResponses;

    this.staticFilesRoot = staticFilesRoot;
  }

  @Override
  public final Result apply(Request request, Result initial) {
    return switch (initial) {
      case Request r -> handle(r, null);

      case StaticFileContent(Content c) -> handle(request, c);

      default -> initial;
    };
  }

  @Override
  public final void close() throws IOException {
    staticFilesRoot.close();
  }

  private Result handle(Request request, BinaryObject contents) {
    final String path;
    path = request.path();

    final Path file;
    file = staticFilesRoot.resolve(path);

    if (file == null) {
      return Status.BAD_REQUEST;
    }

    final BasicFileAttributes attributes;
    attributes = staticFilesAttributes.readOrCreate(file, contents);

    if (attributes == null) {
      return request;
    }

    if (!staticFilesMethod.validate(request)) {
      return staticFilesResponses.methodNotAllowed();
    }

    final String etag;
    etag = staticFilesETag.apply(attributes);

    final String ifNoneMatch;
    ifNoneMatch = request.header(HeaderName.IF_NONE_MATCH);

    if (etag.equals(ifNoneMatch)) {
      return staticFilesResponses.notModified(etag);
    }

    else {
      return staticFilesResponses.ok(file, etag);
    }
  }

  @VisibleForTesting
  public final Path resolve(String path) {
    return staticFilesRoot.resolve(path);
  }

}
