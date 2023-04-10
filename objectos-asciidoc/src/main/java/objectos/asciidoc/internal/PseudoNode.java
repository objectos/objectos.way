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

import java.util.NoSuchElementException;
import objectos.asciidoc.pseudom.Node;

abstract class PseudoNode {

  private final InternalSink sink;

  PseudoNode(InternalSink sink) {
    this.sink = sink;
  }

  public abstract boolean hasNext();

  final int advance(int state) {
    sink.sourceAdvance();

    return state;
  }

  final boolean hasNextNode() {
    return sink.nextNode != null;
  }

  final boolean hasNextText() {
    return sink.hasNextText();
  }

  final PseudoHeader header() {
    return sink.pseudoHeader();
  }

  final PseudoNoHeader noHeader() {
    return sink.pseudoNoHeader();
  }

  final PseudoParagraph paragraph() {
    return sink.pseudoParagraph();
  }

  final PseudoHeading heading() {
    return sink.pseudoHeading();
  }

  final Node nextNode() {
    if (hasNext()) {
      return sink.nextNode();
    } else {
      throw new NoSuchElementException();
    }
  }

  final Node nextText() {
    if (hasNext()) {
      return sink.nextText();
    } else {
      throw new NoSuchElementException();
    }
  }

  final void nextNode(Node value) {
    sink.nextNode = value;
  }

  final void parseText(int initialState, boolean singleLine) {
    sink.parseText(initialState, singleLine);
  }

  final int sourceIndex() {
    return sink.sourceIndex();
  }

  final boolean sourceMore() {
    return sink.sourceMore();
  }

  final void sourceIndex(int value) {
    sink.sourceIndex(value);
  }

  final char sourcePeek() {
    return sink.sourcePeek();
  }

  final int sourceStub() {
    return sink.sourceStub();
  }

  final void stackAssert(int expected) {
    int actual = stackPeek();

    assert actual == expected : "actual=" + actual + ";expected=" + expected;
  }

  final void stackDec() {
    sink.stackDec();
  }

  final void stackInc() {
    sink.stackInc();
  }

  final int stackPeek() {
    return sink.stackPeek();
  }

  final int stackPop() {
    return sink.stackPop();
  }

  final void stackPush(int v0) {
    sink.stackPush(v0);
  }

  final void stackPush(int v0, int v1) {
    sink.stackPush(v0, v1);
  }

  final void stackReplace(int value) {
    sink.stackReplace(value);
  }

  final int stackStub() {
    sink.stackStub();

    return Integer.MIN_VALUE;
  }

  final boolean stackStubBool() {
    sink.stackStub();

    return false;
  }

}