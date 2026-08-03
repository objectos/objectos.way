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
import objectos.html.format.QuotationMarkStyle;
import objectos.html.format.RenderAttributeOptions;

public final class RenderAttributeFormatterBuilder implements RenderAttributeOptions {

  private QuotationMarkStyle quotationMarkStyle = QuotationMarkStyle.doubleMark();

  public final RenderAttributeFormatter build() {
    return new RenderAttributeFormatter(
        quotationMarkStyle
    );
  }

  @Override
  public final void quotationMarkStyle(QuotationMarkStyle value) {
    quotationMarkStyle = Objects.requireNonNull(value, "value == null");
  }

}
