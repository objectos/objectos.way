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

  private enum Parse {
    STOP,
    BLOB,
    TEXT,
    EOL;
  }

  private static final int _SINGLE_LINE = 1 << 0;

  private static final int PSEUDO_DOCUMENT = 0;
  private static final int PSEUDO_HEADER = 1;
  private static final int PSEUDO_HEADING = 2;
  private static final int PSEUDO_PARAGRAPH = 4;
  private static final int PSEUDO_TEXT = 6;
  private static final int PSEUDO_LENGTH = 7;

  private int flags;

  private final Object[] pseudoArray = new Object[PSEUDO_LENGTH];

  Node nextNode;

  private CharSequence source;

  private int sourceIndex;

  private int[] stackArray = new int[8];

  private int stackIndex = -1;

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

  final Node nextNode() {
    var result = nextNode;

    nextNode = null;

    // change state to next adjacent state
    // e.g. HEADING -> HEADING_CONSUMED
    stackDec();

    return result;
  }

  final void parseTextHeading() {
    flagsSet(_SINGLE_LINE);

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

  private Parse advance(Parse nextState) {
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
    var state = Parse.BLOB;

    stackPush(sourceIndex);

    while (state != Parse.STOP) {
      state = switch (state) {
        case BLOB -> parseBlob();

        case EOL -> parseEol();

        case TEXT -> parseText();

        default -> throw new UnsupportedOperationException(
          "Implement me :: state=" + state
        );
      };
    }
  }

  private Parse parseText() {
    int endIndex = stackPop();

    int startIndex = stackPop();

    if (startIndex < endIndex) {
      var text = pseudoText();

      text.end = endIndex;

      text.start = startIndex;

      nextNode = text;
    } else {
      nextNode = null;
    }

    return Parse.STOP;
  }

  private Parse parseBlob() {
    if (!sourceMore()) {
      stackPush(sourceIndex);

      return Parse.TEXT;
    }

    return switch (sourcePeek()) {
      case '\n' -> advance(Parse.EOL);

      case '`' -> throw new UnsupportedOperationException("Implement me");

      case '*' -> throw new UnsupportedOperationException("Implement me");

      case '_' -> throw new UnsupportedOperationException("Implement me");

      default -> advance(Parse.BLOB);
    };
  }

  private Parse parseEol() {
    if (flagsIs(_SINGLE_LINE)) {
      // end before NL
      stackPush(sourceIndex - 1);

      return advance(Parse.TEXT);
    }

    if (!sourceMore()) {
      // end before NL
      stackPush(sourceIndex - 1);

      return advance(Parse.TEXT);
    }

    return switch (sourcePeek()) {
      case '\n' -> {
        // end before NL
        stackPush(sourceIndex);

        yield advance(Parse.TEXT);
      }

      default -> advance(Parse.BLOB);
    };
  }

  private void start(CharSequence source) {
    this.source = source;

    sourceIndex = 0;

    stackIndex = -1;
  }

}