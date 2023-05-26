package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class TradChineseInformalKeyword extends StandardKeyword implements CounterStyleValue {
  static final TradChineseInformalKeyword INSTANCE = new TradChineseInformalKeyword();

  private TradChineseInformalKeyword() {
    super(261, "tradChineseInformal", "trad-chinese-informal");
  }
}
