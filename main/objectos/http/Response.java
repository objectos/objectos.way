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

import java.util.function.Consumer;
import objectos.dev.Testable;
import objectox.http.resp.ResponsePojo;

/// An HTTP response message.
public sealed interface Response
    extends
    Result,
    RoutingOption,
    Testable
    permits ResponsePojo {

  /// Creates a new response with the specified options.
  ///
  /// @param opts allows for setting the options
  ///
  /// @return a newly created response message
  static Response create(Consumer<? super ResponseOptions> opts) {
    return ResponsePojo.create0(opts);
  }

  /// Returns a new response instance with the specified status and content. In
  /// addition to the specified arguments, the returned response contains the
  /// `Date` header. In other words, the following invocation:
  ///
  /// ```java
  /// Content content = ...
  /// Response.of(Status.NOT_FOUND, content);
  /// ```
  ///
  /// is equivalent to the following:
  ///
  /// ```java
  /// Content content = ...
  /// Response.create(opts -> {
  ///   opts.status(Status.NOT_FOUND);
  ///   opts.date();
  ///   opts.send(content);
  /// });
  /// ```
  ///
  /// @param status the response status
  /// @param content the content object to send as part of the response
  ///
  /// @return a newly created response object
  static Response of(Status status, Content content) {
    return create(opts -> {
      opts.status(status);

      opts.date();

      opts.send(content);
    });
  }

  /// Returns a new response instance with the specified status and content
  /// provider. In addition to the specified arguments, the returned response
  /// contains the `Date` header. In other words, the following invocation:
  ///
  /// ```java
  /// ContentProvider provider = ...
  /// Response.of(Status.NOT_FOUND, provider);
  /// ```
  ///
  /// is equivalent to the following:
  ///
  /// ```java
  /// ContentProvider provider = ...
  /// Response.create(opts -> {
  ///   opts.status(Status.NOT_FOUND);
  ///   opts.date();
  ///   opts.send(provider);
  /// });
  /// ```
  ///
  /// @param status the response status
  /// @param provider the content provider
  ///
  /// @return a newly created response object
  static Response of(Status status, ContentProvider provider) {
    return create(opts -> {
      opts.status(status);

      opts.date();

      opts.send(provider);
    });
  }

}
