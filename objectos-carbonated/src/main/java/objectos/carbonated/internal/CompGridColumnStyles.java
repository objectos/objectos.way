/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.carbonated.internal;

import objectos.css.CssTemplate;

final class CompGridColumnStyles extends CssTemplate {

  private final Impl impl;

  CompGridColumnStyles(Impl impl) {
    this.impl = impl;
  }

  @Override
  protected final void definition() {
    style(
      impl.COLUMN,

      marginRight(rem(1)),
      marginLeft(rem(1))
    );

    style(impl.SPAN_0, display(none));
    style(impl.SPAN_1, display(block), gridColumnStart(auto), gridColumnEnd(span, l(1)));
    style(impl.SPAN_2, display(block), gridColumnStart(auto), gridColumnEnd(span, l(2)));
    style(impl.SPAN_3, display(block), gridColumnStart(auto), gridColumnEnd(span, l(3)));
    style(impl.SPAN_4, display(block), gridColumnStart(auto), gridColumnEnd(span, l(4)));
    style(impl.SPAN_5, display(block), gridColumnStart(auto), gridColumnEnd(span, l(5)));
    style(impl.SPAN_6, display(block), gridColumnStart(auto), gridColumnEnd(span, l(6)));
    style(impl.SPAN_7, display(block), gridColumnStart(auto), gridColumnEnd(span, l(7)));
    style(impl.SPAN_8, display(block), gridColumnStart(auto), gridColumnEnd(span, l(8)));
    style(impl.SPAN_9, display(block), gridColumnStart(auto), gridColumnEnd(span, l(9)));
    style(impl.SPAN_10, display(block), gridColumnStart(auto), gridColumnEnd(span, l(10)));
    style(impl.SPAN_11, display(block), gridColumnStart(auto), gridColumnEnd(span, l(11)));
    style(impl.SPAN_12, display(block), gridColumnStart(auto), gridColumnEnd(span, l(12)));
    style(impl.SPAN_13, display(block), gridColumnStart(auto), gridColumnEnd(span, l(13)));
    style(impl.SPAN_14, display(block), gridColumnStart(auto), gridColumnEnd(span, l(14)));
    style(impl.SPAN_15, display(block), gridColumnStart(auto), gridColumnEnd(span, l(15)));
    style(impl.SPAN_16, display(block), gridColumnStart(auto), gridColumnEnd(span, l(16)));
  }

}