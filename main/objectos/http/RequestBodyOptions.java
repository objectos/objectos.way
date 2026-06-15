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

import objectox.http.req.RequestBodyOptionsBuilder;

/// Configures the request body options of a `Server` instance.
public sealed interface RequestBodyOptions permits RequestBodyOptionsBuilder {

  /// Sets the maximum request body size in bytes that will be stored in memory.
  /// A request body larger than this limit is serialized to a local file.
  ///
  /// @param value the maximum size (in bytes) for a memory request body
  void memoryMax(int value);

  /// Sets the maximum allowed size in bytes for the request body.
  ///
  /// If the server determines that the request body exceeds the limit, the
  /// request processing ends, the server responds with a `413 Content Too Large`
  /// message, and the server closes the connection.
  ///
  /// @param value the maximum size (in bytes) of an allowed request body
  void sizeMax(long value);

}
