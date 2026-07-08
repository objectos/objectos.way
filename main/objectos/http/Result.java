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

import java.util.Objects;
import objectos.dev.Testable;
import objectox.http.StatusThrowable;

/// The outcome of processing a `Request` instance by a `Handler`.
public sealed interface Result
    extends
    Testable
    permits
    Content,
    ContentProvider,
    Redirection,
    Request,
    Response,
    StaticFile,
    Status,
    StatusThrowable {

  static Result error(Status status, Throwable cause) {
    final int code;
    code = status.code();

    if (code < 400) {
      final String msg;
      msg = "Status does not represent a client error nor a server error";

      throw new IllegalArgumentException(msg);
    }

    return new StatusThrowable(
        status,

        Objects.requireNonNull(cause, "cause == null")
    );
  }

}
