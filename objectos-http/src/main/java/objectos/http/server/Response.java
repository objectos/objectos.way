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
package objectos.http.server;

import java.nio.charset.Charset;
import objectos.http.Http.Header;
import objectos.http.Http.Status;
import objectos.http.io.CharWritable;

public interface Response {

  void header(Header.Name name, String value);

  void send(byte[] data);

  void send(CharWritable entity, Charset charset);

  void status(Status status);

}