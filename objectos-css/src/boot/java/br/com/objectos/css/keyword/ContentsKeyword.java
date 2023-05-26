package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayBoxValue;

public final class ContentsKeyword extends StandardKeyword implements DisplayBoxValue {
  static final ContentsKeyword INSTANCE = new ContentsKeyword();

  private ContentsKeyword() {
    super(55, "contents", "contents");
  }
}
