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
package objectox.html.rec;

import objectos.html.Component;
import objectos.html.Markup;
import objectos.way.Html;
import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlInstruction;

final class ComponentRecorder {

  private final ByteArray main;

  private final ForwardOffsetRecorder forwardOffsetRecorder;

  private final ReverseOffsetRecorder reverseOffsetRecorder;

  ComponentRecorder(ByteArray main, ForwardOffsetRecorder forwardOffsetRecorder, ReverseOffsetRecorder reverseOffsetRecorder) {
    this.main = main;

    this.forwardOffsetRecorder = forwardOffsetRecorder;

    this.reverseOffsetRecorder = reverseOffsetRecorder;
  }

  public final Html.Instruction.OfFragment record(Markup ctx, Component... components) {
    final int startIndex;
    startIndex = begin();

    for (int idx = 0, len = components.length; idx < len; idx++) {
      final Component c;
      c = components[idx];

      if (c == null) {
        final String msg;
        msg = "components[%d] == null".formatted(idx);

        throw new NullPointerException(msg);
      }

      c.renderHtml(ctx);
    }

    end(startIndex);

    return HtmlInstruction.FRAGMENT;
  }

  public final Html.Instruction.OfFragment record(Markup ctx, Iterable<? extends Component> components) {
    final int startIndex;
    startIndex = begin();

    for (Component c : components) {
      c.renderHtml(ctx);
    }

    end(startIndex);

    return HtmlInstruction.FRAGMENT;
  }

  private int begin() {
    // we mark:
    // 1) the start of the contents of the current declaration
    final int startIndex;
    startIndex = main.size();

    main.add(
        HtmlByteProto.FRAGMENT,

        // length takes 3 bytes
        HtmlByteProto.NULL,
        HtmlByteProto.NULL,
        HtmlByteProto.NULL
    );

    return startIndex;
  }

  private void end(int startIndex) {
    reverseOffsetRecorder.record(startIndex);

    forwardOffsetRecorder.three(startIndex);
  }

}
