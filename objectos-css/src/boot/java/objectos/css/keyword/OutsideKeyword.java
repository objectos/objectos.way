package objectos.css.keyword;

import objectos.css.type.ListStylePositionValue;

public final class OutsideKeyword extends StandardKeyword implements ListStylePositionValue {
  static final OutsideKeyword INSTANCE = new OutsideKeyword();

  private OutsideKeyword() {
    super(177, "outside", "outside");
  }
}
