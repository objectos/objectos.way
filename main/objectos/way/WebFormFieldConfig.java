/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import java.util.concurrent.atomic.AtomicInteger;

abstract class WebFormFieldConfig {

  private static final AtomicInteger ID = new AtomicInteger(1);

  String label = "";

  String id = null;

  String name = "unnamed";

  String requiredMessage;

  WebFormFieldConfig() {
  }

  public final void label(String value) {
    Check.argument(!value.isBlank(), "Description must not be blank"); // implicit value null-check

    label = value;
  }

  public final void id(String value) {
    Check.argument(!value.isBlank(), "Id must not be blank"); // implicit value null-check

    id = value;
  }

  public final void name(String value) {
    Check.argument(!value.isBlank(), "Name must not be blank"); // implicit value null-check

    name = value;
  }

  public final void required() {
    if (requiredMessage == null) {
      requiredMessage = "Please enter a value";
    }
  }

  final String id() {
    if (id != null) {
      return id;
    }

    return "way-form-field-" + ID.getAndIncrement();
  }

}