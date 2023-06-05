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
import objectos.css.pseudom.IterableOnce;
import objectos.css.pseudom.PRule;
import objectos.css.pseudom.PRule.PDeclaration;
import objectos.css.pseudom.PSelector;

public final class PseudoStyleRule
    implements PRule.PStyleRule, IterableOnce<PDeclaration>, Iterator<PDeclaration> {

  private static final int START = -1;
  private static final int ITERABLE = -2;
  private static final int ITERATOR = -3;
  private static final int NEXT = -4;
  @SuppressWarnings("unused")
  private static final int NEXT_CONSUMED = -5;

  private final CssPlayer player;

  private int protoIndex;

  private int state;

  PseudoStyleRule(CssPlayer player) {
    this.player = player;
  }

  @Override
  public final IterableOnce<PDeclaration> declarations() {
    state = player.cas(state, START, ITERABLE);

    return this;
  }

  @Override
  public final boolean hasNext() {
    switch (state) {
      case ITERATOR -> {}

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
  public final Iterator<PDeclaration> iterator() {
    state = player.cas(state, ITERABLE, ITERATOR);

    return this;
  }

  @Override
  public final PDeclaration next() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final PSelector selector() {
    return player.pseudoSelector().init(protoIndex);
  }

  final PseudoStyleRule init(int index) {
    protoIndex = index;

    state = START;

    return this;
  }

  private boolean hasNext0() {
    var found = false;

    loop: while (player.protoMore(protoIndex)) {
      int proto = player.protoGet(protoIndex);

      switch (proto) {
        case ByteProto.CLASS_SELECTOR,
             ByteProto.CLASS_SELECTOR_EXTERNAL,
             ByteProto.COMBINATOR,
             ByteProto.ID_SELECTOR,
             ByteProto.ID_SELECTOR_EXTERNAL,
             ByteProto.PSEUDO_CLASS_SELECTOR,
             ByteProto.PSEUDO_ELEMENT_SELECTOR,
             ByteProto.STYLE_RULE,
             ByteProto.TYPE_SELECTOR -> protoIndex += 2;

        case ByteProto.STYLE_RULE_END -> {
          break loop;
        }

        default -> throw new UnsupportedOperationException(
          "Implement me :: proto=" + proto
        );
      }
    }

    return found;
  }

}