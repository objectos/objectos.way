/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

import objectox.http.StandardMethod;

public sealed interface Method permits StandardMethod {

  /**
   * The CONNECT method.
   */
  Method CONNECT = StandardMethod.CONNECT;

  /**
   * The DELETE method.
   */
  Method DELETE = StandardMethod.DELETE;

  /**
   * The GET method.
   */
  Method GET = StandardMethod.GET;

  /**
   * The HEAD method.
   */
  Method HEAD = StandardMethod.HEAD;

  /**
   * The OPTIONS method.
   */
  Method OPTIONS = StandardMethod.OPTIONS;

  /**
   * The PATCH method.
   */
  Method PATCH = StandardMethod.PATCH;

  /**
   * The POST method.
   */
  Method POST = StandardMethod.POST;

  /**
   * The PUT method.
   */
  Method PUT = StandardMethod.PUT;

  /**
   * The TRACE method.
   */
  Method TRACE = StandardMethod.TRACE;

}