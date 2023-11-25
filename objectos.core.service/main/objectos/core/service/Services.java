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

import java.util.Objects;

/**
 * Provides {@code static} utility methods for {@link Service} instances.
 *
 * @since 2
 */
public final class Services {

  private Services() {}

  /**
   * Start and register with the {@link ShutdownHook} facility the specified
   * services.
   *
   * @param services
   *        the services for the be started and registered with the
   *        {@link ShutdownHook}. The specified services are processed in order.
   *
   * @throws Exception
   *         if a service fails to be started
   */
  public static void start(Service... services) throws Exception {
    Objects.requireNonNull(services, "services == null");

    for (int i = 0; i < services.length; i++) {
      Service s;
      s = services[i];

      if (s == null) {
        throw new NullPointerException("services[" + i + "] == null");
      }

      var rt = Runtime.getRuntime();

      rt.addShutdownHook(
          new Thread(
              () -> {
                try {
                  s.stopService();
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
          )
      );

      s.startService();
    }
  }

}