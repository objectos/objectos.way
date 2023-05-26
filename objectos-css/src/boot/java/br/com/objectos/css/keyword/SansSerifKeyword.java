package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FontFamilyValue;

public final class SansSerifKeyword extends StandardKeyword implements FontFamilyValue {
  static final SansSerifKeyword INSTANCE = new SansSerifKeyword();

  private SansSerifKeyword() {
    super(207, "sansSerif", "sans-serif");
  }
}
