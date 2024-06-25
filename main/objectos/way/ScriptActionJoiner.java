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

final class ScriptActionJoiner {

  private final StringBuilder json;

  private int count;

  public ScriptActionJoiner() {
    json = new StringBuilder();

    json.append('[');
  }

  public final void add(Object o) {
    if (count > 0) {
      json.append(',');
    }

    ScriptAction a;
    a = (ScriptAction) o;

    a.writeTo(json);

    count++;
  }

  public final String join() {
    json.append(']');

    return json.toString();
  }

}