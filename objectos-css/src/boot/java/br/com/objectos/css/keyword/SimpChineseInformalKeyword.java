package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class SimpChineseInformalKeyword extends StandardKeyword implements CounterStyleValue {
  static final SimpChineseInformalKeyword INSTANCE = new SimpChineseInformalKeyword();

  private SimpChineseInformalKeyword() {
    super(217, "simpChineseInformal", "simp-chinese-informal");
  }
}
