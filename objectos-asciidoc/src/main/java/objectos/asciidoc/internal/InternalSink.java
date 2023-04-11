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
import objectos.asciidoc.pseudom.Document;
import objectos.asciidoc.pseudom.Node;
import objectos.lang.Check;
import objectos.util.IntArrays;

public class InternalSink {

  /*
  
  CC_WORD = CG_WORD = '\p{Word}'
  QuoteAttributeListRxt = %(\\[([^\\[\\]]+)\\])
  %(\[([^\[\]]+)\])
  CC_ALL = '.'
  
  [:strong, :constrained, /(^|[^#{CC_WORD};:}])(?:#{QuoteAttributeListRxt})?\*(\S|\S#{CC_ALL}*?\S)\*(?!#{CG_WORD})/m]
  
  /./m - Any character (the m modifier enables multiline mode)
  /\S/ - A non-whitespace character: /[^ \t\r\n\f\v]/
  
   */

  private static final int _HEADING = 1 << 0;

  private static final int PSEUDO_DOCUMENT = 0;
  private static final int PSEUDO_HEADER = 1;
  private static final int PSEUDO_HEADING = 2;
  private static final int PSEUDO_NO_HEADER = 3;
  private static final int PSEUDO_PARAGRAPH = 4;
  private static final int PSEUDO_TEXT = 5;
  private static final int PSEUDO_LENGTH = 6;

  private int flags;

  private final Object[] pseudoArray = new Object[PSEUDO_LENGTH];

  Node nextNode;

  private CharSequence source;

  private int sourceIndex;

  private int[] stackArray = new int[8];

  private int stackIndex = -1;

  private int[] textArray = new int[128];

  private int textCursor;

  private int textIndex;

  protected final Document openImpl(CharSequence source) {
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

    var document = pseudoDocument();

    document.start();

    return document;
  }

  final void appendTo(Appendable out, int start, int end) throws IOException {
    out.append(source, start, end);
  }

  final void close() throws IOException {
    nextNode = null;

    source = null;

    stackIndex = -1;
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
      case Text.REGULAR -> {
        var text = pseudoText();

        text.start = textNext();

        text.end = textNext();

        stackDec();

        yield text;
      }

      default -> textStub(type);
    };
  }

  final void parseTextHeading() {
    flagsSet(_HEADING);

    parse();
  }

  final void parseTextRegular() {
    flags = 0;

    parse();
  }

  final PseudoDocument pseudoDocument() {
    var result = pseudoArray[PSEUDO_DOCUMENT];

    if (result == null) {
      result = pseudoArray[PSEUDO_DOCUMENT] = new PseudoDocument(this);
    }

    return (PseudoDocument) result;
  }

  final PseudoHeader pseudoHeader() {
    var result = pseudoArray[PSEUDO_HEADER];

    if (result == null) {
      result = pseudoArray[PSEUDO_HEADER] = new PseudoHeader(this);
    }

    return (PseudoHeader) result;
  }

  final PseudoHeading pseudoHeading() {
    var result = pseudoArray[PSEUDO_HEADING];

    if (result == null) {
      result = pseudoArray[PSEUDO_HEADING] = new PseudoHeading(this);
    }

    return (PseudoHeading) result;
  }

  final PseudoNoHeader pseudoNoHeader() {
    var result = pseudoArray[PSEUDO_NO_HEADER];

    if (result == null) {
      result = pseudoArray[PSEUDO_NO_HEADER] = new PseudoNoHeader(this);
    }

    return (PseudoNoHeader) result;
  }

  final PseudoParagraph pseudoParagraph() {
    var result = pseudoArray[PSEUDO_PARAGRAPH];

    if (result == null) {
      result = pseudoArray[PSEUDO_PARAGRAPH] = new PseudoParagraph(this);
    }

    return (PseudoParagraph) result;
  }

  final PseudoText pseudoText() {
    var result = pseudoArray[PSEUDO_TEXT];

    if (result == null) {
      result = pseudoArray[PSEUDO_TEXT] = new PseudoText(this);
    }

    return (PseudoText) result;
  }

  final void sourceAdvance() {
    sourceIndex++;
  }

  final int sourceIndex() {
    return sourceIndex;
  }

  final void sourceIndex(int value) {
    sourceIndex = value;
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

  private boolean finalState() {
    return stackIndex == -1;
  }

  private boolean flagsIs(int value) {
    return (flags & value) != 0;
  }

  private void flagsSet(int value) {
    flags |= value;
  }

  @SuppressWarnings("unused")
  private void flagsUnset(int value) {
    flags &= ~value;
  }

  @SuppressWarnings("unused")
  private boolean isWord(char c) {
    int type = Character.getType(c);

    return switch (type) {
      case Character.LOWERCASE_LETTER,
           Character.MODIFIER_LETTER,
           Character.OTHER_LETTER,
           Character.TITLECASE_LETTER,
           Character.UPPERCASE_LETTER,

           Character.NON_SPACING_MARK,
           Character.COMBINING_SPACING_MARK,
           Character.ENCLOSING_MARK,

           Character.DECIMAL_DIGIT_NUMBER,
           Character.LETTER_NUMBER,
           Character.OTHER_NUMBER,

           Character.CONNECTOR_PUNCTUATION -> true;

      default -> false;
    };
  }

  private void parse() {
    textCursor = textIndex = 0;

    tokenAdd(Token.START, sourceIndex);

    int state = Parse.BLOB;

    while (state != Parse.STOP) {
      state = switch (state) {
        case Parse.BLOB -> parseBlob();

        case Parse.EOL -> parseEol();

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }

    process();
  }

  private int parseBlob() {
    if (!sourceMore()) {
      tokenAdd(Token.EOF, sourceIndex);

      return Parse.STOP;
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(Parse.EOL);

      case '`' -> throw new UnsupportedOperationException("Implement me");

      case '*' -> throw new UnsupportedOperationException("Implement me");

      case '_' -> throw new UnsupportedOperationException("Implement me");

      default -> advance(Parse.BLOB);
    };
  }

  private int parseEol() {
    if (flagsIs(_HEADING)) {
      tokenAdd(Token.EOF, sourceIndex - 1);

      return Parse.STOP;
    }

    if (!sourceMore()) {
      tokenAdd(Token.EOF, sourceIndex - 1);

      return Parse.STOP;
    }

    return switch (sourcePeek()) {
      case '\n' -> throw new UnsupportedOperationException("Implement me");

      default -> Parse.BLOB;
    };
  }

  private void process() {
    int max = textIndex;

    int state = Process.START;

    while (state != Process.STOP) {
      int token = tokenNext();

      state = switch (token) {
        case Token.EOF -> processEof(state);

        case Token.START -> processStart(state);

        default -> throw new UnsupportedOperationException(
          "Implement me :: token=" + token
        );
      };
    }

    textCursor = max;
  }

  private int processEof(int state) {
    int value = tokenNext();

    return switch (state) {
      case Process.TEXT_START -> {
        textAdd(value);

        yield Process.STOP;
      }

      default -> throw new UnsupportedOperationException(
        "Implement me :: state=" + state
      );
    };
  }

  private int processStart(int state) {
    int value = tokenNext();

    return switch (state) {
      case Process.START -> {
        textAdd(Text.REGULAR, value);

        yield Process.TEXT_START;
      }

      default -> throw new UnsupportedOperationException(
        "Implement me :: state=" + state
      );
    };
  }

  private void start(CharSequence source) {
    this.source = source;

    sourceIndex = 0;

    stackIndex = -1;
  }

  private void textAdd(int v0) {
    textArray = IntArrays.growIfNecessary(textArray, textIndex + 0);
    textArray[textIndex++] = v0;
  }

  private void textAdd(int v0, int v1) {
    textArray = IntArrays.growIfNecessary(textArray, textIndex + 1);
    textArray[textIndex++] = v0;
    textArray[textIndex++] = v1;
  }

  private int textNext() {
    return textArray[textCursor++];
  }

  private Node textStub(int type) {
    throw new UnsupportedOperationException(
      "Implement me :: type=" + type
    );
  }

  private void tokenAdd(int v0, int v1) {
    textArray = IntArrays.growIfNecessary(textArray, textIndex + 1);
    textArray[textIndex++] = v0;
    textArray[textIndex++] = v1;
  }

  private int tokenNext() {
    return textArray[textCursor++];
  }

}