/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import java.time.Clock;
import java.util.Map;
import java.util.Objects;
import objectos.way.Http.TestingExchange.Config;

final class HttpTestingExchangeConfig implements Http.TestingExchange.Config {

  Clock clock;

  Map<Object, Object> objectStore;

  Http.Method method;

  String path;

  Map<String, String> queryParams;

  public final Http.TestingExchange build() {
    return new HttpTestingExchange(this);
  }

  @Override
  public final Config clock(Clock value) {
    clock = Objects.requireNonNull(value, "value == null");

    return this;
  }

  @Override
  public final Http.TestingExchange.Config method(Http.Method value) {
    method = Objects.requireNonNull(value, "value == null");

    return this;
  }

  @Override
  public final Http.TestingExchange.Config path(String value) {
    path = Objects.requireNonNull(value, "value == null");

    return this;
  }

  @Override
  public final Http.TestingExchange.Config pathParam(String name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    if (objectStore == null) {
      objectStore = Util.createMap();
    }

    objectStore.put(name, value);

    return this;
  }

  @Override
  public final Http.TestingExchange.Config queryParam(String name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    if (queryParams == null) {
      queryParams = Util.createMap();
    }

    queryParams.put(name, value);

    return this;
  }

  @Override
  public final <T> Http.TestingExchange.Config set(Class<T> key, T value) {
    Objects.requireNonNull(key, "key == null");
    Objects.requireNonNull(value, "value == null");

    if (objectStore == null) {
      objectStore = Util.createMap();
    }

    objectStore.put(key, value);

    return this;
  }

}