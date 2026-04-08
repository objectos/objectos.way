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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

record HttpRequestBody0(HttpRequestBodyData data, Map<String, Object> formParams)
    implements
    HttpRequestBody {

  @Override
  public final InputStream bodyInputStream() throws IOException {
    return data.open();
  }

  @Override
  public final Set<String> formParamNames() {
    return formParams.keySet();
  }

  @Override
  public final String formParam(String name) {
    Objects.requireNonNull(name, "name == null");

    return Http.queryParamsGet(formParams, name);
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

    return Http.queryParamsGetAll(formParams, name);
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
