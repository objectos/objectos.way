package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class MozCjkHeavenlyStemKeyword extends StandardKeyword implements CounterStyleValue {
  static final MozCjkHeavenlyStemKeyword INSTANCE = new MozCjkHeavenlyStemKeyword();

  private MozCjkHeavenlyStemKeyword() {
    super(3, "mozCjkHeavenlyStem", "-moz-cjk-heavenly-stem");
  }
}
