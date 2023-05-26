package br.com.objectos.css.keyword;

import br.com.objectos.css.type.WhiteSpaceValue;

public final class PreLineKeyword extends StandardKeyword implements WhiteSpaceValue {
  static final PreLineKeyword INSTANCE = new PreLineKeyword();

  private PreLineKeyword() {
    super(183, "preLine", "pre-line");
  }
}
