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

import objectox.http.req.RequestBuilder;

/// Configures the creation of a stand-alone request instance.
public sealed interface RequestOptions permits RequestBuilder {

  /// Adds the specified header field to this request.
  ///
  /// @param name the header field name
  /// @param value the header field value
  void header(HeaderName name, String value);

  /// Sets the request method to the specified value. Defaults to the `GET`
  /// method when not specified.
  ///
  /// @param value the HTTP method
  void method(RequestMethod value);

  /// Sets the request-target path component to the specified value. Defaults
  /// to `/` when not specified.
  ///
  /// @param value the decoded path value
  void path(String value);

  /// Associates a session to this request and store the provided key-value
  /// pair in the former.
  ///
  /// @param <T> the type of the attribute
  /// @param key the class object providing the attribute name
  /// @param value the value to be stored
  ///
  /// @return the previously associated value or `null`
  <T> T sessionAttr(Class<T> key, T value);

}