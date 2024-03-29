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

import java.io.Closeable;
import java.io.IOException;
import objectos.http.WayServerLoop.ParseStatus;

public interface ServerLoop extends Closeable, ServerExchange {

  /**
   * Closes and ends this exchange by closing its underlying socket.
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  @Override
  void close() throws IOException;

  ParseStatus parse() throws IOException, IllegalStateException;

  void commit() throws IOException, IllegalStateException;

  boolean keepAlive();

}