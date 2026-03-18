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

import java.nio.charset.StandardCharsets;

/**
 * The method of an HTTP request message.
 */
public enum HttpMethod {

  /**
   * The CONNECT method.
   */
  CONNECT(false),

  /**
   * The DELETE method.
   */
  DELETE(true),

  /**
   * The GET method.
   */
  GET(true),

  /**
   * The HEAD method.
   */
  HEAD(true),

  /**
   * The OPTIONS method.
   */
  OPTIONS(false),

  /**
   * The PATCH method.
   */
  PATCH(true),

  /**
   * The POST method.
   */
  POST(true),

  /**
   * The PUT method.
   */
  PUT(true),

  /**
   * The TRACE method.
   */
  TRACE(false);

  static final HttpMethod[] VALUES = values();

  final byte[] ascii = (name() + ' ').getBytes(StandardCharsets.US_ASCII);

  final boolean implemented;

  private HttpMethod(boolean implemented) {
    this.implemented = implemented;
  }

}