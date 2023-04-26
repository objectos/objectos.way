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
import java.util.function.Function;
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

  private static final int PSEUDO_DOCUMENT = 0;
  private static final int PSEUDO_HEADER = 1;
  private static final int PSEUDO_TITLE = 2;
  private static final int PSEUDO_PARAGRAPH = 3;
  private static final int PSEUDO_SECTION = 4;
  private static final int PSEUDO_TEXT = 5;
  private static final int PSEUDO_ULIST = 6;
  private static final int PSEUDO_ATTRIBUTES = 7;
  private static final int PSEUDO_INLINE_MACRO = 8;
  private static final int PSEUDO_LENGTH = 9;

  private final Object[] pseudoArray = new Object[PSEUDO_LENGTH];

  Node nextNode;

  private String source;

  private int sourceIndex;

  private int[] stackArray = new int[8];

  private int stackIndex = -1;

  protected final Document openImpl(String source) {
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

  final PseudoAttributes pseudoAttributes() {
    return pseudoFactory(PSEUDO_ATTRIBUTES, PseudoAttributes::new);
  }

  final PseudoDocument pseudoDocument() {
    return pseudoFactory(PSEUDO_DOCUMENT, PseudoDocument::new);
  }

  final PseudoHeader pseudoHeader() {
    return pseudoFactory(PSEUDO_HEADER, PseudoHeader::new);
  }

  final PseudoInlineMacro pseudoInlineMacro() {
    return pseudoFactory(PSEUDO_INLINE_MACRO, PseudoInlineMacro::new);
  }

  final PseudoParagraph pseudoParagraph() {
    return pseudoFactory(PSEUDO_PARAGRAPH, PseudoParagraph::new);
  }

  final PseudoSection pseudoSection() {
    return pseudoFactory(PSEUDO_SECTION, PseudoSection::new);
  }

  final PseudoText pseudoText() {
    return pseudoFactory(PSEUDO_TEXT, PseudoText::new);
  }

  final PseudoTitle pseudoTitle() {
    return pseudoFactory(PSEUDO_TITLE, PseudoTitle::new);
  }

  final PseudoUnorderedList pseudoUnorderedList() {
    return pseudoFactory(PSEUDO_ULIST, PseudoUnorderedList::new);
  }

  final void sourceAdvance() {
    sourceIndex++;
  }

  final char sourceAt(int index) {
    return source.charAt(index);
  }

  final String sourceGet(int start, int end) {
    return source.substring(start, end);
  }

  final boolean sourceInc() {
    sourceAdvance();

    return sourceMore();
  }

  final int sourceIndex() {
    return sourceIndex;
  }

  final void sourceIndex(int value) {
    sourceIndex = value;
  }

  final boolean sourceMatches(int sourceOffset, String other) {
    return source.regionMatches(sourceOffset, other, 0, other.length());
  }

  final boolean sourceMore() {
    return sourceIndex < source.length();
  }

  final char sourceNext() {
    return source.charAt(sourceIndex++);
  }

  final char sourcePeek() {
    return source.charAt(sourceIndex);
  }

  final char sourcePeek(int offset) {
    return source.charAt(sourceIndex + offset);
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

  final int stackPeek(int offset) {
    return stackArray[stackIndex - offset];
  }

  final int stackPop() {
    return stackArray[stackIndex--];
  }

  final void stackPop(int count) {
    stackIndex -= count;
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

  private boolean finalState() {
    return stackIndex == -1;
  }

  @SuppressWarnings("unchecked")
  private <T> T pseudoFactory(int index, Function<InternalSink, T> factory) {
    var result = pseudoArray[index];

    if (result == null) {
      result = pseudoArray[index] = factory.apply(this);
    }

    return (T) result;
  }

  private void start(String source) {
    this.source = source;

    sourceIndex = 0;

    stackIndex = -1;
  }

}