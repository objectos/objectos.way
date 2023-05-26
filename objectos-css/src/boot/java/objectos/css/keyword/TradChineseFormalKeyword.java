package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class TradChineseFormalKeyword extends StandardKeyword implements CounterStyleValue {
  static final TradChineseFormalKeyword INSTANCE = new TradChineseFormalKeyword();

  private TradChineseFormalKeyword() {
    super(260, "tradChineseFormal", "trad-chinese-formal");
  }
}
