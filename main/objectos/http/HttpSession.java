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

import objectos.lang.Key;

interface HttpSession {

  /**
   * If a session is not associated to this exchange, returns {@code true},
   * otherwise {@code false}.
   *
   * @return {@code true} if a session is not associated to this
   *         exchange, otherwise {@code false}
   */
  boolean sessionAbsent();

  /**
   * Returns the session value associated to the specified
   * class name, or {@code null} if no value is associated.
   *
   * @param <T>
   *        the type of the session value
   * @param key
   *        the class whose associated value is to be returned
   *
   * @return the session value associated to the specified class name, or
   *         {@code null} if no value is associated
   *
   * @throws IllegalStateException
   *         if no session is associated to this exchange
   */
  <T> T sessionAttr(Class<T> key);

  /**
   * Returns the session value associated to the specified key, or
   * {@code null} if no value is associated.
   *
   * @param <T>
   *        the type of the session value
   * @param key
   *        the key object whose associated value is to be returned
   *
   * @return the session value, or {@code null} if no value is associated
   *
   * @throws IllegalStateException
   *         if no session is associated to this exchange
   */
  <T> T sessionAttr(Key<T> key);

  /**
   * Using the name of the specified class as the key, associate the
   * specified value to this exchange's session.
   *
   * @param <T>
   *        the type of the session value
   * @param key
   *        the class object whose name will serve as the key
   * @param value
   *        the session value
   *
   * @return the previous session value, or {@code null} if no value was
   *         associated
   */
  <T> T sessionAttr(Class<T> key, T value);

  /**
   * Using the specified key, associates the specified value to this
   * exchange's session.
   *
   * @param <T>
   *        the type of the session value
   * @param key
   *        the key object
   * @param value
   *        the session value
   *
   * @return the previous session value, or {@code null} if no value was
   *         associated
   */
  <T> T sessionAttr(Key<T> key, T value);

  /**
   * Invalidates the session associated to this exchange.
   *
   * @throws IllegalStateException
   *         if no session is associated to this exchange
   */
  void sessionInvalidate();

  /**
   * If a session is associated to this exchange, returns {@code true},
   * otherwise {@code false}.
   *
   * @return {@code true} if a session is associated to this
   *         exchange, otherwise {@code false}
   */
  boolean sessionPresent();

}