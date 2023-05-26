package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AppearanceValue;

public final class MenulistKeyword extends StandardKeyword implements AppearanceValue {
  static final MenulistKeyword INSTANCE = new MenulistKeyword();

  private MenulistKeyword() {
    super(149, "menulist", "menulist");
  }
}
