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

import objectox.html.IntSize;
import objectox.html.attr.AttributeNamePojo;

final class RenderBooleanAttributeState implements State {

  private final Ctx ctx;

  private final IntSize nameSize;

  RenderBooleanAttributeState(Ctx ctx, IntSize nameSize) {
    this.ctx = ctx;

    this.nameSize = nameSize;
  }

  @Override
  public final State compute() {
    final int nameCode;
    nameCode = ctx.nextInt(nameSize);

    final AttributeNamePojo attributeName;
    attributeName = AttributeNamePojo.get(nameCode);

    final RenderBooleanAttributeEvent event;
    event = new RenderBooleanAttributeEvent(attributeName);

    ctx.result(event);

    ctx.pop(); // jmp frame

    return new ScanElementAttributeState(ctx);
  }

}
