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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

final class RequestBodyData {

  private enum Kind {
    NULL,

    BYTE_ARRAY,

    PATH;
  }

  private static final RequestBodyData NULL = new RequestBodyData(Kind.NULL, null);

  private final Kind kind;

  private final Object value;

  private RequestBodyData(Kind kind, Object value) {
    this.kind = kind;

    this.value = value;
  }

  public static RequestBodyData of(byte[] bytes) {
    return new RequestBodyData(Kind.BYTE_ARRAY, bytes);
  }

  public static RequestBodyData of(Path file) {
    return new RequestBodyData(Kind.PATH, file);
  }

  public static RequestBodyData ofNull() {
    return NULL;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof RequestBodyData that
        && kind == that.kind
        && equalsValue0(that);
  }

  private boolean equalsValue0(RequestBodyData that) {
    return kind != Kind.BYTE_ARRAY
        ? Objects.equals(value, that.value)
        : Arrays.equals((byte[]) value, (byte[]) that.value);
  }

  public final InputStream open() throws IOException {
    return switch (kind) {
      case NULL -> InputStream.nullInputStream();

      case BYTE_ARRAY -> new ByteArrayInputStream((byte[]) value);

      case PATH -> Files.newInputStream((Path) value);
    };
  }

}
