package objectos.css.keyword;

import objectos.css.type.VerticalAlignValue;

public final class SuperKeyword extends StandardKeyword implements VerticalAlignValue {
  static final SuperKeyword INSTANCE = new SuperKeyword();

  private SuperKeyword() {
    super(237, "superKw", "super");
  }
}
