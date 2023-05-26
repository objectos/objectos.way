package objectos.css.keyword;

import objectos.css.type.FontWeightValue;

public final class LighterKeyword extends StandardKeyword implements FontWeightValue {
  static final LighterKeyword INSTANCE = new LighterKeyword();

  private LighterKeyword() {
    super(133, "lighter", "lighter");
  }
}
