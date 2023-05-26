package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayOutsideValue;
import br.com.objectos.css.type.ResizeValue;

public final class BlockKeyword extends StandardKeyword implements DisplayOutsideValue, ResizeValue {
  static final BlockKeyword INSTANCE = new BlockKeyword();

  private BlockKeyword() {
    super(26, "block", "block");
  }
}
