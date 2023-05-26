package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FontSizeValue;

public final class SmallerKeyword extends StandardKeyword implements FontSizeValue {
  static final SmallerKeyword INSTANCE = new SmallerKeyword();

  private SmallerKeyword() {
    super(222, "smaller", "smaller");
  }
}
