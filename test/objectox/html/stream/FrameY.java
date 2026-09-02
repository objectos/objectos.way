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
package objectox.html.stream;

import java.util.function.Consumer;
import objectox.html.IntSize;
import objectox.html.elem.ElementName;

final class FrameY {

  int index;

  private ElementKind elementKind;

  private ElementName elementName;

  private int valueIndex;

  private int valueLength;

  private IntSize valueSize;

  public static Frame create(Consumer<? super FrameY> opts) {
    final FrameY y;
    y = new FrameY();

    opts.accept(y);

    return y.build();
  }

  public final void regular(ElementName name) {
    elementKind = ElementKind.REGULAR;

    elementName = name;
  }

  public final void values8(int index, int length) {
    valueIndex = index;

    valueLength = length;

    valueSize = IntSize.BIT8;
  }

  public final void voidElem(ElementName name) {
    elementKind = ElementKind.VOID;

    elementName = name;
  }

  private Frame build() {
    return new Frame(
        index,

        new ElementFrame(
            elementKind,

            elementName,

            valueIndex,

            valueLength,

            valueSize
        )
    );
  }

}
