package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ObjectFitValue;

public final class FillKeyword extends StandardKeyword implements ObjectFitValue {
  static final FillKeyword INSTANCE = new FillKeyword();

  private FillKeyword() {
    super(77, "fill", "fill");
  }
}
