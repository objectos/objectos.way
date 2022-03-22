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
 * Minimal version of Checks. Just so this project has zero deps.
 */
final class Checks {

  private Checks() {}

  public static <T> T checkNotNull(T object, Object message) {
    if (object == null) {
      String formatted;
      formatted = String.valueOf(message);

      throw new NullPointerException(formatted);
    }

    return object;
  }

}