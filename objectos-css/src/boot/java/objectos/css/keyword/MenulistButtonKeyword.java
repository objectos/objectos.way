package objectos.css.keyword;

import objectos.css.type.AppearanceValue;

public final class MenulistButtonKeyword extends StandardKeyword implements AppearanceValue {
  static final MenulistButtonKeyword INSTANCE = new MenulistButtonKeyword();

  private MenulistButtonKeyword() {
    super(150, "menulistButton", "menulist-button");
  }
}
