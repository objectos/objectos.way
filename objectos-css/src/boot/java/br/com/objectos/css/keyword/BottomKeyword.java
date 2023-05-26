package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BackgroundPositionValue;
import br.com.objectos.css.type.VerticalAlignValue;

public final class BottomKeyword extends StandardKeyword implements BackgroundPositionValue, VerticalAlignValue {
  static final BottomKeyword INSTANCE = new BottomKeyword();

  private BottomKeyword() {
    super(31, "bottom", "bottom");
  }
}
