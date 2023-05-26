package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ClearValue;
import br.com.objectos.css.type.ResizeValue;

public final class BothKeyword extends StandardKeyword implements ClearValue, ResizeValue {
  static final BothKeyword INSTANCE = new BothKeyword();

  private BothKeyword() {
    super(30, "both", "both");
  }
}
