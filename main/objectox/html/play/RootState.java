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
import objectox.html.ObjectArray;
import objectox.html.elem.ElementNamePojo;

final class RootState implements State {

  private final BytePlayer main;

  private final ObjectArray objects;

  RootState(BytePlayer main, ObjectArray objects) {
    this.main = main;

    this.objects = objects;
  }

  @Override
  public final State compute() {
    if (main.hasNext()) {
      return compute0();
    } else {
      return EndState.INSTANCE;
    }
  }

  private State compute0() {
    skipIfNecessary();

    final byte proto;
    proto = main.next();

    return switch (proto) {
      case HtmlByteProto.ELEMENT -> toElement();

      default -> {
        final String msg;
        msg = "Implement me :: proto=%d".formatted(proto);

        throw new UnsupportedOperationException(msg);
      }
    };
  }

  private void skipIfNecessary() {
    while (main.hasNext()) {
      final byte proto;
      proto = main.peek();

      switch (proto) {
        case HtmlByteProto.MARKED3 -> main.skip(3);

        case HtmlByteProto.MARKED4 -> main.skip(4);

        case HtmlByteProto.MARKED5 -> main.skip(5);

        case HtmlByteProto.MARKED6 -> main.skip(6);

        case HtmlByteProto.ELEMENT -> {
          return;
        }

        default -> {
          final String msg;
          msg = "Implement me :: proto=%d".formatted(proto);

          throw new UnsupportedOperationException(msg);
        }
      }
    }
  }

  private State toElement() {
    main.skipInt16();

    final byte standardName;
    standardName = main.next();

    assert standardName == HtmlByteProto.STANDARD_NAME;

    final int ordinal;
    ordinal = main.nextInt8();

    final ElementNamePojo name;
    name = ElementNamePojo.get(ordinal);

    return name.endTag()
        ? new StartTagState(main, objects, name)
        : null;
  }

}
