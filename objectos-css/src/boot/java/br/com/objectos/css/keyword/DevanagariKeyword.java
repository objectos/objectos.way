package br.com.objectos.css.keyword;

import br.com.objectos.css.type.CounterStyleValue;

public final class DevanagariKeyword extends StandardKeyword implements CounterStyleValue {
  static final DevanagariKeyword INSTANCE = new DevanagariKeyword();

  private DevanagariKeyword() {
    super(65, "devanagari", "devanagari");
  }
}
