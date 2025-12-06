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

import java.util.Objects;
import objectos.way.Script.BooleanQuery;

final class ScriptStringQuery implements Script.StringQuery {

  private final ScriptWriter writer;

  private final String query;

  ScriptStringQuery(ScriptWriter writer, String query) {
    this.writer = writer;

    this.query = query;
  }

  @Override
  public final BooleanQuery test(String value) {
    final String v;
    v = Objects.requireNonNull(value, "value == null");

    final String test;
    test = writer.stringTest(this, v);

    return new ScriptBooleanQuery(writer, test);
  }

  @Override
  public final String toString() {
    return query;
  }

}
