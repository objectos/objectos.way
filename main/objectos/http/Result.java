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

/// The outcome of processing a `Request` instance by a `Handler`.
public sealed interface Result permits Request, Response, HttpStatus {

  default Result or(Handler another) {
    return switch (this) {
      case Request request -> another.handle(request);

      default -> this;
    };
  }

  default Result or(Result another) {
    return switch (this) {
      case Request _ -> another;

      default -> this;
    };
  }

}
