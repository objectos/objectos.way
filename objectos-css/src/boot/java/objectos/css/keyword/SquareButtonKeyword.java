package objectos.css.keyword;

import objectos.css.type.AppearanceValue;

public final class SquareButtonKeyword extends StandardKeyword implements AppearanceValue {
  static final SquareButtonKeyword INSTANCE = new SquareButtonKeyword();

  private SquareButtonKeyword() {
    super(230, "squareButton", "square-button");
  }
}
