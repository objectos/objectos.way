package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayOutsideValue;
import br.com.objectos.css.type.ResizeValue;

public final class InlineKeyword extends StandardKeyword implements DisplayOutsideValue, ResizeValue {
  static final InlineKeyword INSTANCE = new InlineKeyword();

  private InlineKeyword() {
    super(106, "inline", "inline");
  }
}
