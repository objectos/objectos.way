/*
 * Copyright (C) 2021-2022 Objectos Software LTDA.
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
package br.com.objectos.logging;

/**
 * A log event that takes three arguments.
 *
 * @param <T1> the type of the first log argument
 * @param <T2> the type of the second log argument
 * @param <T3> the type of the third log argument
 */
public final class Event3<T1, T2, T3> extends Event {

  /**
   * Creates a new event instance.
   *
   * @param source
   *        the class this event is bound to
   * @param key
   *        a key that uniquely identifies this event within the given
   *        {@code source} class
   * @param level
   *        the logging level of this event
   */
  public Event3(Class<?> source, String key, Level level) {
    super(source, key, level);
  }

}
