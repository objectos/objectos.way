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

import java.time.Duration;
import java.util.Objects;

public class SetCookie {

  private final String name;

  private final String value;

  private Duration maxAge;

  private SetCookie(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public static SetCookie of(String name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    return new SetCookie(name, value);
  }

  public final SetCookie maxAge(Duration duration) {
    Objects.requireNonNull(duration, "duration == null");

    if (duration.isZero()) {
      throw new IllegalArgumentException("maxAge must not be zero");
    }

    if (duration.isNegative()) {
      throw new IllegalArgumentException("maxAge must not be negative");
    }

    maxAge = duration;

    return this;
  }

  @Override
  public final String toString() {
    StringBuilder s;
    s = new StringBuilder();

    s.append(name);

    s.append('=');

    s.append(value);

    if (maxAge != null) {
      s.append("; Max-Age=");

      s.append(maxAge.getSeconds());
    }

    return s.toString();
  }

}
