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
package objectos.carbonated;

import objectos.css.CssTemplate;
import objectos.css.util.ClassSelector;
import objectos.html.HtmlComponent;
import objectos.html.HtmlTemplate;
import objectos.html.tmpl.Instruction;
import objectos.html.tmpl.Instruction.ElementContents;
import objectos.lang.Check;

public final class Button extends HtmlComponent {

  public enum Kind {

    PRIMARY;

  }

  public enum Size {

    SMALL,

    MEDIUM,

    LARGE,

    X_LARGE,

    X_LARGE_2;

  }

  private static final ClassSelector BTN = U.cs("btn");

  private static final ClassSelector SIZE_SM = U.cs("btn-sm");
  private static final ClassSelector SIZE_MD = U.cs("btn-md");
  private static final ClassSelector SIZE_LG = U.cs("btn-lg");
  private static final ClassSelector SIZE_XL = U.cs("btn-xl");
  private static final ClassSelector SIZE_2XL = U.cs("btn-2xl");

  private static final ClassSelector KIND_PRIMARY = U.cs("btn-primary");

  private Button.Kind kind;

  private Size size;

  public Button(HtmlTemplate parent) {
    super(parent);

    reset();
  }

  public final Button kind(Kind value) {
    kind = Check.notNull(value, "value == null");
    return this;
  }

  public final Button size(Size value) {
    size = Check.notNull(value, "value == null");
    return this;
  }

  public final ElementContents render(Instruction... contents) {
    ElementContents btn;
    btn = button(
      BTN,

      switch (size) {
        case SMALL -> SIZE_SM;

        case MEDIUM -> SIZE_MD;

        case LARGE -> SIZE_LG;

        case X_LARGE -> SIZE_XL;

        case X_LARGE_2 -> SIZE_2XL;
      },

      switch (kind) {
        case PRIMARY -> KIND_PRIMARY;
      },

      flatten(contents)
    );

    reset();

    return btn;
  }

  private void reset() {
    size = Size.LARGE;

    kind = Button.Kind.PRIMARY;
  }

  static final class Styles extends CssTemplate {

    @Override
    protected final void definition() {
      style(
        BTN,

        fontSize(var(Typography.BODY_COMPACT_01_FONT_SIZE)),
        fontWeight(var(Typography.BODY_COMPACT_01_FONT_WEIGHT)),
        letterSpacing(var(Typography.BODY_COMPACT_01_LETTER_SPACING)),
        lineHeight(var(Typography.BODY_COMPACT_01_LINE_HEIGHT)),

        //border-radius: $button-border-radius;
        cursor(pointer),
        display(inlineFlex),
        flexShrink(0),
        justifyContent(spaceBetween),
        margin($0),
        maxWidth(rem(320 / 16)),
        outline(none),
        paddingInline(px(15), px(63)),
        position(relative),
        textAlign(left),
        textDecoration(none),
        verticalAlign(top),
        width(maxContent)
      /*
      transition: background $duration-fast-01 motion(entrance, productive),
        box-shadow $duration-fast-01 motion(entrance, productive),
        border-color $duration-fast-01 motion(entrance, productive),
        outline $duration-fast-01 motion(entrance, productive);
      */
      );

      style(
        SIZE_SM,

        minHeight(var(Layout.SIZE_HEIGHT_SM)),
        paddingBlock(px(6))
      );

      style(
        SIZE_MD,

        minHeight(var(Layout.SIZE_HEIGHT_MD)),
        paddingBlock(px(10))
      );

      style(
        SIZE_LG,

        minHeight(var(Layout.SIZE_HEIGHT_LG)),
        paddingBlock(px(14))
      );

      style(
        SIZE_XL,

        minHeight(var(Layout.SIZE_HEIGHT_XL)),
        paddingBlock(px(14))
      );

      style(
        SIZE_2XL,

        minHeight(var(Layout.SIZE_HEIGHT_2XL)),
        paddingBlock(px(14))
      );

      style(
        KIND_PRIMARY,

        borderWidth(px(1)),
        borderStyle(solid),
        borderColor(transparent),
        backgroundColor(var(Theme.BUTTON_PRIMARY, hex("#0f62fe"))),
        color(var(Theme.TEXT_ON_COLOR, hex("#ffffff")))
      );

      style(
        KIND_PRIMARY, _active,

        backgroundColor(var(Theme.BUTTON_PRIMARY_ACTIVE, hex("#002d9c")))
      );

      style(
        KIND_PRIMARY, _focus,

        borderColor(var(Theme.FOCUS, hex("#0f62fe"))),
        boxShadow(
          boxShadow(inset, $0, $0, $0, px(1), var(Theme.FOCUS, hex("#0f62fe"))),
          boxShadow(inset, $0, $0, $0, px(2), var(Theme.BACKGROUND, hex("#ffffff")))
        )
      );

      style(
        KIND_PRIMARY, _hover,

        backgroundColor(var(Theme.BUTTON_PRIMARY_HOVER, hex("#0050e6")))
      );
    }

  }
}