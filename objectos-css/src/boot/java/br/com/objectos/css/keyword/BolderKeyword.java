package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FontWeightValue;

public final class BolderKeyword extends StandardKeyword implements FontWeightValue {
  static final BolderKeyword INSTANCE = new BolderKeyword();

  private BolderKeyword() {
    super(28, "bolder", "bolder");
  }
}
