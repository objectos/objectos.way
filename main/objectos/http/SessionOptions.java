/*
 /// Copyright (C) 2023-2026 Objectos Software LTDA.
 *
 /// Licensed under the Apache License, Version 2.0 (the "License");
 /// you may not use this file except in compliance with the License.
 /// You may obtain a copy of the License at
 *
 /// http://www.apache.org/licenses/LICENSE-2.0
 *
 /// Unless required by applicable law or agreed to in writing, software
 /// distributed under the License is distributed on an "AS IS" BASIS,
 /// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 /// See the License for the specific language governing permissions and
 /// limitations under the License.
 */
package objectos.http;

import java.time.Duration;
import java.time.InstantSource;
import java.util.random.RandomGenerator;

/// Configures the HTTP session related options of a `Server` instance.
public sealed interface SessionOptions permits SessionStoreBuilder {

  /// Sets the session cookie `Max-Age` attribute to the specified value.
  ///
  /// @param duration the session cookie `Max-Age` attribute value
  void cookieMaxAge(Duration duration);

  /// Sets the client session cookie name.
  ///
  /// @param name the client session cookie name
  void cookieName(String name);

  /// Sets the session cookie `Path` attribute to the specified value.
  ///
  /// @param path the session cookie `Path` attribute value
  void cookiePath(String path);

  /// Sets the session cookie `Secure` attribute to the specified value.
  ///
  /// @param value the session cookie `Secure` attribute value
  void cookieSecure(boolean value);

  /// Discards empty sessions, during a clean up operation, whose last access
  /// time is greater than the specified duration.
  ///
  /// @param duration the duration value
  void emptyMaxAge(Duration duration);

  /// Sets the `InstantSource` to use for setting session time related values.
  ///
  /// @param value the `InstantSource` instance to use
  void instantSource(InstantSource value);

  /// Sets the `RandomGenerator` to use for generating session token values.
  ///
  /// @param value the `RandomGenerator` instance to use
  void randomGenerator(RandomGenerator value);

}
