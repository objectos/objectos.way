package br.com.objectos.css.keyword;

import br.com.objectos.css.type.TextIndentOptionalValue;

public final class EachLineKeyword extends StandardKeyword implements TextIndentOptionalValue {
  static final EachLineKeyword INSTANCE = new EachLineKeyword();

  private EachLineKeyword() {
    super(72, "eachLine", "each-line");
  }
}
