package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BaselinePosition;

public final class LastKeyword extends StandardKeyword implements BaselinePosition {
  static final LastKeyword INSTANCE = new LastKeyword();

  private LastKeyword() {
    super(130, "last", "last");
  }
}
