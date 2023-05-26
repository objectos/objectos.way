package objectos.css.keyword;

import objectos.css.type.ListStylePositionValue;

public final class InsideKeyword extends StandardKeyword implements ListStylePositionValue {
  static final InsideKeyword INSTANCE = new InsideKeyword();

  private InsideKeyword() {
    super(114, "inside", "inside");
  }
}
