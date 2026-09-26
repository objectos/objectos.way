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

import objectos.html.stream.RenderTestable;
import objectox.html.IntSize;

record TestableNode(
    TestableKind kind,

    int heading,

    int width,

    String key,

    String value
) {

  static TestableNode parse(Ctx ctx, TestableKind kind, IntSize size) {
    return switch (kind) {
      case HEADING -> parseHeading(ctx, size);

      case CELL -> parseCell(ctx, size);

      case PROP -> parseProp(ctx, size);

      case PROP_KEY -> parsePropKey(ctx, size);

      case PROP_VALUE -> parsePropValue(ctx, size);

      case NEWLINE -> new TestableNode(kind, 0, 0, null, null);
    };
  }

  final RenderTestable event(int level) {
    return switch (kind) {
      case HEADING -> TestableHeadingEvent.of(level, heading, value);

      case CELL -> TestableCellEvent.of(level, width, value);

      case PROP -> TestablePropEvent.of(level, key, value);

      case PROP_KEY -> TestableEntryNameEvent.of(level, key);

      case PROP_VALUE -> TestableEntryValueEvent.of(level, value);

      case NEWLINE -> TestableNewLineEvent.of(level);
    };
  }

  final String htmlText() {
    return switch (kind) {
      case HEADING, CELL, PROP, PROP_VALUE -> value;

      case PROP_KEY -> key;

      case NEWLINE -> null;
    };
  }

  private static TestableNode parseCell(Ctx ctx, IntSize size) {
    final int width;
    width = ctx.nextInt(size);

    final int valueIndex;
    valueIndex = ctx.nextInt(size);

    final String value;
    value = ctx.objString(valueIndex);

    return new TestableNode(TestableKind.CELL, 0, width, null, value);
  }

  private static TestableNode parseHeading(Ctx ctx, IntSize size) {
    final int heading;
    heading = Byte.toUnsignedInt(ctx.nextByte());

    final int valueIndex;
    valueIndex = ctx.nextInt(size);

    final String value;
    value = ctx.objString(valueIndex);

    return new TestableNode(TestableKind.HEADING, heading, 0, null, value);
  }

  private static TestableNode parseProp(Ctx ctx, IntSize size) {
    final int keyIndex;
    keyIndex = ctx.nextInt(size);

    final int valueIndex;
    valueIndex = ctx.nextInt(size);

    final String key;
    key = ctx.objString(keyIndex);

    final String value;
    value = ctx.objString(valueIndex);

    return new TestableNode(TestableKind.PROP, 0, 0, key, value);
  }

  private static TestableNode parsePropKey(Ctx ctx, IntSize size) {
    final int keyIndex;
    keyIndex = ctx.nextInt(size);

    final String key;
    key = ctx.objString(keyIndex);

    return new TestableNode(TestableKind.PROP_KEY, 0, 0, key, null);
  }

  private static TestableNode parsePropValue(Ctx ctx, IntSize size) {
    final int valueIndex;
    valueIndex = ctx.nextInt(size);

    final String value;
    value = ctx.objString(valueIndex);

    return new TestableNode(TestableKind.PROP_VALUE, 0, 0, null, value);
  }

}
