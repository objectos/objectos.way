package br.com.objectos.css.keyword;

import br.com.objectos.css.type.WhiteSpaceValue;

public final class PreWrapKeyword extends StandardKeyword implements WhiteSpaceValue {
  static final PreWrapKeyword INSTANCE = new PreWrapKeyword();

  private PreWrapKeyword() {
    super(184, "preWrap", "pre-wrap");
  }
}
