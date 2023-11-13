/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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
package objectos.notes;

final class Duo<T1, T2> {

  final T1 value1;

  final T2 value2;

  Duo(T1 value1, T2 value2) {
    this.value1 = value1;
    this.value2 = value2;
  }

  @Override
  public final String toString() {
    return "value1=" + value1 + ":value2=" + value2;
  }

}