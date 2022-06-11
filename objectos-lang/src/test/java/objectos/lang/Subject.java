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

class Subject {

  private final ComponentInt a;

  private final ComponentString b;

  Subject(ComponentInt a, ComponentString b) {
    this.a = a;
    this.b = b;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof Subject && equals0((Subject) obj);
  }

  private boolean equals0(Subject obj) {
    return Equals.of(
        a, obj.a,
        b, obj.b
    );
  }

}