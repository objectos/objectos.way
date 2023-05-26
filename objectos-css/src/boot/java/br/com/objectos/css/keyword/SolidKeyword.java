package br.com.objectos.css.keyword;

import br.com.objectos.css.type.LineStyleValue;
import br.com.objectos.css.type.TextDecorationStyleValue;

public final class SolidKeyword extends StandardKeyword implements LineStyleValue, TextDecorationStyleValue {
  static final SolidKeyword INSTANCE = new SolidKeyword();

  private SolidKeyword() {
    super(223, "solid", "solid");
  }
}
