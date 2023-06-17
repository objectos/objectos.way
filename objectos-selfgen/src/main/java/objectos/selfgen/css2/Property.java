/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.css2;

import java.util.Locale;
import java.util.Objects;
import objectos.selfgen.util.JavaNames;

final class Property {

  public final String propertyName;

  public final String methodName;

  public final String constantName;

  private Property(String propertyName, String methodName, String constantName) {
    this.propertyName = propertyName;
    this.methodName = methodName;
    this.constantName = constantName;
  }

  public static Property of(String name) {
    var propertyName = Objects.requireNonNull(name, "name == null");

    var methodName = JavaNames.toValidMethodName(propertyName);

    var constantName = propertyName.replace('-', '_').toUpperCase(Locale.US);

    return new Property(propertyName, methodName, constantName);
  }

}