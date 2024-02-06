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
package objectos.css;

enum UtilityKind {

  UNKNOWN(""),

  DISPLAY("display"),

  FLEX_DIRECTION("flex-direction");

  private final String property1;

  private UtilityKind(String property1) {
    this.property1 = property1;
  }

  final Utility name(String className) {
    return new WithNameAndValue(this, className, className);
  }

  final Utility nameValue(String className, String value) {
    return new WithNameAndValue(this, className, value);
  }

  private static final class WithNameAndValue extends Utility {
    private final String className;
    private final String value;

    public WithNameAndValue(UtilityKind kind, String className, String value) {
      super(kind);
      this.className = className;
      this.value = value;
    }

    @Override
    public final String toString() {
      return "." + className + " { " + kind.property1 + ": " + value + " }";
    }
  }

}