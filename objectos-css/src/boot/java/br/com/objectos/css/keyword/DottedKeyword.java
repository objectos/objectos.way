package br.com.objectos.css.keyword;

import br.com.objectos.css.type.LineStyleValue;
import br.com.objectos.css.type.TextDecorationStyleValue;

public final class DottedKeyword extends StandardKeyword implements LineStyleValue, TextDecorationStyleValue {
  static final DottedKeyword INSTANCE = new DottedKeyword();

  private DottedKeyword() {
    super(69, "dotted", "dotted");
  }
}
