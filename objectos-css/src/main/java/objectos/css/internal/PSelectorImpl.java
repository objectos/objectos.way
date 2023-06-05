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

import java.util.Iterator;
import java.util.NoSuchElementException;
import objectos.css.pseudom.IterableOnce;
import objectos.css.pseudom.PSelector;
import objectos.css.pseudom.PSelectorElement;

public final class PSelectorImpl
    implements PSelector, IterableOnce<PSelectorElement>, Iterator<PSelectorElement> {

  private static final int START = -1;
  private static final int ITERABLE = -2;
  private static final int ITERATOR = -3;
  private static final int NEXT = -4;
  private static final int NEXT_CONSUMED = -5;

  private final CssPlayer player;

  private int protoIndex;

  private int state;

  PSelectorImpl(CssPlayer player) {
    this.player = player;
  }

  @Override
  public final IterableOnce<PSelectorElement> elements() {
    state = player.cas(state, START, ITERABLE);

    return this;
  }

  @Override
  public final boolean hasNext() {
    switch (state) {
      case ITERATOR, NEXT_CONSUMED -> {}

      case NEXT -> {
        return true;
      }

      default -> throw new UnsupportedOperationException(
        "Implement me :: state=" + state
      );
    }

    return hasNext0();
  }

  @Override
  public final Iterator<PSelectorElement> iterator() {
    state = player.cas(state, ITERABLE, ITERATOR);

    return this;
  }

  @Override
  public final PSelectorElement next() {
    if (hasNext()) {
      return next0();
    } else {
      throw new NoSuchElementException();
    }
  }

  final PSelectorImpl init(int protoIndex) {
    this.protoIndex = protoIndex;

    state = START;

    return this;
  }

  private boolean hasNext0() {
    var found = false;

    loop: while (player.protoMore(protoIndex)) {
      int proto = player.protoGet(protoIndex);

      switch (proto) {
        case ByteProto.STYLE_RULE -> protoIndex += 2;

        case ByteProto.STYLE_RULE_END -> {
          break loop;
        }

        case ByteProto.ATTR_NAME_SELECTOR,
             ByteProto.ATTR_VALUE_SELECTOR,
             ByteProto.CLASS_SELECTOR,
             ByteProto.CLASS_SELECTOR_EXTERNAL,
             ByteProto.COMBINATOR,
             ByteProto.ID_SELECTOR,
             ByteProto.ID_SELECTOR_EXTERNAL,
             ByteProto.PSEUDO_CLASS_SELECTOR,
             ByteProto.PSEUDO_ELEMENT_SELECTOR,
             ByteProto.TYPE_SELECTOR -> {
          state = NEXT;

          found = true;

          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    return found;
  }

  private PSelectorElement next0() {
    state = player.cas(state, NEXT, NEXT_CONSUMED);

    int proto = player.protoGet(protoIndex++);

    return switch (proto) {
      case ByteProto.ATTR_NAME_SELECTOR -> nextAttributeNameSelector(
        player.protoGet(protoIndex++)
      );

      case ByteProto.ATTR_VALUE_SELECTOR -> nextAttributeValueSelector(
        player.protoGet(protoIndex++)
      );

      case ByteProto.CLASS_SELECTOR,
           ByteProto.CLASS_SELECTOR_EXTERNAL -> nextClassSelector(
             player.protoGet(protoIndex++)
           );

      case ByteProto.COMBINATOR -> nextCombinator(
        player.protoGet(protoIndex++)
      );

      case ByteProto.ID_SELECTOR,
           ByteProto.ID_SELECTOR_EXTERNAL -> nextIdSelector(
             player.protoGet(protoIndex++)
           );

      case ByteProto.PSEUDO_CLASS_SELECTOR -> nextPseudoClassSelector(
        player.protoGet(protoIndex++)
      );

      case ByteProto.PSEUDO_ELEMENT_SELECTOR -> nextPseudoElementSelector(
        player.protoGet(protoIndex++)
      );

      case ByteProto.TYPE_SELECTOR -> nextTypeSelector(
        player.protoGet(protoIndex++)
      );

      default -> throw new UnsupportedOperationException(
        "Implement me :: proto=" + proto
      );
    };
  }

  private PSelectorElement nextAttributeNameSelector(int index) {
    var impl = player.pseudoAttributeNameSelector();

    // skips MARKER, end index
    impl.nameIndex = player.protoGet(index + 2);

    return impl;
  }

  private PSelectorElement nextAttributeValueSelector(int index) {
    var impl = player.pseudoAttributeValueSelector();

    // skips MARKER, end index
    impl.nameIndex = player.protoGet(index + 2);

    int valueOffset = player.protoGet(index + 3);

    int operatorOrdinal = player.protoGet(valueOffset + 2);

    impl.operator = AttributeValueOperator.ofOrdinal(operatorOrdinal);

    impl.valueIndex = player.protoGet(valueOffset + 3);

    return impl;
  }

  private PClassSelectorImpl nextClassSelector(int index) {
    var impl = player.pseudoClassSelector();

    // skips MARKER, end index
    impl.objectIndex = player.protoGet(index + 2);

    return impl;
  }

  private Combinator nextCombinator(int index) {
    // skips MARKER, end index
    int ordinal = player.protoGet(index + 2);

    return Combinator.ofOrdinal(ordinal);
  }

  private PIdSelectorImpl nextIdSelector(int index) {
    var impl = player.pseudoIdSelector();

    // skips MARKER, end index
    impl.objectIndex = player.protoGet(index + 2);

    return impl;
  }

  private PSelectorElement nextPseudoClassSelector(int index) {
    // skips MARKER, end index
    int ordinal = player.protoGet(index + 2);

    return objectos.css.internal.PseudoClassSelector.ofOrdinal(ordinal);
  }

  private PSelectorElement nextPseudoElementSelector(int index) {
    // skips MARKER, end index
    int ordinal = player.protoGet(index + 2);

    return objectos.css.internal.PseudoElementSelector.ofOrdinal(ordinal);
  }

  private TypeSelector nextTypeSelector(int index) {
    // skips MARKER, end index
    int ordinal = player.protoGet(index + 2);

    return TypeSelector.ofOrdinal(ordinal);
  }

}