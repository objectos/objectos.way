package br.com.objectos.css.keyword;

import br.com.objectos.css.type.LineStyleValue;

public final class GrooveKeyword extends StandardKeyword implements LineStyleValue {
  static final GrooveKeyword INSTANCE = new GrooveKeyword();

  private GrooveKeyword() {
    super(93, "groove", "groove");
  }
}
