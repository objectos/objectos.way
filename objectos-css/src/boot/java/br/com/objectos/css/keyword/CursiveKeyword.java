package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FontFamilyValue;

public final class CursiveKeyword extends StandardKeyword implements FontFamilyValue {
  static final CursiveKeyword INSTANCE = new CursiveKeyword();

  private CursiveKeyword() {
    super(60, "cursive", "cursive");
  }
}
