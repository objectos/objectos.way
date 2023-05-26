package br.com.objectos.css.keyword;

import br.com.objectos.css.type.VerticalAlignValue;

public final class SubKeyword extends StandardKeyword implements VerticalAlignValue {
  static final SubKeyword INSTANCE = new SubKeyword();

  private SubKeyword() {
    super(236, "subKw", "sub");
  }
}
