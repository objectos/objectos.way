package objectos.css.keyword;

import objectos.css.type.AppearanceValue;

public final class ButtonKeyword extends StandardKeyword implements AppearanceValue {
  static final ButtonKeyword INSTANCE = new ButtonKeyword();

  private ButtonKeyword() {
    super(33, "buttonKw", "button");
  }
}
