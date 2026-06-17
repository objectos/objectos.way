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

import objectox.http.handler.PathParamNamed;
import objectox.http.handler.PathParamPredicates;

/// Allows for matching an individual route if a path parameter value conforms
/// to a restriction.
public sealed interface PathParam
    extends RoutingOption
    permits PathParamNamed {

  /// Returns a new object that matches a path parameter with the specified name
  /// only if its value contains only US-ASCII digits.
  ///
  /// @param name the path parameter name
  ///
  /// @return a newly created `PathParam` instance
  static PathParam digits(String name) {
    return PathParamPredicates.DIGITS.forName(name);
  }

}
