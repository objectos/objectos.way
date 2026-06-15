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

import objectos.http.Content;
import objectos.http.ContentProvider;
import objectos.http.HttpHeaderName;
import objectos.http.HttpStatus;
import objectos.http.MediaType;
import objectos.http.Redirection;
import objectos.http.Request;
import objectos.http.Response;
import objectos.http.Result;
import objectox.http.RedirectionPojo;
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

      case HttpStatus s -> processStatus(s);
    };
  }

  protected Response processContent(Content content) {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.status(HttpStatus.OK);

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

    builder.header(HttpHeaderName.CONTENT_LENGTH, "0");

    builder.header(HttpHeaderName.LOCATION, pojo.location());

    return builder.build();
  }

  protected Response processRequest(Request request) {
    return processStatus(HttpStatus.NOT_FOUND);
  }

  protected Response processStatus(HttpStatus status) {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.status(status);

    builder.date();

    final String reasonPhrase;
    reasonPhrase = status.reasonPhrase();

    final Content content;
    content = Content.of(MediaType.TEXT_PLAIN, reasonPhrase + "\n");

    builder.send(content);

    return builder.build();
  }

}
