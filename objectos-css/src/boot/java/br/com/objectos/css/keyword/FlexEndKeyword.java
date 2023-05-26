package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ContentPosition;
import br.com.objectos.css.type.SelfPosition;

public final class FlexEndKeyword extends StandardKeyword implements ContentPosition, SelfPosition {
  static final FlexEndKeyword INSTANCE = new FlexEndKeyword();

  private FlexEndKeyword() {
    super(81, "flexEnd", "flex-end");
  }
}
