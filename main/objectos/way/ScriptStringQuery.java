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

import java.util.List;
import objectos.way.Script.BooleanQuery;
import objectos.way.Script.StringQuery;

sealed abstract class ScriptStringQuery implements Script.StringQuery {

  private static final class ElementMethodInvocation extends ScriptStringQuery {

    private final ScriptElement element;

    private final String methodName;

    private final Object argumentOrList;

    private final ScriptWriter writer;

    ElementMethodInvocation(ScriptElement element, String methodName, Object argumentOrList, ScriptWriter writer) {
      this.element = element;

      this.methodName = methodName;

      this.argumentOrList = argumentOrList;

      this.writer = writer;
    }

    @Override
    public final BooleanQuery test(String value) {
      return ScriptBooleanQuery.stringQueryTest(this, value, writer);
    }

    @Override
    final void write() {
      writer.arrayStart();

      // instruction
      element.methodInvocation();

      // method name
      writer.comma();
      writer.stringLiteral(methodName);

      // args
      if (argumentOrList instanceof List<?> list) {
        for (Object o : list) {
          writer.comma();
          writer.literal(o);
        }
      } else {
        writer.comma();
        writer.stringLiteral(argumentOrList.toString());
      }

      writer.arrayEnd();
    }

  }

  public static StringQuery elementMethodInvocation(ScriptElement element, String methodName, Object args, ScriptWriter writer) {
    return new ElementMethodInvocation(element, methodName, args, writer);
  }

  abstract void write();

}
