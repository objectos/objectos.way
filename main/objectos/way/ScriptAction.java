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

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/// Represents an action to be executed by the browser in the context of an web
/// application.
record ScriptAction(String value) implements Media.Bytes {

  static ScriptAction create(Consumer<? super Script> script) {
    final ScriptWriter writer;
    writer = new ScriptWriter();

    script.accept(writer);

    return writer.build();
  }

  /// Return `application/json` always.
  /// @return always {@code application/json}
  @Override
  public final String contentType() {
    return "application/json";
  }

  /// Returns the JSON data representing this action as a byte array.
  /// @return the JSON data representing this action as a byte array
  @Override
  public final byte[] toByteArray() {
    String s;
    s = toString();

    return s.getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public final String toString() {
    return value;
  }

  final void writeTo(StringBuilder json) {
    // strip array delimiters
    json.append(value, 1, value.length() - 1);
  }

}