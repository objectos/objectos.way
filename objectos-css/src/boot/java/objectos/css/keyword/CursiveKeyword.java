package objectos.css.keyword;

import objectos.css.type.FontFamilyValue;

public final class CursiveKeyword extends StandardKeyword implements FontFamilyValue {
  static final CursiveKeyword INSTANCE = new CursiveKeyword();

  private CursiveKeyword() {
    super(60, "cursive", "cursive");
  }
}
