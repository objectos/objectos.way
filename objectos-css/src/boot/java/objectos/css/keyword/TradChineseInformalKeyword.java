package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class TradChineseInformalKeyword extends StandardKeyword implements CounterStyleValue {
  static final TradChineseInformalKeyword INSTANCE = new TradChineseInformalKeyword();

  private TradChineseInformalKeyword() {
    super(261, "tradChineseInformal", "trad-chinese-informal");
  }
}
