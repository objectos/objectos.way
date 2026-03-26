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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

record HttpRequestBodyImpl(Kind kind, Object value, Map<String, Object> formParams)
    implements
    HttpRequestBody {

  enum Kind {
    NULL,

    BYTE_ARRAY,

    PATH;
  }

  private static final HttpRequestBody NULL = new HttpRequestBodyImpl(Kind.NULL, null, null);

  public static HttpRequestBody of(byte[] bytes, Map<String, Object> formParams) {
    return new HttpRequestBodyImpl(Kind.BYTE_ARRAY, bytes, formParams);
  }

  public static HttpRequestBody of(Path file, Map<String, Object> formParams) {
    return new HttpRequestBodyImpl(Kind.PATH, file, formParams);
  }

  public static HttpRequestBody ofNull() {
    return NULL;
  }

  @Override
  public final InputStream bodyInputStream() throws IOException {
    return switch (kind) {
      case NULL -> InputStream.nullInputStream();

      case BYTE_ARRAY -> new ByteArrayInputStream((byte[]) value);

      case PATH -> Files.newInputStream((Path) value);
    };
  }

  public final void close() throws IOException {
    switch (kind) {
      case PATH -> Files.delete((Path) value);

      default -> {}
    }
  }

  @Override
  public final Set<String> formParamNames() {
    if (formParams == null) {
      return Set.of();
    } else {
      return formParams.keySet();
    }
  }

  @Override
  public final String formParam(String name) {
    Objects.requireNonNull(name, "name == null");

    if (formParams == null) {
      return null;
    } else {
      return Http.queryParamsGet(formParams, name);
    }
  }

  @Override
  public final int formParamAsInt(String name, int defaultValue) {
    String maybe;
    maybe = formParam(name);

    if (maybe == null) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(maybe);
    } catch (NumberFormatException expected) {
      return defaultValue;
    }
  }

  @Override
  public final long formParamAsLong(String name, long defaultValue) {
    String maybe;
    maybe = formParam(name);

    if (maybe == null) {
      return defaultValue;
    }

    try {
      return Long.parseLong(maybe);
    } catch (NumberFormatException expected) {
      return defaultValue;
    }
  }

  @Override
  public final List<String> formParamAll(String name) {
    Objects.requireNonNull(name, "name == null");

    if (formParams == null) {
      return List.of();
    } else {
      return Http.queryParamsGetAll(formParams, name);
    }
  }

  @Override
  public final IntStream formParamAllAsInt(String name, int defaultValue) {
    return formParamAll(name).stream().mapToInt(s -> {
      try {
        return Integer.parseInt(s);
      } catch (NumberFormatException expected) {
        return defaultValue;
      }
    });
  }

  @Override
  public final LongStream formParamAllAsLong(String name, long defaultValue) {
    return formParamAll(name).stream().mapToLong(s -> {
      try {
        return Long.parseLong(s);
      } catch (NumberFormatException expected) {
        return defaultValue;
      }
    });
  }

}
