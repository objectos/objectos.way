/*
 * Copyright (C) 2021-2025 Objectos Software LTDA.
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
package objectos.asciidoc;

import java.util.Arrays;
import objectos.lang.Check;
import objectos.util.IntArrays;

class Pass2 {

  interface Source {
    int token(int index);
  }

  private static final int START = -1;

  private static final int REGULAR = -2;

  private static final int MONOSPACE = -3;

  private static final int BOLD = -4;

  private static final int ITALIC = -5;

  private static final int CONSTRAINED_END = -6;

  private static final int LF = -7;

  private int[] context;

  private int contextIndex;

  private Source source;

  private int sourceIndex;

  private int sourceLast;

  private int[] text;

  private int textIndex;

  private int textCursor;

  private int tokenIndex;

  private boolean running;

  private boolean processQuotes;

  Pass2() {
    context = new int[16];

    text = new int[128];
  }

  public final void execute(Source source, int first, int last) {
    Check.state(
      !running,

      """
      Concurrent pass (2) is not supported.

      It seems a previous AsciiDoc document pass (2):

      - is currently running; or
      - finished abruptly (most likely due to a bug in this component, sorry...).
      """
    );

    running = true;

    contextIndex = -1;

    push(START);

    textIndex = 0;

    this.source = source;

    sourceIndex = first;

    sourceLast = last;

    execute0();

    textCursor = 0;
  }

  final boolean hasText() {
    return textCursor < textIndex;
  }

  final int nextText() {
    return text[textCursor++];
  }

  final void reset() {
    processQuotes = false;

    running = false;
  }

  final AttrValue toAttrValue(String source) {
    AttrValue res = AttrValue.EMPTY;

    if (hasText()) {
      var current = text[textCursor];
      var length = textIndex - textCursor;

      if (current == Text.REGULAR && length == 3) {
        nextText();
        var begin = nextText();
        var end = nextText();

        var s = source.substring(begin, end);

        res = AttrValue.string(s);
      } else {
        int[] copy = toText();

        res = AttrValue.text(copy, source);
      }
    }

    return res;
  }

  final int[] toText() {
    return Arrays.copyOf(text, textIndex);
  }

  private void addText(int t0) {
    text = IntArrays.growIfNecessary(text, textIndex);

    text[textIndex++] = t0;
  }

  private void addText(int t0, int t1, int t2) {
    text = IntArrays.growIfNecessary(text, textIndex + 2);

    text[textIndex++] = t0;
    text[textIndex++] = t1;
    text[textIndex++] = t2;
  }

  private void execute0() {
    loop: while (hasToken()) {
      tokenIndex = sourceIndex;

      var token = nextToken();

      switch (token) {
        case Token.BLOB -> executeBlob(nextToken(), nextToken());

        case Token.DQUOTE -> executeDquote(nextToken(), nextToken());

        case Token.EOF -> { break loop; }

        case Token.LF -> executeLf();

        case Token.BOLD_START -> executeBoldStart(nextToken());

        case Token.BOLD_END -> executeConstrainedEnd(nextToken(), BOLD, Text.BOLD_END);

        case Token.ITALIC_START -> executeItalicStart(nextToken());

        case Token.ITALIC_END -> executeConstrainedEnd(nextToken(), ITALIC, Text.ITALIC_END);

        case Token.MONO_START -> executeMonoStart(nextToken());

        case Token.MONO_END -> executeConstrainedEnd(nextToken(), MONOSPACE, Text.MONOSPACE_END);

        case Token.ATTR_VALUE_START, Token.ATTR_VALUE_END -> {}

        case Token.SEPARATOR -> executeBlob(nextToken(), nextToken());

        case Token.APOSTROPHE -> executeApostrophe(nextToken());

        default -> uoe(token);
      }
    }

    executeEof();
  }

  private void executeApostrophe(int index) {
    var ctx = pop();

    switch (ctx) {
      case REGULAR -> {
        addText(Text.CURVED_APOSTROPHE);

        var next = index + 1;

        addText(Text.REGULAR, next, next);

        push(ctx);
      }

      default -> uoe(ctx);
    }
  }

  private void executeBlob(int start, int end) {
    var ctx = pop();

    switch (ctx) {
      case START -> {
        addText(Text.REGULAR, start, end);

        push(ctx, REGULAR);
      }

      case REGULAR -> {
        text[textIndex - 1] = end;

        push(ctx);
      }

      case BOLD, ITALIC, MONOSPACE -> {
        addText(Text.REGULAR, start, end);

        push(ctx, REGULAR);
      }

      case CONSTRAINED_END -> {
        pop();

        addText(Text.REGULAR, start, end);

        push(REGULAR);
      }

      case LF -> {
        addText(Text.REGULAR, start - 1, end);

        push(REGULAR);
      }

      default -> uoe(ctx);
    }
  }

  private void executeBoldStart(int index) {
    var ctx = pop();

    switch (ctx) {
      case START -> {
        push(ctx, tokenIndex, textIndex, BOLD);

        addText(Text.BOLD_START);
      }

      case REGULAR -> {
        push(tokenIndex, textIndex, BOLD);

        addText(Text.BOLD_START);
      }

      default -> uoe(ctx);
    }
  }

  private void executeConstrainedEnd(int index, int expected, int text) {
    var ctx = pop();

    switch (ctx) {
      case REGULAR -> {}

      case CONSTRAINED_END -> {
        pop(); // index
      }

      default -> uoe(ctx);
    }

    var maybe = pop();

    if (maybe != expected) {
      addText(Text.REGULAR, index, index + 1);

      push(ctx, maybe);

      return;
    }

    pop(); // tokenIndex
    pop(); // textIndex

    addText(text);

    push(index + 1, CONSTRAINED_END);
  }

  private void executeDquote(int start, int end) {
    if (processQuotes) {
      throw new UnsupportedOperationException("Implement me");
    } else {
      executeBlob(start, end);
    }
  }

  private void executeEof() {
    var ctx = pop();

    switch (ctx) {
      case START -> {}

      case REGULAR -> {
        var start = pop();

        if (start != START) {
          throw new UnsupportedOperationException("Implement me");
        }
      }

      case CONSTRAINED_END -> {
        pop(); // index

        var start = pop();

        if (start != START) {
          throw new UnsupportedOperationException("Implement me :: start=" + start);
        }
      }

      default -> uoe(ctx);
    }
  }

  private void executeItalicStart(int index) {
    var ctx = pop();

    switch (ctx) {
      case START, BOLD -> push(ctx);

      case REGULAR -> {}

      default -> uoe(ctx);
    }

    push(tokenIndex, textIndex, ITALIC);

    addText(Text.ITALIC_START);
  }

  private void executeLf() {
    var ctx = pop();

    switch (ctx) {
      case START -> push(ctx, LF);

      case REGULAR -> {
        text[textIndex - 1]++;

        push(ctx);
      }

      case CONSTRAINED_END -> {
        var sourceMark = pop();

        addText(Text.REGULAR, sourceMark, sourceMark + 1);

        push(REGULAR);
      }

      default -> uoe(ctx);
    }
  }

  private void executeMonoStart(int index) {
    var ctx = pop();

    switch (ctx) {
      case START -> {
        push(ctx, tokenIndex, textIndex, MONOSPACE);

        addText(Text.MONOSPACE_START);
      }

      case REGULAR -> {
        push(tokenIndex, textIndex, MONOSPACE);

        addText(Text.MONOSPACE_START);
      }

      default -> uoe(ctx);
    }
  }

  private boolean hasToken() {
    return sourceIndex < sourceLast;
  }

  private int nextToken() {
    return source.token(sourceIndex++);
  }

  private int pop() {
    return context[contextIndex--];
  }

  private void push(int p0) {
    contextIndex++;

    context = IntArrays.growIfNecessary(context, contextIndex);

    context[contextIndex] = p0;
  }

  private void push(int p0, int p1) {
    context = IntArrays.growIfNecessary(context, contextIndex + 2);

    context[++contextIndex] = p0;
    context[++contextIndex] = p1;
  }

  private void push(int p0, int p1, int p2) {
    context = IntArrays.growIfNecessary(context, contextIndex + 3);

    context[++contextIndex] = p0;
    context[++contextIndex] = p1;
    context[++contextIndex] = p2;
  }

  private void push(int p0, int p1, int p2, int p3) {
    context = IntArrays.growIfNecessary(context, contextIndex + 4);

    context[++contextIndex] = p0;
    context[++contextIndex] = p1;
    context[++contextIndex] = p2;
    context[++contextIndex] = p3;
  }

  private int uoe(int ctx) {
    throw new UnsupportedOperationException("Implement me :: ctx=" + ctx);
  }

}