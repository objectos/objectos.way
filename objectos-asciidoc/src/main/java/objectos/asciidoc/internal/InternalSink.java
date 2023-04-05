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
import java.io.Reader;
import java.util.NoSuchElementException;
import objectos.asciidoc.pseudom.Document.Processor;
import objectos.asciidoc.pseudom.Node;
import objectos.lang.Check;
import objectos.util.IntArrays;

public class InternalSink {

  private static final int _START = 0,
      _DOCUMENT_COMPUTED = 1,
      _DOCUMENT_NEXT = 2,
      _HEADING_COMPUTED = 3;

  private final PseudoDocument document = new PseudoDocument(this);

  private final PseudoHeading heading = new PseudoHeading(this);

  private final PseudoText text = new PseudoText(this);

  private int[] contentsArray = new int[128];

  private int contentsCursor;

  private int contentsIndex;

  private Node nextNode;

  private Reader reader;

  private final char[] source = new char[1024];

  private int sourceIndex;

  private int sourceLength;

  private int[] stackArray = new int[8];

  private int stackIndex = -1;

  protected final void toProcessorImpl(Reader reader, Processor processor) throws IOException {
    Check.state(
      finalState(),

      """
      Concurrent processing is not supported.

      It seems a previous AsciiDoc document processing:

      - is currently running; or
      - finished abruptly (most likely due to a bug in this component, sorry...).
      """
    );

    start(reader);

    stackPush(_START);

    try {
      processor.process(document);
    } finally {
      end();
    }
  }

  final void appendTo(Appendable out, int start, int end) throws IOException {
    for (int i = start; i < end; i++) {
      char c = source[i];

      out.append(c);
    }
  }

  final boolean documentHasNext() throws IOException {
    switch (stackPeek()) {
      case _HEADING_COMPUTED:
        stackPop();

        // fall-through
      case _START:
      case _DOCUMENT_NEXT:
        parse();

        stackSet(0, _DOCUMENT_COMPUTED);

        break;

      case _DOCUMENT_COMPUTED:
        // noop
        break;

      default:
        stackStub();
        break;
    }

    return nextNode != null;
  }

  final Node documentNext() throws IOException {
    if (documentHasNext()) {
      var result = nextNode;

      stackSet(0, _DOCUMENT_NEXT);

      nextNode = null;

      return result;
    } else {
      throw new NoSuchElementException();
    }
  }

  final boolean headingHasNext() {
    switch (stackPeek()) {
      case _DOCUMENT_NEXT -> {
        contentsCursor = 0;

        stackPush(_HEADING_COMPUTED);
      }

      case _HEADING_COMPUTED -> {}

      default -> stackStub();
    }

    return contentsCursor < contentsIndex;
  }

  final Node headingNext() {
    if (headingHasNext()) {
      int type = contentsNext();

      return switch (type) {
        case Contents.TEXT -> {
          text.start = contentsNext();

          text.end = contentsNext();

          yield text;
        }

        default -> contentsStub(type);
      };
    } else {
      throw new NoSuchElementException();
    }
  }

  final int stackPeek() {
    return stackArray[stackIndex];
  }

  private int advance(int nextState) {
    sourceIndex++;

    return nextState;
  }

  private void contentsAdd(int v0, int v1, int v2) {
    contentsArray = IntArrays.growIfNecessary(contentsArray, contentsIndex + 2);
    contentsArray[contentsIndex++] = v0;
    contentsArray[contentsIndex++] = v1;
    contentsArray[contentsIndex++] = v2;
  }

  private int contentsNext() {
    return contentsArray[contentsCursor++];
  }

  private Node contentsStub(int type) {
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

  private void parse() throws IOException {
    contentsIndex = 0;

    int stackStart = stackIndex;

    int state = Parse.START;

    while (state != Parse.STOP) {
      state = switch (state) {
        case Parse.CONTENTS -> parseContents();

        case Parse.CONTENTS_EOF -> parseContentsEof();

        case Parse.HEADING -> parseHeading();

        case Parse.MAYBE_HEADING -> parseMaybeHeading();

        case Parse.MAYBE_HEADING_TRIM -> parseMaybeHeadingTrim();

        case Parse.START -> parseStart();

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }

    assert stackIndex == stackStart : "stackStart=" + stackStart + ";stackIndex=" + stackIndex;
  }

  private int parseContents() throws IOException {
    if (!sourceMore()) {
      return Parse.CONTENTS_EOF;
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(Parse.CONTENTS_NL);

      case '`' -> throw new UnsupportedOperationException("Implement me");

      case '*' -> throw new UnsupportedOperationException("Implement me");

      case '_' -> throw new UnsupportedOperationException("Implement me");

      default -> advance(Parse.CONTENTS);
    };
  }

  private int parseContentsEof() {
    loop: while (!stackIsEmpty()) {
      switch (stackPeek()) {
        case Ctx.HEADING -> {
          stackPop();

          int sourceStart = stackPop();

          contentsAdd(Contents.TEXT, sourceStart, sourceIndex);

          break loop;
        }

        default -> stackStub();
      }
    }

    return Parse.STOP;
  }

  private int parseHeading() {
    heading.level = stackPop();

    nextNode = heading;

    stackPush(sourceIndex, Ctx.HEADING);

    return Parse.CONTENTS;
  }

  private int parseMaybeHeading() throws IOException {
    if (!sourceMore()) {
      return rollbackMaybeHeading();
    }

    return switch (sourcePeek()) {
      case '=' -> {
        // increase the heading level
        stackInc(0);

        yield advance(Parse.MAYBE_HEADING);
      }

      case '\t', '\f', ' ' -> advance(Parse.MAYBE_HEADING_TRIM);

      default -> rollbackMaybeHeading();
    };
  }

  private int parseMaybeHeadingTrim() throws IOException {
    if (!sourceMore()) {
      return rollbackMaybeHeading();
    }

    return switch (sourcePeek()) {
      case '\t', '\f', ' ' -> advance(Parse.MAYBE_HEADING_TRIM);

      default -> Parse.HEADING;
    };
  }

  private int parseStart() throws IOException {
    if (!sourceMore()) {
      // empty document...
      // just stop

      return Parse.STOP;
    }

    return switch (sourcePeek()) {
      case '=' -> {
        // pushes heading level
        stackPush(0);

        yield advance(Parse.MAYBE_HEADING);
      }

      default -> sourceStub();
    };
  }

  private int rollbackMaybeHeading() {
    // pops heading level
    stackPop(1);

    throw new UnsupportedOperationException(
      "Implement me :: rollback to Parse.PARAGRAPH"
    );
  }

  private boolean sourceMore() throws IOException {
    if (sourceIndex == sourceLength) {
      sourceLength = reader.read(source);
    }

    return sourceIndex < sourceLength;
  }

  private char sourcePeek() {
    return source[sourceIndex];
  }

  private int sourceStub() {
    var c = sourcePeek();

    throw new UnsupportedOperationException(
      "Implement me :: char=" + c
    );
  }

  private void stackInc(int offset) {
    stackArray[stackIndex - offset]++;
  }

  private boolean stackIsEmpty() {
    return stackIndex < 0;
  }

  private int stackPop() {
    return stackArray[stackIndex--];
  }

  private void stackPop(int count) {
    stackIndex -= count;
  }

  private void stackPush(int v0) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 1);
    stackArray[++stackIndex] = v0;
  }

  private void stackPush(int v0, int v1) {
    stackArray = IntArrays.growIfNecessary(stackArray, stackIndex + 2);
    stackArray[++stackIndex] = v0;
    stackArray[++stackIndex] = v1;
  }

  private void stackSet(int offset, int value) {
    stackArray[stackIndex - offset] = value;
  }

  private void stackStub() {
    int ctx = stackPeek();

    throw new UnsupportedOperationException(
      "Implement me :: ctx=" + ctx
    );
  }

  private void start(Reader reader) {
    this.reader = reader;

    sourceIndex = 0;

    sourceLength = 0;

    stackIndex = -1;
  }

}