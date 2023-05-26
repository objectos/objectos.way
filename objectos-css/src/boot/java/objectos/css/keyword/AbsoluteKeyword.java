package objectos.css.keyword;

import objectos.css.type.PositionValue;

public final class AbsoluteKeyword extends StandardKeyword implements PositionValue {
  static final AbsoluteKeyword INSTANCE = new AbsoluteKeyword();

  private AbsoluteKeyword() {
    super(17, "absolute", "absolute");
  }
}
