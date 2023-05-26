package objectos.css.keyword;

import objectos.css.type.LineStyleValue;
import objectos.css.type.OverflowValue;

public final class HiddenKeyword extends StandardKeyword implements LineStyleValue, OverflowValue {
  static final HiddenKeyword INSTANCE = new HiddenKeyword();

  private HiddenKeyword() {
    super(99, "hidden", "hidden");
  }
}
