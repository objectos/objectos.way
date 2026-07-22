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

import objectos.html.ElementName;
import objectos.html.rec.Instruction;

final class DocumentRecorder {

  private final ElementRecorder elementRecorder;

  DocumentRecorder(ElementRecorder elementRecorder) {
    this.elementRecorder = elementRecorder;
  }

  public final ElementInstruction element(ElementName name, Instruction... contents) {
    return elementRecorder.record(name, contents);
  }

}
