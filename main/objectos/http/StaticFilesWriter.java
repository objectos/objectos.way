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
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Function;

final class StaticFilesWriter {

  private static final CopyOption[] COPY = {StandardCopyOption.ATOMIC_MOVE};

  private static final OpenOption[] OPEN = {StandardOpenOption.WRITE};

  private final StaticFilesAttributes staticFilesAttributes;

  private final Function<BasicFileAttributes, String> staticFilesETag;

  private final StaticFilesMethod staticFilesMethod;

  private final StaticFilesResponses staticFilesResponses;

  private final StaticFilesRoot staticFilesRoot;

  StaticFilesWriter(
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

  public final Result apply(Request request, Content content) {
    Path tmp = null;

    try {
      final Path file;
      file = staticFilesRoot.resolve(request);

      staticFilesMethod.validate(request);

      tmp = Files.createTempFile(null, null);

      try (OutputStream out = Files.newOutputStream(tmp, OPEN)) {
        content.binaryTo(out);
      }

      Files.move(tmp, file, COPY);

      final BasicFileAttributes attributes;
      attributes = staticFilesAttributes.delegate(file);

      final String etag;
      etag = staticFilesETag.apply(attributes);

      return staticFilesResponses.ok(file, etag);
    } catch (StaticFilesErrTraversal e) {

      return HttpStatus.BAD_REQUEST;
    } catch (StaticFilesErrMethod e) {

      return staticFilesResponses.methodNotAllowed();
    } catch (IOException e) {

      //return InternalServerError.of(e);
      throw new UnsupportedOperationException("Implement me", e);
    } finally {
      if (tmp != null) {
        try {
          Files.deleteIfExists(tmp);
        } catch (IOException suppressed) {

        }
      }
    }
  }

}
