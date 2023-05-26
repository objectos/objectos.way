package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ContentPosition;
import br.com.objectos.css.type.SelfPosition;
import br.com.objectos.css.type.TextAlignValue;

public final class EndKeyword extends StandardKeyword implements ContentPosition, SelfPosition, TextAlignValue {
  static final EndKeyword INSTANCE = new EndKeyword();

  private EndKeyword() {
    super(73, "end", "end");
  }
}
