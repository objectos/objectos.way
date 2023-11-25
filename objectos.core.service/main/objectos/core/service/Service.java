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
package objectos.core.service;

/**
 * Defines a Service class.
 *
 * <p>
 * Service is any class whose instances must be started during an application
 * initialization and must be stopped at the application shutdown.
 *
 * @since 2
 */
public interface Service {

  /**
   * Starts this service, or throws an exception if the start operation fails.
   *
   * @throws Exception
   *         if the start operation fails
   */
  void startService() throws Exception;

  /**
   * Stops this service, or throws an exception if the stop operation fails.
   *
   * @throws Exception
   *         if the stop operation fails
   */
  void stopService() throws Exception;

}