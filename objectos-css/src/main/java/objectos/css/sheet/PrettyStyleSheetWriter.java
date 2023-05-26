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

final class PrettyStyleSheetWriter extends StyleSheetWriter {
  private static class Indentation {
    private final char c;
    private final int n;

    private int level = 0;

    private Indentation(char c, int n) {
      this.c = c;
      this.n = n;
    }

    public static Indentation standard() {
      return new Indentation(' ', 2);
    }

    public void decrement() {
      level--;
    }

    public void increment() {
      level++;
    }

    public void write(StyleSheetWriter writer) {
      int count = level * n;
      for (int i = 0; i < count; i++) {
        writer.write(c);
      }
    }
  }

  private final Indentation indentation = Indentation.standard();

  public final void indent() {
    indentation.increment();
  }

  public final void unindent() {
    indentation.decrement();
  }

  @Override
  public void visitAfterLastDeclaration() {
    write(';');
  }

  @Override
  public void visitBeforeNextDeclaration() {
    visitAfterLastDeclaration();
    writeNewLine();
  }

  @Override
  public void visitBlockStart() {
    write(' ');
    write('{');
    writeNewLine();
    indent();
  }

  @Override
  public void visitCombinator(Combinator combinator) {
    switch (combinator) {
      case DESCENDANT -> write(' ');

      case LIST -> {
        write(',');
        write(' ');
      }

      default -> {
        write(' ');
        write(combinator.symbol);
        write(' ');
      }
    }
  }

  @Override
  public void visitDeclarationStart(StandardPropertyName name) {
    writeIndentation();
    write(name.getName());
    write(':');
    write(' ');
  }

  @Override
  public void visitEmptyBlock() {
    write(' ');
    write('{');
    write('}');
  }

  @Override
  public final void visitMediaEnd() {
    writeNewLine();
    unindent();
    writeIndentation();
    write('}');

    super.visitMediaEnd();
  }

  @Override
  public void visitMultiDeclarationSeparator() {
    write(',');
    write(' ');
  }

  @Override
  public final void visitRuleEnd() {
    writeNewLine();
    unindent();
    writeIndentation();
    write('}');

    super.visitRuleEnd();
  }

  @Override
  public void visitRuleStart() {
    super.visitRuleStart();

    writeIndentation();
  }

  @Override
  public void writeComma() {
    write(',');
    write(' ');
  }

  public void writeIndentation() {
    indentation.write(this);
  }

  public void writeNewLine() {
    write(System.lineSeparator());
  }

  @Override
  final void quoteIfNecessary(String value) {
    quote(value);
  }

  @Override
  final void writeBlockSeparator() {
    writeNewLine();
    writeNewLine();
  }

  @Override
  final void writeFirstValuePrefix() {
    write(' ');
  }

  @Override
  final void writeValueColorHex(String value) {
    write(value);
  }
}