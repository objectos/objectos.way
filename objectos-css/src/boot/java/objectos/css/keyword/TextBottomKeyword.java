package objectos.css.keyword;

import objectos.css.type.VerticalAlignValue;

public final class TextBottomKeyword extends StandardKeyword implements VerticalAlignValue {
  static final TextBottomKeyword INSTANCE = new TextBottomKeyword();

  private TextBottomKeyword() {
    super(251, "textBottom", "text-bottom");
  }
}
