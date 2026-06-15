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

import objectox.http.HttpStatus0;
import objectox.http.RedirectPojo;

/// A redirection message.
public sealed interface Redirect extends Result permits RedirectPojo {

  /// Returns a new `301 Moved Permanently` redirection with the specified
  /// `Location` header.
  ///
  /// @param location the value of the `Location` header
  ///
  /// @return a newly created redirection message
  static Redirect movedPermanently(String location) {
    return RedirectPojo.of(HttpStatus0.MOVED_PERMANENTLY, location);
  }

  /// Returns a new `302 Found` redirection with the specified `Location`
  /// header.
  ///
  /// @param location the value of the `Location` header
  ///
  /// @return a newly created redirection message
  static Redirect found(String location) {
    return RedirectPojo.of(HttpStatus0.FOUND, location);
  }

  /// Returns a new `303 See Other` redirection with the specified `Location`
  /// header.
  ///
  /// @param location the value of the `Location` header
  ///
  /// @return a newly created redirection message
  static Redirect seeOther(String location) {
    return RedirectPojo.of(HttpStatus0.SEE_OTHER, location);
  }

}
