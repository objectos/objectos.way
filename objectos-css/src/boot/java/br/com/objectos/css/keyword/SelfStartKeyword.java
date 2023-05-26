package br.com.objectos.css.keyword;

import br.com.objectos.css.type.SelfPosition;

public final class SelfStartKeyword extends StandardKeyword implements SelfPosition {
  static final SelfStartKeyword INSTANCE = new SelfStartKeyword();

  private SelfStartKeyword() {
    super(213, "selfStart", "self-start");
  }
}
