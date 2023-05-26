package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class KoreanHangulFormalKeyword extends StandardKeyword implements CounterStyleValue {
  static final KoreanHangulFormalKeyword INSTANCE = new KoreanHangulFormalKeyword();

  private KoreanHangulFormalKeyword() {
    super(124, "koreanHangulFormal", "korean-hangul-formal");
  }
}
