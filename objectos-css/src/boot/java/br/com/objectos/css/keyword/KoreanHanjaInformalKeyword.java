package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class KoreanHanjaInformalKeyword extends StandardKeyword implements CounterStyleValue {
  static final KoreanHanjaInformalKeyword INSTANCE = new KoreanHanjaInformalKeyword();

  private KoreanHanjaInformalKeyword() {
    super(126, "koreanHanjaInformal", "korean-hanja-informal");
  }
}
