/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.security.SecureRandom;
import java.time.Clock;
import java.time.Duration;
import java.util.Objects;
import java.util.random.RandomGenerator;

final class WebSecureConfig implements Web.Secure.Config {

  Clock clock = Clock.systemDefaultZone();

  Duration cookieMaxAge;

  String cookieName = "OBJECTOSWAY";

  String cookiePath = "/";

  Duration emptyMaxAge = Duration.ofMinutes(5);

  RandomGenerator randomGenerator;

  @Override
  public final void clock(Clock value) {
    clock = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void cookieName(String name) {
    cookieName = Objects.requireNonNull(name, "name == null");
  }

  @Override
  public final void cookiePath(String path) {
    cookiePath = Objects.requireNonNull(path, "path == null");
  }

  @Override
  public final void cookieMaxAge(Duration duration) {
    Objects.requireNonNull(duration, "duration == null");

    if (duration.isZero()) {
      throw new IllegalArgumentException("maxAge must not be zero");
    }

    if (duration.isNegative()) {
      throw new IllegalArgumentException("maxAge must not be negative");
    }

    cookieMaxAge = duration;
  }

  @Override
  public final void emptyMaxAge(Duration duration) {
    Objects.requireNonNull(duration, "duration == null");

    if (duration.isZero()) {
      throw new IllegalArgumentException("emptyMaxAge must not be zero");
    }

    if (duration.isNegative()) {
      throw new IllegalArgumentException("emptyMaxAge must not be negative");
    }

    emptyMaxAge = duration;
  }

  @Override
  public final void randomGenerator(RandomGenerator value) {
    this.randomGenerator = Objects.requireNonNull(value, "value == null");
  }

  final Web.Secure build() {
    if (randomGenerator == null) {
      randomGenerator = new SecureRandom();
    }

    return new WebSecure(this);
  }

}