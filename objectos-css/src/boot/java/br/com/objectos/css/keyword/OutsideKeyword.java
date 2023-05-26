package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ListStylePositionValue;

public final class OutsideKeyword extends StandardKeyword implements ListStylePositionValue {
  static final OutsideKeyword INSTANCE = new OutsideKeyword();

  private OutsideKeyword() {
    super(177, "outside", "outside");
  }
}
