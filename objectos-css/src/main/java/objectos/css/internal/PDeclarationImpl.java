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
import objectos.css.pseudom.PDeclaration;
import objectos.css.pseudom.PProperty;
import objectos.css.pseudom.PPropertyValue;
import objectos.css.tmpl.LengthUnit;

public final class PDeclarationImpl
    implements PDeclaration, IterableOnce<PPropertyValue>, Iterator<PPropertyValue> {

  static final int START = -1;
  private static final int ITERABLE = -2;
  private static final int ITERATOR = -3;
  private static final int NEXT = -4;
  private static final int NEXT_CONSUMED = -5;

  private final CssPlayer player;

  Property property;

  int protoIndex;

  int state;

  PDeclarationImpl(CssPlayer player) {
    this.player = player;
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
  public final Iterator<PPropertyValue> iterator() {
    state = player.cas(state, ITERABLE, ITERATOR);

    return this;
  }

  @Override
  public final PPropertyValue next() {
    if (hasNext()) {
      return next0();
    } else {
      throw new NoSuchElementException();
    }
  }

  @Override
  public final PProperty property() {
    return property;
  }

  @Override
  public final IterableOnce<PPropertyValue> values() {
    state = player.cas(state, START, ITERABLE);

    return this;
  }

  private boolean hasNext0() {
    var found = false;

    loop: while (player.protoMore(protoIndex)) {
      int proto = player.protoGet(protoIndex);

      switch (proto) {
        case ByteProto.DECLARATION_END -> {
          break loop;
        }

        case ByteProto.DOUBLE_VALUE,
             ByteProto.INT_VALUE,
             ByteProto.KEYWORD,
             ByteProto.LENGTH_DOUBLE,
             ByteProto.LENGTH_INT,
             ByteProto.STRING_VALUE -> {
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

  private PPropertyValue next0() {
    state = player.cas(state, NEXT, NEXT_CONSUMED);

    int proto = player.protoGet(protoIndex++);

    return switch (proto) {
      case ByteProto.DOUBLE_VALUE -> {
        var impl = player.pseudoDoubleValue();

        int high = player.protoGet(protoIndex++);
        int low = player.protoGet(protoIndex++);
        long bits = high;
        bits <<= 32;
        bits |= low;

        impl.doubleValue = Double.longBitsToDouble(bits);

        yield impl;
      }

      case ByteProto.INT_VALUE -> {
        var impl = player.pseudoIntValue();

        impl.intValue = player.protoGet(protoIndex++);

        yield impl;
      }

      case ByteProto.KEYWORD -> Keyword.ofOrdinal(
        player.protoGet(protoIndex++)
      );

      case ByteProto.LENGTH_DOUBLE -> {
        var impl = player.pseudoLengthDoubleValue();

        int ordinal = player.protoGet(protoIndex++);

        impl.unit = LengthUnit.ofOrdinal(ordinal);

        int high = player.protoGet(protoIndex++);
        int low = player.protoGet(protoIndex++);
        long bits = high;
        bits <<= 32;
        bits |= low;

        impl.value = Double.longBitsToDouble(bits);

        yield impl;
      }

      case ByteProto.LENGTH_INT -> {
        var impl = player.pseudoLengthIntValue();

        int ordinal = player.protoGet(protoIndex++);

        impl.unit = LengthUnit.ofOrdinal(ordinal);

        impl.value = player.protoGet(protoIndex++);

        yield impl;
      }

      case ByteProto.STRING_VALUE -> {
        var impl = player.pseudoStringValue();

        impl.objectIndex = player.protoGet(protoIndex++);

        yield impl;
      }

      default -> throw new UnsupportedOperationException(
        "Implement me :: proto=" + proto
      );
    };
  }

}