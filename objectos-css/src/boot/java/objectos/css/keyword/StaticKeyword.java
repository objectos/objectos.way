package objectos.css.keyword;

import objectos.css.type.PositionValue;

public final class StaticKeyword extends StandardKeyword implements PositionValue {
  static final StaticKeyword INSTANCE = new StaticKeyword();

  private StaticKeyword() {
    super(232, "staticKw", "static");
  }
}
