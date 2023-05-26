package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BackgroundPositionValue;
import br.com.objectos.css.type.VerticalAlignValue;

public final class TopKeyword extends StandardKeyword implements BackgroundPositionValue, VerticalAlignValue {
  static final TopKeyword INSTANCE = new TopKeyword();

  private TopKeyword() {
    super(259, "top", "top");
  }
}
