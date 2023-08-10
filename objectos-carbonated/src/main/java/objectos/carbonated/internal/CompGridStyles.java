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

import objectos.carbonated.Breakpoint;
import objectos.css.CssTemplate;
import objectos.css.util.ClassSelector;

final class CompGridStyles extends CssTemplate {

  static final ClassSelector COLUMN = U.nextClass();

  static final ClassSelector SPAN_0 = U.nextClass();
  static final ClassSelector SPAN_1 = U.nextClass();
  static final ClassSelector SPAN_2 = U.nextClass();
  static final ClassSelector SPAN_3 = U.nextClass();
  static final ClassSelector SPAN_4 = U.nextClass();
  static final ClassSelector SPAN_5 = U.nextClass();
  static final ClassSelector SPAN_6 = U.nextClass();
  static final ClassSelector SPAN_7 = U.nextClass();
  static final ClassSelector SPAN_8 = U.nextClass();
  static final ClassSelector SPAN_9 = U.nextClass();
  static final ClassSelector SPAN_10 = U.nextClass();
  static final ClassSelector SPAN_11 = U.nextClass();
  static final ClassSelector SPAN_12 = U.nextClass();
  static final ClassSelector SPAN_13 = U.nextClass();
  static final ClassSelector SPAN_14 = U.nextClass();
  static final ClassSelector SPAN_15 = U.nextClass();
  static final ClassSelector SPAN_16 = U.nextClass();

  private final Impl impl;

  CompGridStyles(Impl impl) {
    this.impl = impl;
  }

  @Override
  protected final void definition() {
    style(
      impl.GRID,

      display(grid),
      gridTemplateColumns(repeat(l(4), minmax($0, fr(1)))),
      marginLeft(auto),
      marginRight(auto),
      paddingLeft($0),
      paddingRight($0),
      width(pct(100))
    );

    style(
      impl.GRID_STD,

      maxWidth(Breakpoint.MAX)
    );

    style(
      impl.GRID_FULL,

      maxWidth(pct(100))
    );

    style(
      COLUMN,

      marginRight(rem(1)),
      marginLeft(rem(1))
    );

    style(SPAN_0, display(none));
    style(SPAN_1, display(block), gridColumnStart(auto), gridColumnEnd(span, l(1)));
    style(SPAN_2, display(block), gridColumnStart(auto), gridColumnEnd(span, l(2)));
    style(SPAN_3, display(block), gridColumnStart(auto), gridColumnEnd(span, l(3)));
    style(SPAN_4, display(block), gridColumnStart(auto), gridColumnEnd(span, l(4)));
    style(SPAN_5, display(block), gridColumnStart(auto), gridColumnEnd(span, l(5)));
    style(SPAN_6, display(block), gridColumnStart(auto), gridColumnEnd(span, l(6)));
    style(SPAN_7, display(block), gridColumnStart(auto), gridColumnEnd(span, l(7)));
    style(SPAN_8, display(block), gridColumnStart(auto), gridColumnEnd(span, l(8)));
    style(SPAN_9, display(block), gridColumnStart(auto), gridColumnEnd(span, l(9)));
    style(SPAN_10, display(block), gridColumnStart(auto), gridColumnEnd(span, l(10)));
    style(SPAN_11, display(block), gridColumnStart(auto), gridColumnEnd(span, l(11)));
    style(SPAN_12, display(block), gridColumnStart(auto), gridColumnEnd(span, l(12)));
    style(SPAN_13, display(block), gridColumnStart(auto), gridColumnEnd(span, l(13)));
    style(SPAN_14, display(block), gridColumnStart(auto), gridColumnEnd(span, l(14)));
    style(SPAN_15, display(block), gridColumnStart(auto), gridColumnEnd(span, l(15)));
    style(SPAN_16, display(block), gridColumnStart(auto), gridColumnEnd(span, l(16)));

    media(
      minWidth(Breakpoint.MEDIUM),

      style(
        impl.GRID,

        gridTemplateColumns(repeat(l(8), minmax($0, fr(1)))),
        paddingLeft(rem(1)),
        paddingRight(rem(1))
      )
    );

    media(
      minWidth(Breakpoint.LARGE),

      style(
        impl.GRID,

        gridTemplateColumns(repeat(l(16), minmax($0, fr(1))))
      )
    );

    media(
      minWidth(Breakpoint.MAX),

      style(
        impl.GRID,

        paddingLeft(rem(1.5)),
        paddingRight(rem(1.5))
      )
    );
  }

}