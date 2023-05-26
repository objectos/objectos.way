package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FontSizeValue;

public final class LargeKeyword extends StandardKeyword implements FontSizeValue {
  static final LargeKeyword INSTANCE = new LargeKeyword();

  private LargeKeyword() {
    super(128, "large", "large");
  }
}
