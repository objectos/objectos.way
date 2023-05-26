package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class KoreanHanjaFormalKeyword extends StandardKeyword implements CounterStyleValue {
  static final KoreanHanjaFormalKeyword INSTANCE = new KoreanHanjaFormalKeyword();

  private KoreanHanjaFormalKeyword() {
    super(125, "koreanHanjaFormal", "korean-hanja-formal");
  }
}
