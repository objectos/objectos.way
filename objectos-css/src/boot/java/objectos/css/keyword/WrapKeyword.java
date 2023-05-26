package objectos.css.keyword;

import objectos.css.type.FlexWrapValue;

public final class WrapKeyword extends StandardKeyword implements FlexWrapValue {
  static final WrapKeyword INSTANCE = new WrapKeyword();

  private WrapKeyword() {
    super(276, "wrap", "wrap");
  }
}
