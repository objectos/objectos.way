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

import objectox.html.IntSize;
import objectox.html.elem.ElementName;

final class BeginStartTagState implements State {

  private final Ctx ctx;

  private final ElementKind elementKind;

  private final IntSize nameSize;

  BeginStartTagState(Ctx ctx, ElementKind elementKind, IntSize nameSize) {
    this.ctx = ctx;

    this.elementKind = elementKind;

    this.nameSize = nameSize;
  }

  @Override
  public final State compute() {
    final int nameIndex;
    nameIndex = ctx.nextInt(nameSize);

    final ElementName elementName;
    elementName = ElementName.get(nameIndex);

    final BeginStartTagEvent event;
    event = new BeginStartTagEvent(elementName);

    ctx.result(event);

    return switch (elementKind) {
      case REGULAR -> new BeginRegularElementContentsState(ctx);

      case VOID -> new BeginVoidElementContentsState(ctx);

      case TEXT -> throw new UnsupportedOperationException("Implement me");
    };
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof BeginStartTagState that
        && ctx.equals(that.ctx)
        && elementKind.equals(that.elementKind)
        && nameSize.equals(that.nameSize);
  }

}
