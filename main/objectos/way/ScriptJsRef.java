/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import objectos.way.Script.JsElement;
import objectos.way.Script.JsRef;
import objectos.way.Script.JsString;

final class ScriptJsRef implements Script.JsRef {

  private final String value;

  ScriptJsRef(String value) {
    this.value = value;
  }

  public static JsRef args(int index) {
    if (index < 0) {
      throw new IllegalArgumentException(
          "index must not be negative"
      );
    }

    final ScriptJsArray.Builder builder;
    builder = new ScriptJsArray.Builder();

    builder.rawString("AX");
    builder.jsNumber(index);

    return builder.build(ScriptJsRef::new);
  }

  public static JsRef var(String name) {
    final ScriptJsArray.Builder builder;
    builder = new ScriptJsArray.Builder();

    builder.rawString("CR");
    builder.jsString(name, "name");

    return builder.build(ScriptJsRef::new);
  }

  @Override
  public final JsElement asElem() {
    return new ScriptJsElement(value);
  }

  @Override
  public final JsString asString() {
    return new ScriptJsString(value);
  }

  @Override
  public final String toString() {
    return value;
  }

}
