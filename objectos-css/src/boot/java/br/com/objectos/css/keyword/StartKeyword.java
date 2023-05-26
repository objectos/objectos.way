package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ContentPosition;
import br.com.objectos.css.type.SelfPosition;
import br.com.objectos.css.type.TextAlignValue;

public final class StartKeyword extends StandardKeyword implements ContentPosition, SelfPosition, TextAlignValue {
  static final StartKeyword INSTANCE = new StartKeyword();

  private StartKeyword() {
    super(231, "start", "start");
  }
}
