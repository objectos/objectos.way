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
package objectos.http.server;

import java.nio.file.Path;
import objectos.http.HeaderName;

public interface ServerResponse {

  // status

  /*
   * 200 OK
   */
  ServerResponse ok();

  /*
   * 304 NOT MODIFIED
   */
  ServerResponse notModified();

  /*
   * 404 NOT FOUND
   */
  ServerResponse notFound();

  // headers

  ServerResponse contentLength(long value);

  ServerResponse contentType(String value);

  ServerResponse dateNow();

  ServerResponse header(HeaderName name, String value);

  // send

  ServerResponseResult send();

  ServerResponseResult send(byte[] body);

  ServerResponseResult send(Path file);

}