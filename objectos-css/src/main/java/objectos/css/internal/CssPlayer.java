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
package objectos.css.internal;

import objectos.css.pseudom.StyleSheetProcessor;
import objectos.util.IntArrays;

public class CssPlayer extends CssRecorder {

  private interface Context {
    int START = -1;
    int STYLE_SHEET = -2;
    int STYLE_SHEET_RULES = -3;
  }

  @FunctionalInterface
  private interface PseudoFactory<T> {
    T create(CssPlayer player);
  }

  public CssPlayer() {
  }

  protected final void executePlayer(StyleSheetProcessor processor) {
    throw new UnsupportedOperationException("Implement me");
  }

  final void executePlayerBefore() {
    // sets protoIndex to ROOT START
    protoIndex = protoArray[--protoIndex];

    // listArray is a proper stack
    listIndex = -1;

    ctxPush(Context.START);
  }

  final PseudoStyleSheet pseudoStyleSheet() {
    return pseudoFactory(PSTYLE_SHEET, PseudoStyleSheet::new);
  }

  final boolean styleSheetHasNext() {
    styleSheetHasNextCheck();

    var result = false;

    loop: while (protoMore()) {
      int proto = protoPeek();

      switch (proto) {
        case ByteProto.RULE -> {
          result = true;

          break loop;
        }

        case ByteProto.ROOT -> protoNext();

        case ByteProto.ROOT_END -> {
          protoNext();

          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    proto2ctx();

    return result;
  }

  final void styleSheetIterable() {
    ctxCheck(Context.START);

    ctxSet(0, Context.STYLE_SHEET);
  }

  final void styleSheetIterator() {
    ctxCheck(Context.STYLE_SHEET);

    ctxPush(protoIndex, Context.STYLE_SHEET_RULES);
  }

  private void ctx2proto() {
    protoIndex = ctxPeek(1);
  }

  private void ctxCheck(int expected) {
    int state = ctxPeek();

    ctxThrow(state, expected);
  }

  private int ctxPeek() {
    return listArray[listIndex];
  }

  private int ctxPeek(int offset) {
    return listArray[listIndex - offset];
  }

  private void ctxPush(int v0) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 1);
    listArray[++listIndex] = v0;
  }

  private void ctxPush(int v0, int v1) {
    listArray = IntArrays.growIfNecessary(listArray, listIndex + 2);
    listArray[++listIndex] = v0;
    listArray[++listIndex] = v1;
  }

  private void ctxSet(int offset, int value) {
    listArray[listIndex - offset] = value;
  }

  private void ctxThrow(int actual, int expected) {
    if (actual != expected) {
      throw new IllegalStateException(
        """
      Found state '%d' but expected state '%d'
      """.formatted(actual, expected)
      );
    }
  }

  private void proto2ctx() {
    ctxSet(1, protoIndex);
  }

  private boolean protoMore() {
    return protoIndex < protoArray.length;
  }

  private int protoNext() {
    return protoArray[protoIndex++];
  }

  private int protoPeek() {
    return protoArray[protoIndex];
  }

  @SuppressWarnings("unchecked")
  private <T> T pseudoFactory(int index, PseudoFactory<T> factory) {
    if (objectArray[index] == null) {
      objectArray[index] = factory.create(this);
    }

    return (T) objectArray[index];
  }

  private void styleSheetHasNextCheck() {
    int peek = ctxPeek();

    switch (peek) {
      case Context.STYLE_SHEET_RULES -> ctx2proto();

      default -> ctxThrow(peek, Context.STYLE_SHEET_RULES);
    }
  }

}