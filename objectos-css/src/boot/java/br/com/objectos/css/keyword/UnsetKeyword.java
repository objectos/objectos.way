package br.com.objectos.css.keyword;

import br.com.objectos.css.type.GlobalKeyword;

public final class UnsetKeyword extends StandardKeyword implements GlobalKeyword {
  static final UnsetKeyword INSTANCE = new UnsetKeyword();

  private UnsetKeyword() {
    super(264, "unset", "unset");
  }
}
