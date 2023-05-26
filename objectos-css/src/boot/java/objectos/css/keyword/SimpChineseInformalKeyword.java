package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class SimpChineseInformalKeyword extends StandardKeyword implements CounterStyleValue {
  static final SimpChineseInformalKeyword INSTANCE = new SimpChineseInformalKeyword();

  private SimpChineseInformalKeyword() {
    super(217, "simpChineseInformal", "simp-chinese-informal");
  }
}
