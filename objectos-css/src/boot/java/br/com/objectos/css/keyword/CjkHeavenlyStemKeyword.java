package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class CjkHeavenlyStemKeyword extends StandardKeyword implements CounterStyleValue {
  static final CjkHeavenlyStemKeyword INSTANCE = new CjkHeavenlyStemKeyword();

  private CjkHeavenlyStemKeyword() {
    super(44, "cjkHeavenlyStem", "cjk-heavenly-stem");
  }
}
