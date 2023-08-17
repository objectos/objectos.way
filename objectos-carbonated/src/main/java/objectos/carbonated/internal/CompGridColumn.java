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

import objectos.carbonated.Carbon;
import objectos.carbonated.Carbon.Breakpoints;
import objectos.carbonated.Carbon.GridColumn;
import objectos.css.CssTemplate;
import objectos.css.util.ClassSelector;
import objectos.css.util.Next;
import objectos.html.HtmlComponent;
import objectos.html.HtmlTemplate;
import objectos.html.tmpl.Instruction;
import objectos.html.tmpl.Instruction.ElementContents;
import objectos.lang.Check;

public final class CompGridColumn {

  private final Breakpoints breakpoints;

  private final ClassSelector COLUMN;

  private final ClassSelector SM_SPAN_0;
  private final ClassSelector SM_SPAN_1;
  private final ClassSelector SM_SPAN_2;
  private final ClassSelector SM_SPAN_3;
  private final ClassSelector SM_SPAN_4;

  private final ClassSelector MD_SPAN_0;
  private final ClassSelector MD_SPAN_1;
  private final ClassSelector MD_SPAN_2;
  private final ClassSelector MD_SPAN_3;
  private final ClassSelector MD_SPAN_4;
  private final ClassSelector MD_SPAN_5;
  private final ClassSelector MD_SPAN_6;
  private final ClassSelector MD_SPAN_7;
  private final ClassSelector MD_SPAN_8;

  private final ClassSelector LG_SPAN_0;
  private final ClassSelector LG_SPAN_1;
  private final ClassSelector LG_SPAN_2;
  private final ClassSelector LG_SPAN_3;
  private final ClassSelector LG_SPAN_4;
  private final ClassSelector LG_SPAN_5;
  private final ClassSelector LG_SPAN_6;
  private final ClassSelector LG_SPAN_7;
  private final ClassSelector LG_SPAN_8;
  private final ClassSelector LG_SPAN_9;
  private final ClassSelector LG_SPAN_10;
  private final ClassSelector LG_SPAN_11;
  private final ClassSelector LG_SPAN_12;
  private final ClassSelector LG_SPAN_13;
  private final ClassSelector LG_SPAN_14;
  private final ClassSelector LG_SPAN_15;
  private final ClassSelector LG_SPAN_16;

  private final ClassSelector XL_SPAN_0;
  private final ClassSelector XL_SPAN_1;
  private final ClassSelector XL_SPAN_2;
  private final ClassSelector XL_SPAN_3;
  private final ClassSelector XL_SPAN_4;
  private final ClassSelector XL_SPAN_5;
  private final ClassSelector XL_SPAN_6;
  private final ClassSelector XL_SPAN_7;
  private final ClassSelector XL_SPAN_8;
  private final ClassSelector XL_SPAN_9;
  private final ClassSelector XL_SPAN_10;
  private final ClassSelector XL_SPAN_11;
  private final ClassSelector XL_SPAN_12;
  private final ClassSelector XL_SPAN_13;
  private final ClassSelector XL_SPAN_14;
  private final ClassSelector XL_SPAN_15;
  private final ClassSelector XL_SPAN_16;

  private final ClassSelector MAX_SPAN_0;
  private final ClassSelector MAX_SPAN_1;
  private final ClassSelector MAX_SPAN_2;
  private final ClassSelector MAX_SPAN_3;
  private final ClassSelector MAX_SPAN_4;
  private final ClassSelector MAX_SPAN_5;
  private final ClassSelector MAX_SPAN_6;
  private final ClassSelector MAX_SPAN_7;
  private final ClassSelector MAX_SPAN_8;
  private final ClassSelector MAX_SPAN_9;
  private final ClassSelector MAX_SPAN_10;
  private final ClassSelector MAX_SPAN_11;
  private final ClassSelector MAX_SPAN_12;
  private final ClassSelector MAX_SPAN_13;
  private final ClassSelector MAX_SPAN_14;
  private final ClassSelector MAX_SPAN_15;
  private final ClassSelector MAX_SPAN_16;

  CompGridColumn(Breakpoints breakpoints, Next next) {
    this.breakpoints = breakpoints;

    COLUMN = next.classSelector();

    SM_SPAN_0 = next.classSelector();
    SM_SPAN_1 = next.classSelector();
    SM_SPAN_2 = next.classSelector();
    SM_SPAN_3 = next.classSelector();
    SM_SPAN_4 = next.classSelector();

    MD_SPAN_0 = next.classSelector();
    MD_SPAN_1 = next.classSelector();
    MD_SPAN_2 = next.classSelector();
    MD_SPAN_3 = next.classSelector();
    MD_SPAN_4 = next.classSelector();
    MD_SPAN_5 = next.classSelector();
    MD_SPAN_6 = next.classSelector();
    MD_SPAN_7 = next.classSelector();
    MD_SPAN_8 = next.classSelector();

    LG_SPAN_0 = next.classSelector();
    LG_SPAN_1 = next.classSelector();
    LG_SPAN_2 = next.classSelector();
    LG_SPAN_3 = next.classSelector();
    LG_SPAN_4 = next.classSelector();
    LG_SPAN_5 = next.classSelector();
    LG_SPAN_6 = next.classSelector();
    LG_SPAN_7 = next.classSelector();
    LG_SPAN_8 = next.classSelector();
    LG_SPAN_9 = next.classSelector();
    LG_SPAN_10 = next.classSelector();
    LG_SPAN_11 = next.classSelector();
    LG_SPAN_12 = next.classSelector();
    LG_SPAN_13 = next.classSelector();
    LG_SPAN_14 = next.classSelector();
    LG_SPAN_15 = next.classSelector();
    LG_SPAN_16 = next.classSelector();

    XL_SPAN_0 = next.classSelector();
    XL_SPAN_1 = next.classSelector();
    XL_SPAN_2 = next.classSelector();
    XL_SPAN_3 = next.classSelector();
    XL_SPAN_4 = next.classSelector();
    XL_SPAN_5 = next.classSelector();
    XL_SPAN_6 = next.classSelector();
    XL_SPAN_7 = next.classSelector();
    XL_SPAN_8 = next.classSelector();
    XL_SPAN_9 = next.classSelector();
    XL_SPAN_10 = next.classSelector();
    XL_SPAN_11 = next.classSelector();
    XL_SPAN_12 = next.classSelector();
    XL_SPAN_13 = next.classSelector();
    XL_SPAN_14 = next.classSelector();
    XL_SPAN_15 = next.classSelector();
    XL_SPAN_16 = next.classSelector();

    MAX_SPAN_0 = next.classSelector();
    MAX_SPAN_1 = next.classSelector();
    MAX_SPAN_2 = next.classSelector();
    MAX_SPAN_3 = next.classSelector();
    MAX_SPAN_4 = next.classSelector();
    MAX_SPAN_5 = next.classSelector();
    MAX_SPAN_6 = next.classSelector();
    MAX_SPAN_7 = next.classSelector();
    MAX_SPAN_8 = next.classSelector();
    MAX_SPAN_9 = next.classSelector();
    MAX_SPAN_10 = next.classSelector();
    MAX_SPAN_11 = next.classSelector();
    MAX_SPAN_12 = next.classSelector();
    MAX_SPAN_13 = next.classSelector();
    MAX_SPAN_14 = next.classSelector();
    MAX_SPAN_15 = next.classSelector();
    MAX_SPAN_16 = next.classSelector();
  }

  public final class Component extends HtmlComponent implements Carbon.GridColumn {

    private int sm_span;
    private int md_span;
    private int lg_span;
    private int xl_span;
    private int max_span;

    public Component(HtmlTemplate parent) {
      super(parent);

      reset();
    }

    private void reset() {
      sm_span = Integer.MIN_VALUE;
      md_span = Integer.MIN_VALUE;
      lg_span = Integer.MIN_VALUE;
      xl_span = Integer.MIN_VALUE;
      max_span = Integer.MIN_VALUE;
    }

    @Override
    public final ElementContents render(Instruction... contents) {
      ElementContents div;
      div = div(
        COLUMN,

        switch (sm_span) {
          case 0 -> SM_SPAN_0;
          case 1 -> SM_SPAN_1;
          case 2 -> SM_SPAN_2;
          case 3 -> SM_SPAN_3;
          case 4 -> SM_SPAN_4;

          default -> noop();
        },

        switch (md_span) {
          case 0 -> MD_SPAN_0;
          case 1 -> MD_SPAN_1;
          case 2 -> MD_SPAN_2;
          case 3 -> MD_SPAN_3;
          case 4 -> MD_SPAN_4;
          case 5 -> MD_SPAN_5;
          case 6 -> MD_SPAN_6;
          case 7 -> MD_SPAN_7;
          case 8 -> MD_SPAN_8;

          default -> noop();
        },

        switch (lg_span) {
          case 0 -> LG_SPAN_0;
          case 1 -> LG_SPAN_1;
          case 2 -> LG_SPAN_2;
          case 3 -> LG_SPAN_3;
          case 4 -> LG_SPAN_4;
          case 5 -> LG_SPAN_5;
          case 6 -> LG_SPAN_6;
          case 7 -> LG_SPAN_7;
          case 8 -> LG_SPAN_8;
          case 9 -> LG_SPAN_9;
          case 10 -> LG_SPAN_10;
          case 11 -> LG_SPAN_11;
          case 12 -> LG_SPAN_12;
          case 13 -> LG_SPAN_13;
          case 14 -> LG_SPAN_14;
          case 15 -> LG_SPAN_15;
          case 16 -> LG_SPAN_16;

          default -> noop();
        },

        switch (xl_span) {
          case 0 -> XL_SPAN_0;
          case 1 -> XL_SPAN_1;
          case 2 -> XL_SPAN_2;
          case 3 -> XL_SPAN_3;
          case 4 -> XL_SPAN_4;
          case 5 -> XL_SPAN_5;
          case 6 -> XL_SPAN_6;
          case 7 -> XL_SPAN_7;
          case 8 -> XL_SPAN_8;
          case 9 -> XL_SPAN_9;
          case 10 -> XL_SPAN_10;
          case 11 -> XL_SPAN_11;
          case 12 -> XL_SPAN_12;
          case 13 -> XL_SPAN_13;
          case 14 -> XL_SPAN_14;
          case 15 -> XL_SPAN_15;
          case 16 -> XL_SPAN_16;

          default -> noop();
        },

        switch (max_span) {
          case 0 -> MAX_SPAN_0;
          case 1 -> MAX_SPAN_1;
          case 2 -> MAX_SPAN_2;
          case 3 -> MAX_SPAN_3;
          case 4 -> MAX_SPAN_4;
          case 5 -> MAX_SPAN_5;
          case 6 -> MAX_SPAN_6;
          case 7 -> MAX_SPAN_7;
          case 8 -> MAX_SPAN_8;
          case 9 -> MAX_SPAN_9;
          case 10 -> MAX_SPAN_10;
          case 11 -> MAX_SPAN_11;
          case 12 -> MAX_SPAN_12;
          case 13 -> MAX_SPAN_13;
          case 14 -> MAX_SPAN_14;
          case 15 -> MAX_SPAN_15;
          case 16 -> MAX_SPAN_16;

          default -> noop();
        },

        flatten(contents)
      );

      reset();

      return div;
    }

    @Override
    public final GridColumn sm(int span) {
      Check.argument(0 <= span && span <= 4, "span should be: 0 <= span <= 4");

      sm_span = span;

      return this;
    }

    @Override
    public final GridColumn md(int span) {
      Check.argument(0 <= span && span <= 8, "span should be: 0 <= span <= 8");

      md_span = span;

      return this;
    }

    @Override
    public final GridColumn lg(int span) {
      Check.argument(0 <= span && span <= 16, "span should be: 0 <= span <= 16");

      lg_span = span;

      return this;
    }

    @Override
    public final GridColumn xl(int span) {
      Check.argument(0 <= span && span <= 16, "span should be: 0 <= span <= 16");

      xl_span = span;

      return this;
    }

    @Override
    public final GridColumn max(int span) {
      Check.argument(0 <= span && span <= 16, "span should be: 0 <= span <= 16");

      max_span = span;

      return this;
    }

  }

  final class Styles extends CssTemplate {
    @Override
    protected final void definition() {
      style(
        COLUMN,

        marginRight(rem(1)),
        marginLeft(rem(1))
      );

      style(SM_SPAN_0, display(none));
      style(SM_SPAN_1, display(block), gridColumnStart(auto), gridColumnEnd(span, l(1)));
      style(SM_SPAN_2, display(block), gridColumnStart(auto), gridColumnEnd(span, l(2)));
      style(SM_SPAN_3, display(block), gridColumnStart(auto), gridColumnEnd(span, l(3)));
      style(SM_SPAN_4, display(block), gridColumnStart(auto), gridColumnEnd(span, l(4)));

      media(
        minWidth(breakpoints.medium()),

        style(MD_SPAN_0, display(none)),
        style(MD_SPAN_1, display(block), gridColumnStart(auto), gridColumnEnd(span, l(1))),
        style(MD_SPAN_2, display(block), gridColumnStart(auto), gridColumnEnd(span, l(2))),
        style(MD_SPAN_3, display(block), gridColumnStart(auto), gridColumnEnd(span, l(3))),
        style(MD_SPAN_4, display(block), gridColumnStart(auto), gridColumnEnd(span, l(4))),
        style(MD_SPAN_5, display(block), gridColumnStart(auto), gridColumnEnd(span, l(5))),
        style(MD_SPAN_6, display(block), gridColumnStart(auto), gridColumnEnd(span, l(6))),
        style(MD_SPAN_7, display(block), gridColumnStart(auto), gridColumnEnd(span, l(7))),
        style(MD_SPAN_8, display(block), gridColumnStart(auto), gridColumnEnd(span, l(8)))
      );

      media(
        minWidth(breakpoints.large()),

        style(LG_SPAN_0, display(none)),
        style(LG_SPAN_1, display(block), gridColumnStart(auto), gridColumnEnd(span, l(1))),
        style(LG_SPAN_2, display(block), gridColumnStart(auto), gridColumnEnd(span, l(2))),
        style(LG_SPAN_3, display(block), gridColumnStart(auto), gridColumnEnd(span, l(3))),
        style(LG_SPAN_4, display(block), gridColumnStart(auto), gridColumnEnd(span, l(4))),
        style(LG_SPAN_5, display(block), gridColumnStart(auto), gridColumnEnd(span, l(5))),
        style(LG_SPAN_6, display(block), gridColumnStart(auto), gridColumnEnd(span, l(6))),
        style(LG_SPAN_7, display(block), gridColumnStart(auto), gridColumnEnd(span, l(7))),
        style(LG_SPAN_8, display(block), gridColumnStart(auto), gridColumnEnd(span, l(8))),
        style(LG_SPAN_9, display(block), gridColumnStart(auto), gridColumnEnd(span, l(9))),
        style(LG_SPAN_10, display(block), gridColumnStart(auto), gridColumnEnd(span, l(10))),
        style(LG_SPAN_11, display(block), gridColumnStart(auto), gridColumnEnd(span, l(11))),
        style(LG_SPAN_12, display(block), gridColumnStart(auto), gridColumnEnd(span, l(12))),
        style(LG_SPAN_13, display(block), gridColumnStart(auto), gridColumnEnd(span, l(13))),
        style(LG_SPAN_14, display(block), gridColumnStart(auto), gridColumnEnd(span, l(14))),
        style(LG_SPAN_15, display(block), gridColumnStart(auto), gridColumnEnd(span, l(15))),
        style(LG_SPAN_16, display(block), gridColumnStart(auto), gridColumnEnd(span, l(16)))
      );

      media(
        minWidth(breakpoints.xLarge()),

        style(XL_SPAN_0, display(none)),
        style(XL_SPAN_1, display(block), gridColumnStart(auto), gridColumnEnd(span, l(1))),
        style(XL_SPAN_2, display(block), gridColumnStart(auto), gridColumnEnd(span, l(2))),
        style(XL_SPAN_3, display(block), gridColumnStart(auto), gridColumnEnd(span, l(3))),
        style(XL_SPAN_4, display(block), gridColumnStart(auto), gridColumnEnd(span, l(4))),
        style(XL_SPAN_5, display(block), gridColumnStart(auto), gridColumnEnd(span, l(5))),
        style(XL_SPAN_6, display(block), gridColumnStart(auto), gridColumnEnd(span, l(6))),
        style(XL_SPAN_7, display(block), gridColumnStart(auto), gridColumnEnd(span, l(7))),
        style(XL_SPAN_8, display(block), gridColumnStart(auto), gridColumnEnd(span, l(8))),
        style(XL_SPAN_9, display(block), gridColumnStart(auto), gridColumnEnd(span, l(9))),
        style(XL_SPAN_10, display(block), gridColumnStart(auto), gridColumnEnd(span, l(10))),
        style(XL_SPAN_11, display(block), gridColumnStart(auto), gridColumnEnd(span, l(11))),
        style(XL_SPAN_12, display(block), gridColumnStart(auto), gridColumnEnd(span, l(12))),
        style(XL_SPAN_13, display(block), gridColumnStart(auto), gridColumnEnd(span, l(13))),
        style(XL_SPAN_14, display(block), gridColumnStart(auto), gridColumnEnd(span, l(14))),
        style(XL_SPAN_15, display(block), gridColumnStart(auto), gridColumnEnd(span, l(15))),
        style(XL_SPAN_16, display(block), gridColumnStart(auto), gridColumnEnd(span, l(16)))
      );

      media(
        minWidth(breakpoints.max()),

        style(MAX_SPAN_0, display(none)),
        style(MAX_SPAN_1, display(block), gridColumnStart(auto), gridColumnEnd(span, l(1))),
        style(MAX_SPAN_2, display(block), gridColumnStart(auto), gridColumnEnd(span, l(2))),
        style(MAX_SPAN_3, display(block), gridColumnStart(auto), gridColumnEnd(span, l(3))),
        style(MAX_SPAN_4, display(block), gridColumnStart(auto), gridColumnEnd(span, l(4))),
        style(MAX_SPAN_5, display(block), gridColumnStart(auto), gridColumnEnd(span, l(5))),
        style(MAX_SPAN_6, display(block), gridColumnStart(auto), gridColumnEnd(span, l(6))),
        style(MAX_SPAN_7, display(block), gridColumnStart(auto), gridColumnEnd(span, l(7))),
        style(MAX_SPAN_8, display(block), gridColumnStart(auto), gridColumnEnd(span, l(8))),
        style(MAX_SPAN_9, display(block), gridColumnStart(auto), gridColumnEnd(span, l(9))),
        style(MAX_SPAN_10, display(block), gridColumnStart(auto), gridColumnEnd(span, l(10))),
        style(MAX_SPAN_11, display(block), gridColumnStart(auto), gridColumnEnd(span, l(11))),
        style(MAX_SPAN_12, display(block), gridColumnStart(auto), gridColumnEnd(span, l(12))),
        style(MAX_SPAN_13, display(block), gridColumnStart(auto), gridColumnEnd(span, l(13))),
        style(MAX_SPAN_14, display(block), gridColumnStart(auto), gridColumnEnd(span, l(14))),
        style(MAX_SPAN_15, display(block), gridColumnStart(auto), gridColumnEnd(span, l(15))),
        style(MAX_SPAN_16, display(block), gridColumnStart(auto), gridColumnEnd(span, l(16)))
      );
    }
  }

}