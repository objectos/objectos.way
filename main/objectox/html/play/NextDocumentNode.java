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
import objectox.html.rec.HtmlBytes;

final class NextDocumentNode {

  private final HtmlSource source;

  private final int startIndex;

  NextDocumentNode(HtmlSource source, int startIndex) {
    this.source = source;

    this.startIndex = startIndex;
  }

  public final State compute() {
    if (source.hasByte(startIndex)) {
      return compute0();
    } else {
      return EndDocumentState.INSTANCE;
    }
  }

  private State compute0() {
    final byte node;
    node = source.getByte(startIndex);

    return switch (node) {
      case HtmlBytes.STARTTAG8 -> element(IntSize.BIT8);

      default -> throw new UnsupportedOperationException("Implement me :: node=" + node);
    };
  }

  private State element(final IntSize size) {
    final SkipElement skip;
    skip = new SkipElement(source, startIndex, size);

    final int returnIndex;
    returnIndex = skip.compute();

    final Stack stack;
    stack = Stack.of(returnIndex);

    final BeginElement next;
    next = new BeginElement(source, startIndex, size, stack);

    return next.compute();
  }

}
