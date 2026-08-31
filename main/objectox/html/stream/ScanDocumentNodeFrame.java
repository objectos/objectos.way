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

final class ScanDocumentNodeFrame extends Frame {

  ScanDocumentNodeFrame(int index) {
    super(index);
  }

  @Override
  public final State newState(Ctx ctx) {
    throw new UnsupportedOperationException();
  }

  @Override
  final boolean equalsImpl(Frame that) {
    return that instanceof ScanDocumentNodeFrame;
  }

}