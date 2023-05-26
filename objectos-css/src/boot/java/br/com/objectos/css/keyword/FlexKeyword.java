package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInsideValue;

public final class FlexKeyword extends StandardKeyword implements DisplayInsideValue {
  static final FlexKeyword INSTANCE = new FlexKeyword();

  private FlexKeyword() {
    super(80, "flex", "flex");
  }
}
