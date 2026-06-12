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

import objectox.http.RequestMethodEnum;

/// The method of an HTTP request message.
public sealed interface RequestMethod
    extends
    HttpRoutes.Option,
    RoutingOption
    permits
    RequestMethodEnum {

  /// The DELETE method.
  RequestMethod DELETE = RequestMethodEnum.DELETE;

  /// The GET method.
  RequestMethod GET = RequestMethodEnum.GET;

  /// The HEAD method.
  RequestMethod HEAD = RequestMethodEnum.HEAD;

  /// The PATCH method.
  RequestMethod PATCH = RequestMethodEnum.PATCH;

  /// The POST method.
  RequestMethod POST = RequestMethodEnum.POST;

  /// The PUT method.
  RequestMethod PUT = RequestMethodEnum.PUT;

  /// Returns the method name, such as `GET` or `PATCH`.
  ///
  /// @return the method name
  String name();

}
