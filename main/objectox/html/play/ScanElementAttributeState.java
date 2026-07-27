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

final class ScanElementAttributeState implements State {

  private final Ctx ctx;

  private final Values values;

  private final int valueIndex;

  ScanElementAttributeState(Ctx ctx, Values values, int valueIndex) {
    this.ctx = ctx;

    this.values = values;

    this.valueIndex = valueIndex;
  }

  @Override
  public final State compute() {
    if (values.hasNext(valueIndex)) {
      return compute0();
    } else {
      ctx.result(EndStartTagEvent.INSTANCE);

      return new ScanElementNodeState(ctx, values, 0);
    }
  }

  private State compute0() {
    throw new UnsupportedOperationException("Implement me");
  }

}
