/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package br.com.objectos.css.select;

import br.com.objectos.css.parser.IsTerminal;
import objectos.lang.Check;

public enum AttributeValueOperator implements IsTerminal {

  CONTAINS("*=", "contains") {
    @Override
    final boolean matches(String expression, String actual) {
      return actual.contains(expression);
    }
  },

  ENDS_WITH("$=", "endsWith") {
    @Override
    final boolean matches(String expression, String actual) {
      return actual.endsWith(expression);
    }
  },

  EQUALS("=", "eq") {
    @Override
    final boolean matches(String expression, String actual) {
      return expression.equals(actual);
    }
  },

  HYPHEN("|=", "lang") {
    @Override
    final boolean matches(String expression, String actual) {
      boolean matches = false;
      String[] parts = actual.split("-");
      if (parts.length > 0) {
        matches = parts[0].equals(expression);
      }
      return matches;
    }
  },

  STARTS_WITH("^=", "startsWith") {
    @Override
    final boolean matches(String expression, String actual) {
      return actual.startsWith(expression);
    }
  },

  WS_LIST("~=", "whitespaceSeparated") {
    @Override
    final boolean matches(String expression, String actual) {
      String[] parts = actual.split("\\s");
      for (String part : parts) {
        if (expression.equals(part)) {
          return true;
        }
      }
      return false;
    }
  };

  private static final AttributeValueOperator[] ARRAY = values();

  private final String methodName;
  private final String symbol;

  private AttributeValueOperator(String symbol, String methodName) {
    this.symbol = symbol;
    this.methodName = methodName;
  }

  public static AttributeValueOperator getByCode(int code) {
    return ARRAY[code];
  }

  public final int getCode() {
    return ordinal();
  }

  public final String getMethodName() {
    return methodName;
  }

  public final String getSymbol() {
    return symbol;
  }

  @Override
  public final String toString() {
    return symbol;
  }

  public final AttributeValueElement withValue(String value) {
    Check.notNull(value, "value == null");

    return new AttributeValueElement(this, value);
  }

  abstract boolean matches(String expression, String actual);

}