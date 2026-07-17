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

import objectos.html.ElementName;
import objectos.html.play.EndTag;
import objectox.html.HtmlByteProto;

public final class EndTagState extends AbstractState implements EndTag {

  final int parentIndex;

  final ElementName name;

  EndTagState(byte[] main, int mainIndex, Object[] objects, int parentIndex, ElementName name) {
    super(main, mainIndex, objects);

    this.parentIndex = parentIndex;

    this.name = name;
  }

  @Override
  public final State compute() {
    final byte parentProto;
    parentProto = peekByte(parentIndex);

    return switch (parentProto) {
      case HtmlByteProto.ROOT_ELEMENT -> {
        final RootState root;
        root = new RootState(main, parentIndex, objects);

        yield root.endElement();
      }

      default -> throw implMe(parentProto);
    };
  }

  @Override
  public final String name() {
    return name.name();
  }

}
