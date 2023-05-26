package objectos.css.keyword;

import objectos.css.type.FontFamilyValue;

public final class FantasyKeyword extends StandardKeyword implements FontFamilyValue {
  static final FantasyKeyword INSTANCE = new FantasyKeyword();

  private FantasyKeyword() {
    super(76, "fantasy", "fantasy");
  }
}
