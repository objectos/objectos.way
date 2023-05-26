package objectos.css.keyword;

import objectos.css.type.AppearanceValue;

public final class MeterKeyword extends StandardKeyword implements AppearanceValue {
  static final MeterKeyword INSTANCE = new MeterKeyword();

  private MeterKeyword() {
    super(152, "meter", "meter");
  }
}
