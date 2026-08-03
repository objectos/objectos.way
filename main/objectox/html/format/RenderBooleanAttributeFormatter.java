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
package objectox.html.format;

import objectos.html.format.BooleanAttributeValueStyle;
import objectos.html.format.QuotationMarkStyle;
import objectos.html.stream.RenderBooleanAttribute;

final class RenderBooleanAttributeFormatter {

  final BooleanAttributeValueStyle valueStyle;

  final QuotationMarkStyle quotationMarkStyle;

  RenderBooleanAttributeFormatter(BooleanAttributeValueStyle valueStyle, QuotationMarkStyle quotationMarkStyle) {
    this.valueStyle = valueStyle;

    this.quotationMarkStyle = quotationMarkStyle;
  }

  public final void format(RenderBooleanAttribute attr, Output out) {
    out.append(' ');

    out.append(attr.name());

    switch (valueStyle) {
      case BooleanAttributeValueOption o -> {
        switch (o) {
          case NO_VALUE -> {}

          case EMPTY_STRING -> emitValue("", out);

          case ATTRIBUTE_NAME -> emitValue(attr.name(), out);
        }
      }
    }
  }

  private void emitValue(String value, Output out) {
    out.append('=');

    switch (quotationMarkStyle) {
      case QuotationMarkOption o -> {
        out.append(o.mark);

        out.append(value);

        out.append(o.mark);
      }
    }
  }

}
