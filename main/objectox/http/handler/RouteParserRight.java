/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http.handler;

import java.util.Arrays;
import objectox.http.Rfc;

final class RouteParserRight {

  final RouteParser ctx;

  RouteParserRight(RouteParser ctx) {
    this.ctx = ctx;
  }

  public final void execute() {
    final int startIndex;
    startIndex = ctx.index();

    final int right;
    right = ctx.indexOf('}');

    if (right == -1) {
      final String msg;
      msg = "Invalid path expression: unclosed path parameter";

      throw new IllegalArgumentException(msg);
    }

    else {
      final String paramName;
      paramName = ctx.substring(startIndex, right);

      final int lastIndex;
      lastIndex = ctx.length() - 1;

      final boolean last;
      last = right == lastIndex;

      execute(paramName, last);
    }
  }

  private void execute(String paramName, boolean last) {
    if (paramName.isEmpty()) {
      executeParamNameEmpty(last);
    }

    else {
      executeParamName(paramName, last);
    }
  }

  private void executeParamNameEmpty(boolean last) {
    if (!last) {
      final String msg;
      msg = "Invalid path expression: the '{}' wildcard path parameter can only be declared at the end of the expression";

      throw new IllegalArgumentException(msg);
    }

    else {
      ctx.add(RouteMatcherWildcard.INSTANCE);

      ctx.end();
    }
  }

  private void executeParamName(String paramName, boolean last) {
    validateParamName(paramName);

    if (last) {
      final RouteMatcher segment;
      segment = new RouteMatcherParamLast(paramName);

      ctx.add(segment);

      ctx.end();
    }

    else {
      final char delim;
      delim = validateTrailingDelim();

      final RouteMatcherParam segment;
      segment = new RouteMatcherParam(paramName, delim);

      ctx.add(segment);
    }
  }

  private void validateParamName(String paramName) {
    final int idenStart;
    idenStart = paramName.codePointAt(0); // safe as name is not empty

    if (!Character.isJavaIdentifierStart(idenStart)) {
      final String msg;
      msg = "Invalid path expression: path parameter name must be a valid Java identifier";

      throw new IllegalArgumentException(msg);
    }

    final boolean idenPart;
    idenPart = paramName.codePoints().skip(1).allMatch(Character::isJavaIdentifierPart);

    if (!idenPart) {
      final String msg;
      msg = "Invalid path expression: path parameter name must be a valid Java identifier";

      throw new IllegalArgumentException(msg);
    }

    if (!ctx.add(paramName)) {
      final String msg;
      msg = "Invalid path expression: duplicate path parameter name '%s'".formatted(paramName);

      throw new IllegalArgumentException(msg);
    }
  }

  private static final char[] DELIMS;

  static {
    final String pathDelim;
    pathDelim = Rfc.pathDelim();

    final char[] delims;
    delims = pathDelim.toCharArray();

    Arrays.sort(delims);

    DELIMS = delims;
  }

  private char validateTrailingDelim() {
    final char rightDelim;
    rightDelim = ctx.next();

    final int validDelim;
    validDelim = Arrays.binarySearch(DELIMS, rightDelim);

    if (validDelim < 0) {
      final String msg;
      msg = "Invalid path expression: path parameter can only be either at the end of the expression or immediately followed by one of " + Rfc.pathDelim();

      throw new IllegalArgumentException(msg);
    }

    return rightDelim;
  }

}
