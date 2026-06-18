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

final class PathExpressionParserStart {

  final PathExpressionParser ctx;

  PathExpressionParserStart(PathExpressionParser ctx) {
    this.ctx = ctx;
  }

  public final void execute() {
    if (!ctx.hasNext()) {
      final String msg;
      msg = "Invalid path expression: it must not be empty";

      throw new IllegalArgumentException(msg);
    }

    final char first;
    first = ctx.peek();

    if (first != '/') {
      final String msg;
      msg = "Invalid path expression: it must begin with the '/' character";

      throw new IllegalArgumentException(msg);
    }
  }

}
