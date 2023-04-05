/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.asciidoc.internal;

import java.io.IOException;
import java.io.Reader;
import objectos.asciidoc.pseudom.Document.Processor;
import objectos.asciidoc.pseudom.Node;
import objectos.lang.Check;
import objectos.util.IntArrays;

public class InternalSink {

  private static final int _START = 0,
      _DOCUMENT_COMPUTED = 1;

  private final PseudoDocument document = new PseudoDocument(this);

  private final PseudoHeading heading = new PseudoHeading(this);

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

  final boolean documentHasNext() throws IOException {
    int ctx = stackPeek();

    if (ctx == _START) {
      parse();

      stackSet(0, _DOCUMENT_COMPUTED);
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: ctx=" + ctx
      );
    }

    return nextNode != null;
  }

  final int stackPeek() {
    return stackArray[stackIndex];
  }

  private void end() {
    stackIndex = -1;
  }

  private boolean finalState() {
    return stackIndex == -1;
  }

  private boolean isSpaceLike(char c) {
    return c == ' ';
  }

  private void parse() throws IOException {
    stackPush(
      sourceIndex // 0 - source start
    );

    int state = Parse.START;

    while (state != Parse.STOP) {
      state = switch (state) {
        case Parse.CONTENTS -> parseContents();

        case Parse.HEADING -> parseHeading();

        case Parse.MAYBE_HEADING -> parseMaybeHeading();

        case Parse.START -> parseStart();

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }
  }

  private int parseContents() {
    return null;
  }

  private int parseHeading() {
    heading.level = stackPop();

    nextNode = heading;

    stackPush(sourceIndex);

    return Parse.CONTENTS;
  }

  private int parseMaybeHeading() throws IOException {
    if (sourceMore()) {
      var c = sourcePeek();

      if (c == '=') {
        // inc. heading level
        stackInc(0);
        sourceIndex++;
        return Parse.MAYBE_HEADING;
      }

      if (isSpaceLike(c)) {
        sourceIndex++;
        return Parse.MAYBE_HEADING;
      }

      return Parse.HEADING;
    } else {
      // pops heading level
      stackPop(1);

      throw new UnsupportedOperationException(
        "Implement me :: regular text"
      );
    }
  }

  private int parseStart() throws IOException {
    if (sourceMore()) {
      var c = sourceNext();

      return switch (c) {
        case '=' -> {
          // pushes heading level
          stackPush(0);

          yield Parse.MAYBE_HEADING;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: c=" + c
        );
      };
    } else {
      return Parse.STOP;
    }
  }

  private boolean sourceMore() throws IOException {
    if (sourceIndex == sourceLength) {
      sourceLength = reader.read(source);
    }

    return sourceIndex < sourceLength;
  }

  private char sourceNext() {
    return source[sourceIndex++];
  }

  private char sourcePeek() {
    return source[sourceIndex];
  }

  private void stackInc(int offset) {
    stackArray[stackIndex - offset]++;
  }

  private void stackIs(int expected) {
    int actual = stackPeek();

    if (actual != expected) {
      stateThrow(actual, expected);
    }
  }

  private int stackPeek(int offset) {
    return stackArray[stackIndex - offset];
  }

  private int stackPop() {
    return stackArray[stackIndex];
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

  private void start(Reader reader) {
    this.reader = reader;

    sourceIndex = 0;

    sourceLength = 0;

    stackIndex = -1;
  }

}