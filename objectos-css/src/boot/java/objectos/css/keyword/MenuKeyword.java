package objectos.css.keyword;

import objectos.css.type.SystemFontValue;

public final class MenuKeyword extends StandardKeyword implements SystemFontValue {
  static final MenuKeyword INSTANCE = new MenuKeyword();

  private MenuKeyword() {
    super(148, "menuKw", "menu");
  }
}
