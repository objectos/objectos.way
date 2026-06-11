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
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Function;
import objectos.way.Note;

final class StaticFilesHandler implements Handler {

  static final Note.Ref2<String, String> THROW = Note.Ref2.create(StaticFilesHandler.class, "THR", Note.ERROR);

  private final Note.Sink noteSink;

  private final StaticFilesAttributes staticFilesAttributes;

  private final Function<BasicFileAttributes, String> staticFilesETag;

  private final StaticFilesMethod staticFilesMethod;

  private final StaticFilesResponses staticFilesResponses;

  private final StaticFilesRoot staticFilesRoot;

  StaticFilesHandler(
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
  public final Result handle(Request request) {
    try {
      final Path file;
      file = staticFilesRoot.resolve(request);

      final BasicFileAttributes attributes;
      attributes = staticFilesAttributes.read(file);

      staticFilesMethod.validate(request);

      final String etag;
      etag = staticFilesETag.apply(attributes);

      final String ifNoneMatch;
      ifNoneMatch = request.header(HttpHeaderName.IF_NONE_MATCH);

      if (etag.equals(ifNoneMatch)) {
        return staticFilesResponses.notModified(etag);
      }

      else {
        return staticFilesResponses.ok(file, etag);
      }
    } catch (StaticFilesErrTraversal e) {
      noteError(e);

      return HttpStatus.BAD_REQUEST;
    } catch (StaticFilesErrNonRegular e) {
      noteError(e);

      return request;
    } catch (StaticFilesErrMethod e) {
      noteError(e);

      return staticFilesResponses.methodNotAllowed();
    } catch (IOException e) {
      //return InternalServerError.of(e);
      throw new UnsupportedOperationException("Implement me", e);
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
