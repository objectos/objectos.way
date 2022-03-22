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
 * A log event that takes no arguments.
 */
public final class Event0 extends Event {

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
  public Event0(Class<?> source, String key, Level level) {
    super(source, key, level);
  }

}