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
package objectox.http.host;

import java.io.PrintWriter;
import java.io.StringWriter;
import objectos.http.Content;
import objectos.http.ContentProvider;
import objectos.http.HeaderName;
import objectos.http.Status;
import objectos.http.MediaType;
import objectos.http.Redirection;
import objectos.http.Request;
import objectos.http.Response;
import objectos.http.Result;
import objectox.http.RedirectionPojo;
import objectox.http.StatusThrowable;
import objectox.http.media.StaticFileContent;
import objectox.http.resp.ResponseBuilder;

/// Given a `Result` instance, produces the response message to be sent to
/// the client.
class ResultProcessor {

  /// Returns the response message for the given result instance.
  /// Implementations must never return `null`.
  ///
  /// @param result the result instance
  ///
  /// @return the response message, never `null`
  public final Response process(Result result) {
    return switch (result) {
      case Content content -> processContent(content);

      case ContentProvider provider -> {
        final Content content;
        content = provider.toContent();

        if (content == null) {
          throw new IllegalArgumentException("%s provided a null `Content` instance".formatted(provider));
        }

        yield processContent(content);
      }

      case Redirection r -> processRedirect(r);

      case Request r -> processRequest(r);

      case Response r -> r;

      case StaticFileContent(Content content) -> processContent(content);

      case Status s -> processStatus(s);

      case StatusThrowable(Status status, Throwable cause) -> processStatus(status, cause);
    };
  }

  protected Response processContent(Content content) {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.status(Status.OK);

    builder.date();

    builder.send(content);

    return builder.build();
  }

  protected Response processRedirect(Redirection redirect) {
    final RedirectionPojo pojo;
    pojo = (RedirectionPojo) redirect;

    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.status(pojo.status());

    builder.date();

    builder.header(HeaderName.CONTENT_LENGTH, "0");

    builder.header(HeaderName.LOCATION, pojo.location());

    return builder.build();
  }

  protected Response processRequest(Request request) {
    return processStatus(Status.NOT_FOUND);
  }

  protected Response processStatus(Status status) {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.status(status);

    builder.date();

    final int code;
    code = status.code();

    final String reasonPhrase;
    reasonPhrase = status.reasonPhrase();

    final String msg;
    msg = code + " " + reasonPhrase + "\n";

    final Content content;
    content = Content.of(MediaType.TEXT_PLAIN, msg);

    builder.send(content);

    return builder.build();
  }

  protected Response processStatus(Status status, Throwable cause) {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.status(status);

    builder.date();

    final int code;
    code = status.code();

    final String reasonPhrase;
    reasonPhrase = status.reasonPhrase();

    final StringWriter writer;
    writer = new StringWriter();

    final PrintWriter pw;
    pw = new PrintWriter(writer);

    cause.printStackTrace(pw);

    final String msg;
    msg = code + " " + reasonPhrase + "\n" + writer.toString();

    final Content content;
    content = Content.of(MediaType.TEXT_PLAIN, msg);

    builder.send(content);

    return builder.build();
  }

}
