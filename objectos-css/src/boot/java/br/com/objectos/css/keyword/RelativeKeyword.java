package br.com.objectos.css.keyword;

import br.com.objectos.css.type.PositionValue;

public final class RelativeKeyword extends StandardKeyword implements PositionValue {
  static final RelativeKeyword INSTANCE = new RelativeKeyword();

  private RelativeKeyword() {
    super(189, "relative", "relative");
  }
}
