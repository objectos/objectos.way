package br.com.objectos.css.keyword;

import br.com.objectos.css.type.LineStyleValue;
import br.com.objectos.css.type.TextDecorationStyleValue;

public final class DashedKeyword extends StandardKeyword implements LineStyleValue, TextDecorationStyleValue {
  static final DashedKeyword INSTANCE = new DashedKeyword();

  private DashedKeyword() {
    super(61, "dashed", "dashed");
  }
}
