package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class SimpChineseFormalKeyword extends StandardKeyword implements CounterStyleValue {
  static final SimpChineseFormalKeyword INSTANCE = new SimpChineseFormalKeyword();

  private SimpChineseFormalKeyword() {
    super(216, "simpChineseFormal", "simp-chinese-formal");
  }
}
