package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class ArabicIndicKeyword extends StandardKeyword implements CounterStyleValue {
  static final ArabicIndicKeyword INSTANCE = new ArabicIndicKeyword();

  private ArabicIndicKeyword() {
    super(20, "arabicIndic", "arabic-indic");
  }
}
