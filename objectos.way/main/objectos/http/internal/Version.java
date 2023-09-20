/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.http.internal;

public enum Version {

  HTTP_1_0("HTTP/1.0"),

  HTTP_1_1("HTTP/1.1");

  final byte[] responseBytes;

  private Version(String signature) {
    String response;
    response = signature + " ";

    responseBytes = Bytes.utf8(response);
  }

}
