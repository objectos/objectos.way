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
package objectos.css.sheet;

import objectos.css.property.StandardPropertyName;
import objectos.css.select.Combinator;
import objectos.css.type.ColorHex;

final class MinifiedStyleSheetWriter extends StyleSheetWriter {

  @Override
  public final void visitAfterLastDeclaration() { /* noop */ }

  @Override
  public final void visitBeforeNextDeclaration() {
    write(';');
  }

  @Override
  public final void visitBlockStart() {
    write('{');
  }

  @Override
  public final void visitCombinator(Combinator combinator) {
    write(combinator.symbol);
  }

  @Override
  public final void visitDeclarationStart(StandardPropertyName name) {
    write(name.getName());
    write(':');
  }

  @Override
  public final void visitEmptyBlock() {
    visitBlockStart();
    visitRuleEnd();
  }

  @Override
  public final void visitMediaEnd() {
    write('}');

    super.visitMediaEnd();
  }

  @Override
  public final void visitMultiDeclarationSeparator() {
    write(',');
  }

  @Override
  public final void visitRuleEnd() {
    write('}');

    super.visitRuleEnd();
  }

  @Override
  public final void writeComma() {
    write(',');
  }

  @Override
  final void quoteIfNecessary(String value) {
    boolean shouldQuote = false;

    char[] array;
    array = value.toCharArray();

    for (int i = 0; i < array.length; i++) {
      char c;
      c = array[i];

      if (Character.isLetterOrDigit(c)) {
        continue;
      }

      shouldQuote = true;

      break;
    }

    if (shouldQuote) {
      quote(value);
    } else {
      write(value);
    }
  }

  @Override
  final void writeBlockSeparator() { /* noop */ }

  @Override
  final void writeDoubleImpl(double value) {
    String string = Double.toString(value);

    if (string.startsWith("0.")) {
      string = string.substring(1);
    }

    write(string);
  }

  @Override
  final void writeFirstValuePrefix() { /* noop */ }

  @Override
  final void writeValueColorHex(String value) {
    ColorHex color;
    color = ColorHex.of(value);

    write(color.getMinifiedHexString());
  }

}