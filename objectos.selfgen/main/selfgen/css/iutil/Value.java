/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package selfgen.css.iutil;

import objectos.code.Code;

public sealed interface Value {

  record ExpressionName(String fieldName) implements Value {
    @Override
    public final String arg() { return fieldName; }
  }

  record LiteralDouble(double value) implements Value {
    @Override
    public final String arg() { return Double.toString(value); }
  }

  record LiteralInt(int value) implements Value {
    @Override
    public final String arg() { return Integer.toString(value); }
  }

  record MethodDouble(String methodName, double value) implements Value {
    @Override
    public final String arg() {
      return methodName + "(" + Double.toString(value) + ")";
    }
  }

  record MethodInt(String methodName, int value) implements Value {
    @Override
    public final String arg() {
      return methodName + "(" + Integer.toString(value) + ")";
    }
  }

  record MethodString(String methodName, String value) implements Value {
    @Override
    public final String arg() {
      return methodName + "(\"" + Code.escape(value) + "\")";
    }
  }

  String arg();

}