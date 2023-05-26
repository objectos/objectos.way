package objectos.css.keyword;

import objectos.css.type.FontFamilyValue;

public final class SansSerifKeyword extends StandardKeyword implements FontFamilyValue {
  static final SansSerifKeyword INSTANCE = new SansSerifKeyword();

  private SansSerifKeyword() {
    super(207, "sansSerif", "sans-serif");
  }
}
