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

import objectos.html.TestableName;
import objectos.html.stream.RenderTestable;
import objectox.html.TestableNameCell;
import objectox.html.TestableNameEnum;

final class RenderTestableNodeState implements State {

  private final Ctx ctx;

  private final TestableName name;

  private final String value;

  RenderTestableNodeState(Ctx ctx, TestableName name, String value) {
    this.ctx = ctx;

    this.name = name;

    this.value = value;
  }

  @Override
  public final State compute() {
    final int level;
    level = ctx.stackIndex();

    final RenderTestable event;
    event = event(level);

    ctx.result(event);

    final Frame top;
    top = ctx.pop();

    final EndNode endNode;
    endNode = new EndNode(ctx);

    return endNode.execute(top);
  }

  private RenderTestable event(int level) {
    return switch (name) {
      case TestableNameEnum e -> switch (e) {
        case H1 -> TestableHeadingEvent.of(level, 1, value);

        case H2 -> TestableHeadingEvent.of(level, 2, value);

        case H3 -> TestableHeadingEvent.of(level, 3, value);

        case H4 -> TestableHeadingEvent.of(level, 4, value);

        case H5 -> TestableHeadingEvent.of(level, 5, value);

        case H6 -> TestableHeadingEvent.of(level, 6, value);

        case DD -> TestableEntryValueEvent.of(level, value);

        case DT -> TestableEntryNameEvent.of(level, value);
      };

      case TestableNameCell c -> TestableCellEvent.of(level, c.width(), value);
    };
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof RenderTestableNodeState that
        && ctx.equals(that.ctx)
        && name.equals(that.name)
        && value.equals(that.value);
  }

}
