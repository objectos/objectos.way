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

import java.nio.file.Path;
import objectos.http.HttpHeaderName;
import objectos.http.HttpStatus;
import objectos.http.Response;

final class StaticFilesResponses {

  private static final Response METHOD_NOT_ALLOWED = Response.create(opts -> {
    opts.status(HttpStatus.METHOD_NOT_ALLOWED);

    opts.date();

    opts.header(HttpHeaderName.CONTENT_LENGTH, 0);

    opts.header(HttpHeaderName.ALLOW, "GET, HEAD");
  });

  private final StaticFilesExtension staticFilesExtension;

  private final StaticFilesTypes staticFilesTypes;

  StaticFilesResponses(StaticFilesExtension staticFilesExtension, StaticFilesTypes staticFilesTypes) {
    this.staticFilesExtension = staticFilesExtension;

    this.staticFilesTypes = staticFilesTypes;
  }

  public final Response methodNotAllowed() {
    return METHOD_NOT_ALLOWED;
  }

  public final Response notModified(String etag) {
    return Response.create(opts -> {
      opts.status(HttpStatus.NOT_MODIFIED);

      opts.date();

      opts.header(HttpHeaderName.ETAG, etag);

      opts.header(HttpHeaderName.CONTENT_LENGTH, 0);
    });
  }

  public final Response ok(Path file, String etag) {
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
