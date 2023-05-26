package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AppearanceValue;

public final class MenulistButtonKeyword extends StandardKeyword implements AppearanceValue {
  static final MenulistButtonKeyword INSTANCE = new MenulistButtonKeyword();

  private MenulistButtonKeyword() {
    super(150, "menulistButton", "menulist-button");
  }
}
