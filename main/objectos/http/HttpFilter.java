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

/// Executes tasks around the invocation of an HTTP handler.
@FunctionalInterface
public interface HttpFilter {

  /// Allows for executing tasks before and after the handling of the HTTP
  /// exchange.
  ///
  /// @param http the HTTP exchange
  /// @param handler the HTTP handler
  void filter(HttpExchange http, HttpHandler handler);

}