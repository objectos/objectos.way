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

import objectos.http.Http.Version;

/**
 * Provides methods for inspecting the request line of an HTTP request
 * message.
 *
 * <p>
 * Unless otherwise specified the values returned by the methods of this
 * interface are decoded.
 */
public sealed interface HttpRequestLine extends HttpRequestTarget permits HttpRequest {

  /**
   * The code of the method of this request message.
   *
   * @return the code of the method of this request message
   */
  HttpMethod method();

  /**
   * The version of the HTTP protocol of this request message.
   *
   * @return the version of the HTTP protocol of this request message.
   */
  Version version();

}