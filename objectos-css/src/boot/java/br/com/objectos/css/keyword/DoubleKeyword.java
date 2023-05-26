package br.com.objectos.css.keyword;

import br.com.objectos.css.type.LineStyleValue;
import br.com.objectos.css.type.TextDecorationStyleValue;

public final class DoubleKeyword extends StandardKeyword implements LineStyleValue, TextDecorationStyleValue {
  static final DoubleKeyword INSTANCE = new DoubleKeyword();

  private DoubleKeyword() {
    super(70, "doubleKw", "double");
  }
}
