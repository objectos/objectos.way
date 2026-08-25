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

final class ScanRegularElementAttributeState extends AbstractScanElementAttributeState {

  ScanRegularElementAttributeState(Ctx ctx) {
    super(ctx);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ScanRegularElementAttributeState that
        && ctx.equals(that.ctx);
  }

  @Override
  final State computeNonAttribute(byte code) {
    throw State.implCode(code);
  }

  @Override
  final EndStartTagKind endTagIsEmpty() {
    return EndStartTagKind.EMPTY_ELEMENT;
  }

  @Override
  final State endTagIsEmptyNextState() {
    return new RenderEndTagState(ctx);
  }

}
