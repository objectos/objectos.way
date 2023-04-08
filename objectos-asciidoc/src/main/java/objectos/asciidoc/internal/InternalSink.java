/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.asciidoc.internal;

import java.io.IOException;
import objectos.asciidoc.pseudom.Document.Processor;
import objectos.asciidoc.pseudom.Node;
import objectos.lang.Check;
import objectos.util.IntArrays;

public class InternalSink {

  private final PseudoDocument document = new PseudoDocument(this);

  final PseudoHeader header = new PseudoHeader(this);

  final PseudoHeading heading = new PseudoHeading(this);

  private final PseudoText text = new PseudoText(this);

  Node nextNode;

  private CharSequence source;

  private int sourceIndex;

  private int[] stackArray = new int[8];

  private int stackIndex = -1;

  private int[] textArray = new int[128];

  private int textCursor;

  private int textIndex;

  protected final void toProcessorImpl(
      CharSequence source, Processor processor) throws IOException {
    Check.state(
      finalState(),

      """
      Concurrent processing is not supported.

      It seems a previous AsciiDoc document processing:

      - is currently running; or
      - finished abruptly (most likely due to a bug in this component, sorry...).
      """
    );

    start(source);

    try {
      document.start();

      processor.process(document);
    } finally {
      end();

      assert stackIndex == -1;
    }
  }

  final void appendTo(Appendable out, int start, int end) throws IOException {
    out.append(source, start, end);
  }

  final boolean hasNextText() {
    return textCursor < textIndex;
  }

  final Node nextNode() {
    var result = nextNode;

    nextNode = null;

    // change state to next adjacent state
    // e.g. HEADING -> HEADING_CONSUMED
    stackDec();

    return result;
  }

  final Node nextText() {
    int type = textNext();

    return switch (type) {
      case Contents.TEXT -> {
        text.start = textNext();

        text.end = textNext();

        stackDec();

        yield text;
      }

      default -> textStub(type);
    };
  }

  final void parseText(int initialState, boolean singleLine) {
    textIndex = 0;

    stackPush(Text.START);

    int state = initialState;

    while (state != ParseTxt.STOP) {
      state = switch (state) {
        case ParseTxt.BLOB -> parseBlob();

        case ParseTxt.EOF -> parseEof();

        case ParseTxt.START_LIKE -> parseStartLike();

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }
  }

  final void sourceAdvance() {
    sourceIndex++;
  }

  final boolean sourceMore() {
    return sourceIndex < source.length();
  }

  final char sourcePeek() {
    return source.charAt(sourceIndex);
  }

  final int sourceStub() {
    var c = sourcePeek();

    throw new UnsupportedOperationException(
      "Implement me :: char=" + c
    );
  }

  final void stackDec() {
    stackArray[stackIndex]--;
  }

  final void stackInc() {
    stackArray[stackIndex]++;
  }

  final int stackPeek() {
    return stackArray[stackIndex];
  }

  final int stackPop() {
    return stackArray[stackIndex--];
  }

  final void stackPush(int v0) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 1);
    stackArray[++stackIndex] = v0;
  }

  final void stackPush(int v0, int v1) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 2);
    stackArray[++stackIndex] = v0;
    stackArray[++stackIndex] = v1;
  }

  final void stackReplace(int value) {
    stackArray[stackIndex] = value;
  }

  final void stackStub() {
    int ctx = stackPeek();

    throw new UnsupportedOperationException(
      "Implement me :: ctx=" + ctx
    );
  }

  private int advance(int nextState) {
    sourceIndex++;

    return nextState;
  }

  private int textNext() {
    return textArray[textCursor++];
  }

  private Node textStub(int type) {
    throw new UnsupportedOperationException(
      "Implement me :: type=" + type
    );
  }

  private void end() {
    stackIndex = -1;
  }

  private boolean finalState() {
    return stackIndex == -1;
  }

  private int parseBlob() {
    if (!sourceMore()) {
      return ParseTxt.EOF;
    }

    return switch (sourcePeek()) {
      default -> advance(ParseTxt.BLOB);
    };
  }

  private int parseEof() {
    loop: while (true) {
      switch (stackPop()) {
        case Text.BLOB -> {
          int start = stackPop();

          int end = sourceIndex;

          textAdd(Contents.TEXT, start, end);
        }

        case Text.START -> {
          break loop;
        }
      }
    }

    return ParseTxt.STOP;
  }

  private int parseStartLike() {
    if (!sourceMore()) {
      throw new UnsupportedOperationException(
        "Implement me"
      );
    }

    return switch (sourcePeek()) {
      case '`' -> throw new UnsupportedOperationException("Implement me");

      case '*' -> throw new UnsupportedOperationException("Implement me");

      case '_' -> throw new UnsupportedOperationException("Implement me");

      default -> {
        stackPush(sourceIndex, Text.BLOB);

        yield advance(ParseTxt.BLOB);
      }
    };
  }

  private void start(CharSequence source) {
    this.source = source;

    sourceIndex = 0;

    stackIndex = -1;
  }

  private void textAdd(int v0, int v1, int v2) {
    textArray = IntArrays.growIfNecessary(textArray, textIndex + 2);
    textArray[textIndex++] = v0;
    textArray[textIndex++] = v1;
    textArray[textIndex++] = v2;
  }

}