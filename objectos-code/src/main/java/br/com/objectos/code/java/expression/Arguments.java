/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.expression;

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.declaration.EnumConstantCode;
import br.com.objectos.code.java.declaration.EnumConstantCodeElement;
import br.com.objectos.code.java.element.AbstractImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.element.NewLine;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.io.Section;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public final class Arguments extends AbstractImmutableCodeElement
    implements
    EnumConstantCodeElement {

  private static final Arguments EMPTY = new Arguments(
      openParens(), closeParens()
  );

  private Arguments(CodeElement... elements) {
    super(elements);
  }

  private Arguments(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  public static Arguments args() {
    return empty();
  }

  public static Arguments args(
      ArgumentsElement a1) {
    Check.notNull(a1, "a1 == null");
    Builder b = new Builder();
    a1.acceptArgumentsBuilder(b);
    return b.build();
  }

  public static Arguments args(ArgumentsElement... args) {
    Check.notNull(args, "args == null");
    Builder b = new Builder();
    for (int i = 0; i < args.length; i++) {
      ArgumentsElement e = Check.notNull(args[i], "args[" + i + "] == null");
      e.acceptArgumentsBuilder(b);
    }
    return b.build();
  }

  public static Arguments args(
      ArgumentsElement a1,
      ArgumentsElement a2) {
    Check.notNull(a1, "a1 == null");
    Check.notNull(a2, "a2 == null");
    Builder b = new Builder();
    a1.acceptArgumentsBuilder(b);
    a2.acceptArgumentsBuilder(b);
    return b.build();
  }

  public static Arguments args(
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3) {
    Check.notNull(a1, "a1 == null");
    Check.notNull(a2, "a2 == null");
    Check.notNull(a3, "a3 == null");
    Builder b = new Builder();
    a1.acceptArgumentsBuilder(b);
    a2.acceptArgumentsBuilder(b);
    a3.acceptArgumentsBuilder(b);
    return b.build();
  }

  public static Arguments args(
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4) {
    Check.notNull(a1, "a1 == null");
    Check.notNull(a2, "a2 == null");
    Check.notNull(a3, "a3 == null");
    Check.notNull(a4, "a4 == null");
    Builder b = new Builder();
    a1.acceptArgumentsBuilder(b);
    a2.acceptArgumentsBuilder(b);
    a3.acceptArgumentsBuilder(b);
    a4.acceptArgumentsBuilder(b);
    return b.build();
  }

  public static Arguments args(
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5) {
    Check.notNull(a1, "a1 == null");
    Check.notNull(a2, "a2 == null");
    Check.notNull(a3, "a3 == null");
    Check.notNull(a4, "a4 == null");
    Check.notNull(a5, "a5 == null");
    Builder b = new Builder();
    a1.acceptArgumentsBuilder(b);
    a2.acceptArgumentsBuilder(b);
    a3.acceptArgumentsBuilder(b);
    a4.acceptArgumentsBuilder(b);
    a5.acceptArgumentsBuilder(b);
    return b.build();
  }

  public static Arguments args(
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6) {
    Check.notNull(a1, "a1 == null");
    Check.notNull(a2, "a2 == null");
    Check.notNull(a3, "a3 == null");
    Check.notNull(a4, "a4 == null");
    Check.notNull(a5, "a5 == null");
    Check.notNull(a6, "a6 == null");
    Builder b = new Builder();
    a1.acceptArgumentsBuilder(b);
    a2.acceptArgumentsBuilder(b);
    a3.acceptArgumentsBuilder(b);
    a4.acceptArgumentsBuilder(b);
    a5.acceptArgumentsBuilder(b);
    a6.acceptArgumentsBuilder(b);
    return b.build();
  }

  public static Arguments args(
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6,
      ArgumentsElement a7) {
    Check.notNull(a1, "a1 == null");
    Check.notNull(a2, "a2 == null");
    Check.notNull(a3, "a3 == null");
    Check.notNull(a4, "a4 == null");
    Check.notNull(a5, "a5 == null");
    Check.notNull(a6, "a6 == null");
    Check.notNull(a7, "a7 == null");
    Builder b = new Builder();
    a1.acceptArgumentsBuilder(b);
    a2.acceptArgumentsBuilder(b);
    a3.acceptArgumentsBuilder(b);
    a4.acceptArgumentsBuilder(b);
    a5.acceptArgumentsBuilder(b);
    a6.acceptArgumentsBuilder(b);
    a7.acceptArgumentsBuilder(b);
    return b.build();
  }

  public static Arguments args(
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6,
      ArgumentsElement a7,
      ArgumentsElement a8) {
    Check.notNull(a1, "a1 == null");
    Check.notNull(a2, "a2 == null");
    Check.notNull(a3, "a3 == null");
    Check.notNull(a4, "a4 == null");
    Check.notNull(a5, "a5 == null");
    Check.notNull(a6, "a6 == null");
    Check.notNull(a7, "a7 == null");
    Check.notNull(a8, "a8 == null");
    Builder b = new Builder();
    a1.acceptArgumentsBuilder(b);
    a2.acceptArgumentsBuilder(b);
    a3.acceptArgumentsBuilder(b);
    a4.acceptArgumentsBuilder(b);
    a5.acceptArgumentsBuilder(b);
    a6.acceptArgumentsBuilder(b);
    a7.acceptArgumentsBuilder(b);
    a8.acceptArgumentsBuilder(b);
    return b.build();
  }

  public static Arguments args(
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6,
      ArgumentsElement a7,
      ArgumentsElement a8,
      ArgumentsElement a9) {
    Check.notNull(a1, "a1 == null");
    Check.notNull(a2, "a2 == null");
    Check.notNull(a3, "a3 == null");
    Check.notNull(a4, "a4 == null");
    Check.notNull(a5, "a5 == null");
    Check.notNull(a6, "a6 == null");
    Check.notNull(a7, "a7 == null");
    Check.notNull(a8, "a8 == null");
    Check.notNull(a9, "a9 == null");
    Builder b = new Builder();
    a1.acceptArgumentsBuilder(b);
    a2.acceptArgumentsBuilder(b);
    a3.acceptArgumentsBuilder(b);
    a4.acceptArgumentsBuilder(b);
    a5.acceptArgumentsBuilder(b);
    a6.acceptArgumentsBuilder(b);
    a7.acceptArgumentsBuilder(b);
    a8.acceptArgumentsBuilder(b);
    a9.acceptArgumentsBuilder(b);
    return b.build();
  }

  public static Arguments args(Iterable<? extends ArgumentsElement> args) {
    Check.notNull(args, "args == null");
    Builder b = new Builder();
    int i = 0;
    for (ArgumentsElement e : args) {
      e = Check.notNull(e, "args[" + i + "] == null");
      e.acceptArgumentsBuilder(b);
      i++;
    }
    return b.build();
  }

  @Ignore
  public static Builder builder() {
    return new Builder();
  }

  @Ignore
  public static Arguments empty() {
    return EMPTY;
  }

  static Arguments of(ArgumentsElement... elements) {
    switch (elements.length) {
      case 0:
        return EMPTY;
      default:
        Builder b = new Builder();
        for (ArgumentsElement e : elements) {
          e.acceptArgumentsBuilder(b);
        }
        return b.build();
    }
  }

  static Arguments of(Iterable<? extends CodeElement> args) {
    return new Arguments(
        openParens(), commaSeparated(args), closeParens()
    );
  }

  @Override
  public final void acceptEnumConstantCodeBuilder(EnumConstantCode.Builder builder) {
    builder.arguments(this);
  }

  public static class Builder {

    private final GrowableList<CodeElement> elements = new GrowableList<>();

    private CommaOrNoop lastCommaOrNoop = CommaOrNoop.noop();

    private Builder() {
      elements.add(openParens());
      elements.add(beginSection(Section.ARGUMENTS));
    }

    public final Builder addArgument(Argument argument) {
      Check.notNull(argument, "argument == null");
      addArgumentUnchecked(argument);
      return this;
    }

    public final Builder addNewLine() {
      elements.add(NewLine.nextLine());
      lastCommaOrNoop.spaceOff();
      return this;
    }

    public final Arguments build() {
      lastCommaOrNoop.setNoop();

      elements.add(endSection());
      elements.add(indentIfNecessary());
      elements.add(closeParens());
      return new Arguments(elements.toUnmodifiableList());
    }

    final void addArgumentUnchecked(Argument argument) {
      elements.add(argument);

      lastCommaOrNoop = CommaOrNoop.commaAndSpace();
      elements.add(lastCommaOrNoop);
    }

  }

  private static class CommaOrNoop implements CodeElement {

    private static final CommaOrNoop NOOP = new CommaOrNoop(false, false);

    private boolean comma;
    private boolean space;

    CommaOrNoop(boolean comma, boolean space) {
      this.comma = comma;
      this.space = space;
    }

    static CommaOrNoop commaAndSpace() {
      return new CommaOrNoop(true, true);
    }

    static CommaOrNoop noop() {
      return NOOP;
    }

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      if (comma) {
        w.write(',');
      }
      if (space) {
        w.write(' ');
      }
      return w;
    }

    public final void setNoop() {
      comma = false;
      space = false;
    }

    public final void spaceOff() {
      space = false;
    }

  }

}