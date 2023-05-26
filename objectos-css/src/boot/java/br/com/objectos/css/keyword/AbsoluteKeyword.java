package br.com.objectos.css.keyword;

import br.com.objectos.css.type.PositionValue;

public final class AbsoluteKeyword extends StandardKeyword implements PositionValue {
  static final AbsoluteKeyword INSTANCE = new AbsoluteKeyword();

  private AbsoluteKeyword() {
    super(17, "absolute", "absolute");
  }
}
