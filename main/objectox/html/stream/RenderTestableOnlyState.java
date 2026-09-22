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
import objectox.html.IntSize;

final class RenderTestableOnlyState implements State {

  private final Ctx ctx;

  private final IntSize testSize;

  RenderTestableOnlyState(Ctx ctx, IntSize testSize) {
    this.ctx = ctx;

    this.testSize = testSize;
  }

  @Override
  public final State compute() {
    final int testIndex;
    testIndex = ctx.nextInt(testSize);

    final String value;
    value = ctx.objString(testIndex);

    final TestableName name;
    name = ctx.objTestableName(testIndex - 1);

    return new RenderTestableNodeState(ctx, name, value);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof RenderTestableOnlyState that
        && ctx.equals(that.ctx)
        && testSize.equals(that.testSize);
  }

}
