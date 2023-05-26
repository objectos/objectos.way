package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ClearValue;
import br.com.objectos.css.type.FloatValue;

public final class InlineStartKeyword extends StandardKeyword implements ClearValue, FloatValue {
  static final InlineStartKeyword INSTANCE = new InlineStartKeyword();

  private InlineStartKeyword() {
    super(111, "inlineStart", "inline-start");
  }
}
