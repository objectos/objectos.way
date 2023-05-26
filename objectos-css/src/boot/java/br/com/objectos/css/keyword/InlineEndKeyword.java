package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ClearValue;
import br.com.objectos.css.type.FloatValue;

public final class InlineEndKeyword extends StandardKeyword implements ClearValue, FloatValue {
  static final InlineEndKeyword INSTANCE = new InlineEndKeyword();

  private InlineEndKeyword() {
    super(108, "inlineEnd", "inline-end");
  }
}
