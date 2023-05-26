package br.com.objectos.css.keyword;

import br.com.objectos.css.type.WhiteSpaceValue;

public final class PreKeyword extends StandardKeyword implements WhiteSpaceValue {
  static final PreKeyword INSTANCE = new PreKeyword();

  private PreKeyword() {
    super(182, "preKw", "pre");
  }
}
