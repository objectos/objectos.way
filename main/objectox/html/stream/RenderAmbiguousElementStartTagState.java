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

import objectox.html.AmbiguousName;
import objectox.html.IntSize;
import objectox.html.elem.ElementName;

final class RenderAmbiguousElementStartTagState implements State {

  private final Ctx ctx;

  private final IntSize nameSize;

  private final IntSize valueSize;

  RenderAmbiguousElementStartTagState(Ctx ctx, IntSize nameSize, IntSize valueSize) {
    this.ctx = ctx;

    this.nameSize = nameSize;

    this.valueSize = valueSize;
  }

  @Override
  public final State compute() {
    final int level;
    level = ctx.stackIndex();

    final int nameIndex;
    nameIndex = ctx.nextInt(nameSize);

    final AmbiguousName name;
    name = AmbiguousName.get(nameIndex);

    final ElementName elementName;
    elementName = name.element;

    final BeginStartTagEvent event;
    event = new BeginStartTagEvent(level, elementName);

    ctx.result(event);

    final int valueIndex;
    valueIndex = ctx.nextInt(valueSize);

    final String value;
    value = ctx.objString(valueIndex);

    return new RenderAmbiguousElementEndStartState(ctx, elementName, value);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof RenderAmbiguousElementStartTagState that
        && ctx.equals(that.ctx)
        && nameSize.equals(that.nameSize)
        && valueSize.equals(that.valueSize);
  }

}
