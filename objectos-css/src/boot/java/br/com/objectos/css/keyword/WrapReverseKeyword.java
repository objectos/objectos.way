package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FlexWrapValue;

public final class WrapReverseKeyword extends StandardKeyword implements FlexWrapValue {
  static final WrapReverseKeyword INSTANCE = new WrapReverseKeyword();

  private WrapReverseKeyword() {
    super(277, "wrapReverse", "wrap-reverse");
  }
}
