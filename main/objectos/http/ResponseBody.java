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
import objectos.lang.ByteSource;
import objectos.lang.CharSource;

record ResponseBody(Kind kind, Object value) {

  enum Kind {

    EMPTY,

    BYTES,

    FILE,

    BYTE_SOURCE,

    CHAR_SOURCE;

  }

  static final ResponseBody EMPTY = new ResponseBody(Kind.EMPTY, null);

  public static ResponseBody of(byte[] bytes, int offset, int length) {
    throw new UnsupportedOperationException("Implement me");
  }

  public static ResponseBody of(Path file) {
    throw new UnsupportedOperationException("Implement me");
  }

  public static ResponseBody of(ByteSource source) {
    throw new UnsupportedOperationException("Implement me");
  }

  public static ResponseBody of(CharSource source) {
    return new ResponseBody(Kind.CHAR_SOURCE, source);
  }

}
