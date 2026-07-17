/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html.play;

import objectox.html.HtmlByteProto;
import objectox.html.elem.ElementNamePojo;

final class RootState extends AbstractState {

  RootState(byte[] main, int mainIndex, Object[] objects) {
    super(main, mainIndex, objects);
  }

  @Override
  public final State compute() {
    if (hasMain()) {
      return compute0();
    } else {
      return EndState.INSTANCE;
    }
  }

  private State compute0() {
    final byte proto;
    proto = skipIfNecessary();

    return switch (proto) {
      case HtmlByteProto.ELEMENT -> toElement();

      case HtmlByteProto.NULL -> EndState.INSTANCE;

      default -> {
        final String msg;
        msg = "Implement me :: proto=%d".formatted(proto);

        throw new UnsupportedOperationException(msg);
      }
    };
  }

  private byte skipIfNecessary() {
    while (hasMain()) {
      final byte proto;
      proto = peekByte();

      switch (proto) {
        case HtmlByteProto.MARKED3 -> skip(3);

        case HtmlByteProto.MARKED4 -> skip(4);

        case HtmlByteProto.MARKED5 -> skip(5);

        case HtmlByteProto.MARKED6 -> skip(6);

        case HtmlByteProto.ELEMENT -> {
          return proto;
        }

        default -> {
          final String msg;
          msg = "Implement me :: proto=%d".formatted(proto);

          throw new UnsupportedOperationException(msg);
        }
      }
    }

    return HtmlByteProto.NULL;
  }

  private State toElement() {
    final int parentIndex;
    parentIndex = set(HtmlByteProto.ROOT_ELEMENT);

    skipInt16();

    final byte standardName;
    standardName = nextByte();

    assert standardName == HtmlByteProto.STANDARD_NAME;

    final int ordinal;
    ordinal = nextInt8();

    final ElementNamePojo name;
    name = ElementNamePojo.get(ordinal);

    return name.endTag()
        ? new StartTagState(main, mainIndex, objects, parentIndex, name)
        : null;
  }

}
