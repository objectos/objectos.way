package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class KoreanHangulFormalKeyword extends StandardKeyword implements CounterStyleValue {
  static final KoreanHangulFormalKeyword INSTANCE = new KoreanHangulFormalKeyword();

  private KoreanHangulFormalKeyword() {
    super(124, "koreanHangulFormal", "korean-hangul-formal");
  }
}
