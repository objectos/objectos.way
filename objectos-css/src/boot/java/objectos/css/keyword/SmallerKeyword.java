package objectos.css.keyword;

import objectos.css.type.FontSizeValue;

public final class SmallerKeyword extends StandardKeyword implements FontSizeValue {
  static final SmallerKeyword INSTANCE = new SmallerKeyword();

  private SmallerKeyword() {
    super(222, "smaller", "smaller");
  }
}
