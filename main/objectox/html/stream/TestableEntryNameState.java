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

final class TestableEntryNameState implements State, ScanOp {

  private final Ctx ctx;

  private final TestableLevel testableLevel;

  private final IntSize size;

  TestableEntryNameState(Ctx ctx, TestableLevel testableLevel, IntSize size) {
    this.ctx = ctx;

    this.testableLevel = testableLevel;

    this.size = size;
  }

  @Override
  public final State compute() {
    return switch (testableLevel) {
      case REGULAR -> computeRegular();

      case TOP_LEVEL, TEST_ONLY -> computeTopLevel();
    };
  }

  @Override
  public final State documentNode() {
    return switch (testableLevel) {
      case REGULAR, TEST_ONLY -> {
        final int bytes;
        bytes = size.bytes();

        yield new SkipState(ctx, bytes);
      }

      case TOP_LEVEL -> this;
    };
  }

  @Override
  public final State elementAttribute() {
    return switch (testableLevel) {
      case REGULAR, TEST_ONLY -> this;

      case TOP_LEVEL -> ScanOp.super.elementAttribute();
    };
  }

  @Override
  public final State elementNode() {
    return switch (testableLevel) {
      case REGULAR, TEST_ONLY -> this;

      case TOP_LEVEL -> ScanOp.super.elementNode();
    };
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof TestableEntryNameState that
        && ctx.equals(that.ctx)
        && testableLevel.equals(that.testableLevel)
        && size.equals(that.size);
  }

  private State computeRegular() {
    final int level;
    level = ctx.stackIndex();

    final Frame frame;
    frame = ctx.peekFrame();

    final int startIndex;
    startIndex = frame.index;

    final int valueIndex;
    valueIndex = ctx.nextInt(size);

    final String value;
    value = ctx.objString(valueIndex);

    frame.index = startIndex;

    final RenderTextEvent event;
    event = RenderTextEvent.of(level, value);

    ctx.result(event);

    return new TestableEntryNameState(ctx, TestableLevel.TOP_LEVEL, size);
  }

  private State computeTopLevel() {
    final int level;
    level = ctx.stackIndex();

    final int valueIndex;
    valueIndex = ctx.nextInt(size);

    final String value;
    value = ctx.objString(valueIndex);

    final TestableEntryNameEvent event;
    event = TestableEntryNameEvent.of(level, value);

    ctx.result(event);

    final Frame top;
    top = ctx.pop();

    return new EndNodeState(ctx, top);
  }

}
