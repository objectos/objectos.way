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
package br.com.objectos.core.object;

final class ComponentString {

  private final String value;

  ComponentString(String value) {
    this.value = value;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof ComponentString && equals0((ComponentString) obj);
  }

  private boolean equals0(ComponentString that) {
    return Equals.objects(
        value, that.value
    );
  }

}