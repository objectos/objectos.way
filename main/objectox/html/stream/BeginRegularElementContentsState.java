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

final class BeginRegularElementContentsState extends AbstractBeginElementContentsState {

  BeginRegularElementContentsState(Ctx ctx) {
    super(ctx);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof BeginRegularElementContentsState that
        && ctx.equals(that.ctx);
  }

  @Override
  final AbstractElementAttributeFrame newFrame(int startIndex, IntSize size, int length) {
    return new ScanRegularElementAttributeFrame(startIndex, size, length);
  }

  @Override
  final State nextState() {
    return new ScanRegularElementAttributeState(ctx);
  }

}
