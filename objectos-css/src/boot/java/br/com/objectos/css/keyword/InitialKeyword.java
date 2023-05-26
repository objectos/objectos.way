package br.com.objectos.css.keyword;

import br.com.objectos.css.type.GlobalKeyword;

public final class InitialKeyword extends StandardKeyword implements GlobalKeyword {
  static final InitialKeyword INSTANCE = new InitialKeyword();

  private InitialKeyword() {
    super(105, "initial", "initial");
  }
}
