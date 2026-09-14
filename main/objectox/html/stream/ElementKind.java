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

import objectos.html.stream.EndStartTagKind;
import objectox.html.elem.ElementName;

enum ElementKind {

  REGULAR,

  VOID;

  public final State endStartTagStateEmpty(Ctx ctx, ElementName elementName) {
    final int level;
    level = ctx.level();

    return switch (this) {
      case REGULAR -> {
        final EndStartTagKind kind;
        kind = EndStartTagKind.EMPTY_ELEMENT;

        final EndStartTagEvent event;
        event = new EndStartTagEvent(level, kind);

        ctx.result(event);

        yield new RenderEndTagState(ctx, elementName);
      }

      case VOID -> {
        final EndStartTagKind kind;
        kind = EndStartTagKind.VOID_ELEMENT;

        final EndStartTagEvent event;
        event = new EndStartTagEvent(level, kind);

        ctx.result(event);

        yield new EndElementState(ctx);
      }
    };
  }

}
