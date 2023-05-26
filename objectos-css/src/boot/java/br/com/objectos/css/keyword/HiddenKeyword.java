package br.com.objectos.css.keyword;

import br.com.objectos.css.type.LineStyleValue;
import br.com.objectos.css.type.OverflowValue;

public final class HiddenKeyword extends StandardKeyword implements LineStyleValue, OverflowValue {
  static final HiddenKeyword INSTANCE = new HiddenKeyword();

  private HiddenKeyword() {
    super(99, "hidden", "hidden");
  }
}
