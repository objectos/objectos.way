/*
 * Copyright (C) 2022-2022 Objectos Software LTDA.
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

import java.util.NoSuchElementException;

/**
 * Provides {@code static} utility methods for accessing system property values.
 *
 * @since 0.2
 */
public final class SystemProperty {

  private SystemProperty() {}

  /**
   * Returns the system property associated with the given property name or
   * throws a {@link NoSuchElementException} if unable to do so.
   *
   * @param propertyName
   *        the name of the property to obtain
   *
   * @return the system property associated with the given name
   *
   * @throws NoSuchElementException
   *         if no value is associated with the given name
   */
  public static String get(String propertyName) {
    String property;
    property = System.getProperty(propertyName);

    if (property == null) {
      throw new NoSuchElementException(propertyName);
    }

    return property;
  }

}