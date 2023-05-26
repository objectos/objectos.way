package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FontSizeValue;

public final class SmallKeyword extends StandardKeyword implements FontSizeValue {
  static final SmallKeyword INSTANCE = new SmallKeyword();

  private SmallKeyword() {
    super(219, "smallKw", "small");
  }
}
