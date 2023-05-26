package br.com.objectos.css.keyword;

import br.com.objectos.css.type.GlobalKeyword;

public final class InheritKeyword extends StandardKeyword implements GlobalKeyword {
  static final InheritKeyword INSTANCE = new InheritKeyword();

  private InheritKeyword() {
    super(104, "inherit", "inherit");
  }
}
