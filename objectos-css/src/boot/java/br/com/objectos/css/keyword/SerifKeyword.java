package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FontFamilyValue;

public final class SerifKeyword extends StandardKeyword implements FontFamilyValue {
  static final SerifKeyword INSTANCE = new SerifKeyword();

  private SerifKeyword() {
    super(215, "serif", "serif");
  }
}
