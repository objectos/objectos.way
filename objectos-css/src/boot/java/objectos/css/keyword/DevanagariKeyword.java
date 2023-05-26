package objectos.css.keyword;

import objectos.css.type.CounterStyleValue;

public final class DevanagariKeyword extends StandardKeyword implements CounterStyleValue {
  static final DevanagariKeyword INSTANCE = new DevanagariKeyword();

  private DevanagariKeyword() {
    super(65, "devanagari", "devanagari");
  }
}
