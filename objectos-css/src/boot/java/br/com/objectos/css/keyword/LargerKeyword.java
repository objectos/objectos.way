package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FontSizeValue;

public final class LargerKeyword extends StandardKeyword implements FontSizeValue {
  static final LargerKeyword INSTANCE = new LargerKeyword();

  private LargerKeyword() {
    super(129, "larger", "larger");
  }
}
