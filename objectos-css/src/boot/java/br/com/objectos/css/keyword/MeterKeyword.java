package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AppearanceValue;

public final class MeterKeyword extends StandardKeyword implements AppearanceValue {
  static final MeterKeyword INSTANCE = new MeterKeyword();

  private MeterKeyword() {
    super(152, "meter", "meter");
  }
}
