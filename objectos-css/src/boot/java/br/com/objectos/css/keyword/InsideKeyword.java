package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ListStylePositionValue;

public final class InsideKeyword extends StandardKeyword implements ListStylePositionValue {
  static final InsideKeyword INSTANCE = new InsideKeyword();

  private InsideKeyword() {
    super(114, "inside", "inside");
  }
}
