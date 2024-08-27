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
import java.time.Duration;
import java.util.Objects;
import java.util.Random;
import objectos.lang.object.Check;
import objectos.way.Session.Repository.Option;

/**
 * The <strong>Objectos Session</strong> main class.
 */
public final class Session {

  /**
   * An instance of a session uniquely identifies the user of an application.
   */
  public sealed interface Instance permits SessionInstance {

    /**
     * The identifier of this session.
     *
     * @return the identifier of this session.
     */
    String id();

    /**
     * Returns the object associated to the specified class instance, or
     * {@code null} if there's no object associated.
     *
     * @param <T> the type of the object
     *
     * @param type
     *        the class instance to search for
     *
     * @return the object associated or {@code null} if there's no object
     *         associated
     */
    <T> T get(Class<T> type);

    /**
     * Returns the object associated to the specified name, or {@code null} if
     * there's no object associated.
     *
     * @param name
     *        the name to search for
     *
     * @return the object associated or {@code null} if there's no object
     *         associated
     */
    Object get(String name);

    <T> Object put(Class<T> type, T value);

    Object put(String name, Object value);

    Object remove(String name);

    void invalidate();

  }

  /**
   * Creates, stores and manages session instances.
   */
  public sealed interface Repository permits SessionRepository {

    /**
     * A repository configuration option.
     */
    public sealed interface Option {}

    void cleanUp();

    /**
     * Creates and immediately stores a new session instance.
     *
     * @return a newly created session instance.
     */
    Instance createNext();

    Instance get(Http.Request.Cookies cookies);

    Instance get(String id);

    /**
     * Returns a Set-Cookie header value for the specified session ID.
     *
     * @param id
     *        the id of the session
     *
     * @return the value of a Set-Cookie header for the specified session ID
     */
    String setCookie(String id);

    /**
     * Stores the specified {@code session} in this repository. If a session
     * instance with the same ID is already managed by this repository then the
     * existing session is replaced by the specified one.
     *
     * @param session
     *        the session instance to be stored
     *
     * @return the previously stored session instance or {@code null}
     */
    Instance store(Instance session);

  }

  non-sealed static abstract class SessionRepositoryOption implements Repository.Option {

    abstract void accept(SessionRepository.Builder builder);

  }

  private Session() {}

  /**
   * Creates a new session repository with the specified configuration options.
   */
  public static Repository createRepository(Repository.Option... options) {
    SessionRepository.Builder builder;
    builder = new SessionRepository.Builder();

    for (int i = 0, len = options.length; i < len; i++) {
      Option o;
      o = Check.notNull(options[i], "options[", i, "] == null");

      SessionRepositoryOption option;
      option = (SessionRepositoryOption) o;

      option.accept(builder);
    }

    return builder.build();
  }

  /**
   * Repository option: use the specified {@code clock} when setting session
   * instances time related values.
   *
   * @param value
   *        the clock instance to use
   *
   * @return a new repository configuration option
   */
  public static Repository.Option clock(Clock value) {
    Check.notNull(value, "value == null");

    return new SessionRepositoryOption() {
      @Override
      final void accept(SessionRepository.Builder builder) {
        builder.clock = value;
      }
    };
  }

  /**
   * Repository option: use the specified {@code name} when setting the client
   * session cookie.
   *
   * @param name
   *        the cookie name to use
   *
   * @return a new repository configuration option
   */
  public static Repository.Option cookieName(String name) {
    Check.notNull(name, "name == null");

    return new SessionRepositoryOption() {
      @Override
      final void accept(SessionRepository.Builder builder) {
        builder.cookieName = name;
      }
    };
  }

  /**
   * Repository option: sets the session cookie Path attribute to the specified
   * value.
   *
   * @param path
   *        the value of the Path attribute
   *
   * @return a new repository configuration option
   */
  public static Repository.Option cookiePath(String path) {
    Check.notNull(path, "path == null");

    return new SessionRepositoryOption() {
      @Override
      final void accept(SessionRepository.Builder builder) {
        builder.cookiePath = path;
      }
    };
  }

  /**
   * Repository option: sets the session cookie Max-Age attribute to the
   * specified value.
   *
   * @param duration
   *        the value of the Max-Age attribute
   *
   * @return a new repository configuration option
   */
  public static Repository.Option cookieMaxAge(Duration duration) {
    Objects.requireNonNull(duration, "duration == null");

    if (duration.isZero()) {
      throw new IllegalArgumentException("maxAge must not be zero");
    }

    if (duration.isNegative()) {
      throw new IllegalArgumentException("maxAge must not be negative");
    }

    return new SessionRepositoryOption() {
      @Override
      final void accept(SessionRepository.Builder builder) {
        builder.cookieMaxAge = duration;
      }
    };
  }

  /**
   * Repository option: discards empty sessions, during a {@link #cleanUp()}
   * operation, whose last access time is greater than the specified duration.
   *
   * @param duration
   *        the duration value
   *
   * @return a new repository configuration option
   */
  public static Repository.Option emptyMaxAge(Duration duration) {
    Objects.requireNonNull(duration, "duration == null");

    if (duration.isZero()) {
      throw new IllegalArgumentException("emptyMaxAge must not be zero");
    }

    if (duration.isNegative()) {
      throw new IllegalArgumentException("emptyMaxAge must not be negative");
    }

    return new SessionRepositoryOption() {
      @Override
      final void accept(SessionRepository.Builder builder) {
        builder.emptyMaxAge = duration;
      }
    };
  }

  /**
   * Repository option: use the specified {@link Random} instance for generating
   * session IDs.
   *
   * @param random
   *        the {@link Random} instance to use
   *
   * @return a new repository configuration option
   */
  public static Repository.Option random(Random random) {
    Check.notNull(random, "random == null");

    return new SessionRepositoryOption() {
      @Override
      final void accept(SessionRepository.Builder builder) {
        builder.random = random;
      }
    };
  }

}
