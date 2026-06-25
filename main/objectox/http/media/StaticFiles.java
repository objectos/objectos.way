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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiFunction;
import java.util.function.Function;
import objectos.http.Content;
import objectos.http.HeaderName;
import objectos.http.Status;
import objectos.http.Request;
import objectos.http.Result;
import objectos.way.Note;

public final class StaticFiles implements BiFunction<Request, Result, Result> {

  static final Note.Ref2<String, String> THROW = Note.Ref2.create(StaticFiles.class, "THR", Note.ERROR);

  private final Note.Sink noteSink;

  private final StaticFilesAttributes staticFilesAttributes;

  private final Function<BasicFileAttributes, String> staticFilesETag;

  private final StaticFilesMethod staticFilesMethod;

  private final StaticFilesResponses staticFilesResponses;

  private final StaticFilesRoot staticFilesRoot;

  StaticFiles(
      Note.Sink noteSink,

      StaticFilesAttributes staticFilesAttributes,

      Function<BasicFileAttributes, String> staticFilesETag,

      StaticFilesMethod staticFilesMethod,

      StaticFilesResponses staticFilesResponses,

      StaticFilesRoot staticFilesRoot
  ) {
    this.noteSink = noteSink;

    this.staticFilesAttributes = staticFilesAttributes;

    this.staticFilesETag = staticFilesETag;

    this.staticFilesMethod = staticFilesMethod;

    this.staticFilesResponses = staticFilesResponses;

    this.staticFilesRoot = staticFilesRoot;
  }

  @Override
  public final Result apply(Request request, Result initial) {
    return switch (initial) {
      case Request r -> handle(r);

      case StaticFileContent(Content c) -> staticFileContent(request, c);

      default -> initial;
    };
  }

  public final Result handle(Request request) {
    try {
      return handle0(request);
    } catch (StaticFilesErrNonRegular e) {
      noteError(e);

      return request;
    }
  }

  private Result handle0(Request request) throws StaticFilesErrNonRegular {
    try {
      staticFilesMethod.validate(request);

      final Path file;
      file = staticFilesRoot.resolve(request);

      final BasicFileAttributes attributes;
      attributes = staticFilesAttributes.read(file);

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
    } catch (StaticFilesErrMethod e) {
      noteError(e);

      return staticFilesResponses.methodNotAllowed();
    } catch (StaticFilesErrTraversal e) {
      noteError(e);

      return Status.BAD_REQUEST;
    }
  }

  private Result staticFileContent(Request request, Content c) {
    try {
      return handle0(request);
    } catch (StaticFilesErrNonRegular e) {
      final Throwable cause;
      cause = e.getCause();

      if (cause instanceof NoSuchFileException) {
        final Path file;
        file = e.path;

        return staticFileWrite(file, c);
      } else {
        noteError(e);

        return request;
      }
    }
  }

  private static final CopyOption[] COPY = {StandardCopyOption.ATOMIC_MOVE};

  private static final OpenOption[] OPEN = {StandardOpenOption.WRITE};

  private Result staticFileWrite(Path file, Content content) {
    Path tmp = null;

    try {
      tmp = Files.createTempFile("objectos-http-static-file-", ".tmp");

      try (OutputStream out = Files.newOutputStream(tmp, OPEN)) {
        content.binaryTo(out);
      }

      Files.move(tmp, file, COPY);

      final BasicFileAttributes attributes;
      attributes = staticFilesAttributes.readDirect(file);

      final String etag;
      etag = staticFilesETag.apply(attributes);

      return staticFilesResponses.ok(file, etag);
    } catch (IOException e) {
      noteError(e);

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

  private void noteError(Throwable e) {
    final Class<? extends Throwable> type;
    type = e.getClass();

    final String name;
    name = type.getName();

    final String message;
    message = e.getMessage();

    noteSink.send(THROW, name, message);
  }

}
