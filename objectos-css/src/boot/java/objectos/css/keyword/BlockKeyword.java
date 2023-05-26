package objectos.css.keyword;

import objectos.css.type.DisplayOutsideValue;
import objectos.css.type.ResizeValue;

public final class BlockKeyword extends StandardKeyword implements DisplayOutsideValue, ResizeValue {
  static final BlockKeyword INSTANCE = new BlockKeyword();

  private BlockKeyword() {
    super(26, "block", "block");
  }
}
