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

import java.nio.file.Path;
import objectos.way.Media;

sealed interface ResponseBody {

  enum OfEmpty implements ResponseBody {
    INSTANCE;
  }

  record OfBytes(byte[] bytes, int offset, int length) implements ResponseBody {}

  record OfFile(Path file) implements ResponseBody {}

  record OfMediaStream(Media.Stream entity) implements ResponseBody {}

  record OfMediaText(Media.Text entity) implements ResponseBody {}

}
