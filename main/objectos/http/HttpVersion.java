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

/**
 * The version of the HTTP protocol.
 */
public enum HttpVersion {

  /**
   * The {@code HTTP/0.9} version.
   */
  HTTP_0_9("HTTP/0.9"),

  /**
   * The {@code HTTP/1.0} version.
   */
  HTTP_1_0("HTTP/1.0"),

  /**
   * The {@code HTTP/1.1} version.
   */
  HTTP_1_1("HTTP/1.1");

  final byte[] responseBytes;

  private HttpVersion(String signature) {
    String response;
    response = signature + " ";

    responseBytes = Http.utf8(response);
  }

  final void appendTo(StringBuilder out) {
    switch (this) {
      case HTTP_0_9 -> out.append("HTTP/0.9");

      case HTTP_1_0 -> out.append("HTTP/1.0");

      case HTTP_1_1 -> out.append("HTTP/1.1");
    }
  }

}