package br.com.objectos.css.keyword;

import br.com.objectos.css.type.VerticalAlignValue;

public final class SuperKeyword extends StandardKeyword implements VerticalAlignValue {
  static final SuperKeyword INSTANCE = new SuperKeyword();

  private SuperKeyword() {
    super(237, "superKw", "super");
  }
}
