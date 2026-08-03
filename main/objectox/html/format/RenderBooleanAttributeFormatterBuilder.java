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

import java.util.Objects;
import objectos.html.format.BooleanAttributeValueStyle;
import objectos.html.format.QuotationMarkStyle;
import objectos.html.format.RenderBooleanAttributeOptions;

public final class RenderBooleanAttributeFormatterBuilder implements RenderBooleanAttributeOptions {

  private BooleanAttributeValueStyle valueStyle = BooleanAttributeValueStyle.noValue();

  private QuotationMarkStyle quotationMarkStyle = QuotationMarkStyle.doubleMark();

  public final RenderBooleanAttributeFormatter build() {
    return new RenderBooleanAttributeFormatter(
        valueStyle,

        quotationMarkStyle
    );
  }

  @Override
  public final void valueStyle(BooleanAttributeValueStyle value) {
    valueStyle = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void quotationMarkStyle(QuotationMarkStyle value) {
    quotationMarkStyle = Objects.requireNonNull(value, "value == null");
  }

}
