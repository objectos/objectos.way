package objectos.css.keyword;

import objectos.css.type.ClearValue;
import objectos.css.type.ResizeValue;

public final class BothKeyword extends StandardKeyword implements ClearValue, ResizeValue {
  static final BothKeyword INSTANCE = new BothKeyword();

  private BothKeyword() {
    super(30, "both", "both");
  }
}
