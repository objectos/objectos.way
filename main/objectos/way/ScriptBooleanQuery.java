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

import objectos.way.Script.BooleanQuery;
import objectos.way.Script.Callback;

sealed abstract class ScriptBooleanQuery implements Script.BooleanQuery {

  private static final class StringQueryTest extends ScriptBooleanQuery {

    private final ScriptStringQuery query;

    private final String value;

    private final ScriptWriter writer;

    StringQueryTest(ScriptStringQuery query, String value, ScriptWriter writer) {
      this.query = query;

      this.value = value;

      this.writer = writer;
    }

    @Override
    public final void when(boolean value, Callback action) {
      writer.arrayStart();

      writer.stringLiteral("boolean-test");

      writer.arrayEnd();
    }

  }

  public static BooleanQuery stringQueryTest(ScriptStringQuery query, String value, ScriptWriter writer) {
    return new StringQueryTest(query, value, writer);
  }

}
