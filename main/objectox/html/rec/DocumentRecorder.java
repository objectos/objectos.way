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

import objectos.html.AttributeName;
import objectos.html.ElementName;
import objectos.html.rec.Instruction;

final class DocumentRecorder {

  private final AttributeRecorder attributeRecorder;

  private final BooleanAttributeRecorder booleanAttributeRecorder;

  private final ElementRecorder elementRecorder;

  DocumentRecorder(AttributeRecorder attributeRecorder, BooleanAttributeRecorder booleanAttributeRecorder, ElementRecorder elementRecorder) {
    this.attributeRecorder = attributeRecorder;

    this.booleanAttributeRecorder = booleanAttributeRecorder;

    this.elementRecorder = elementRecorder;
  }

  public static DocumentRecorder of(HtmlSink sink) {
    return new DocumentRecorder(
        new AttributeRecorder(sink),

        new BooleanAttributeRecorder(sink),

        new ElementRecorder(sink)
    );
  }

  public final AttributeInstruction attribute(AttributeName name) {
    return booleanAttributeRecorder.record(name);
  }

  public final AttributeInstruction attribute(AttributeName name, String value) {
    return attributeRecorder.record(name, value);
  }

  public final ElementInstruction element(ElementName name, Instruction... contents) {
    return elementRecorder.record(name, contents);
  }

}
