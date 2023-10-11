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
package objectox.lang;

import objectos.lang.Level;
import objectos.lang.NoteSink;

public sealed interface NoteSinkBuilder<SELF extends NoteSinkBuilder<SELF>>
    permits objectos.lang.NoteSink.OfConsole {

  /**
   * Sets the note sink level. The note sink instance created by this builder
   * will only send notes whose level is greater or equal to the specified
   * level.
   *
   * @param level
   *        the level
   *
   * @return this builder
   */
  SELF level(Level level);

  /**
   * Returns a newly created {@code NoteSink} instance from the current state of
   * this builder.
   *
   * @return a new {@code NoteSink} instance from the current state of this
   *         builder
   */
  NoteSink start();

}