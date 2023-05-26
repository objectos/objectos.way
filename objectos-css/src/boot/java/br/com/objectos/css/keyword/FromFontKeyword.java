package br.com.objectos.css.keyword;

import br.com.objectos.css.type.TextDecorationThicknessValue;

public final class FromFontKeyword extends StandardKeyword implements TextDecorationThicknessValue {
  static final FromFontKeyword INSTANCE = new FromFontKeyword();

  private FromFontKeyword() {
    super(85, "fromFont", "from-font");
  }
}
