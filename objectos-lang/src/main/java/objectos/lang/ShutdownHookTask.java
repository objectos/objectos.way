/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
package objectos.lang;

/**
 * Represents an arbitrary task to be executed in the {@link ShutdownHook}
 * facility.
 *
 * @since 0.2
 */
public interface ShutdownHookTask {

  /**
   * Executes a task during the JVM shutdown process, or throws an exception
   * if it is unable to.
   *
   * @throws Exception
   *         if it is unable to execute the task
   */
  void executeShutdownHookTask() throws Exception;

}