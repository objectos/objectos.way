/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.concurrent;

import objectos.core.service.AbstractService;
import objectos.lang.object.Check;
import objectos.notes.NoteSink;

abstract class AbstractConcurrentService extends AbstractService {

  NoteSink logger;

  /**
   * Have this service use the specified logger for logging.
   *
   * @param logger
   *        a logger object
   *
   * @throws IllegalStateException
   *         if this service has been started
   */
  public final void setLogger(NoteSink logger) {
    Check.state(!isStarted(), "Service already started");

    this.logger = Check.notNull(logger, "logger == null");
  }

  abstract boolean isStarted();

}