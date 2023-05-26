package objectos.css.keyword;

import objectos.css.type.AppearanceValue;

public final class ButtonBevelKeyword extends StandardKeyword implements AppearanceValue {
  static final ButtonBevelKeyword INSTANCE = new ButtonBevelKeyword();

  private ButtonBevelKeyword() {
    super(34, "buttonBevel", "button-bevel");
  }
}
