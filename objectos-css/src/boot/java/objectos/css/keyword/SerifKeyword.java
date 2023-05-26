package objectos.css.keyword;

import objectos.css.type.FontFamilyValue;

public final class SerifKeyword extends StandardKeyword implements FontFamilyValue {
  static final SerifKeyword INSTANCE = new SerifKeyword();

  private SerifKeyword() {
    super(215, "serif", "serif");
  }
}
