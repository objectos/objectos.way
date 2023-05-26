package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ContentPosition;
import br.com.objectos.css.type.SelfPosition;

public final class FlexStartKeyword extends StandardKeyword implements ContentPosition, SelfPosition {
  static final FlexStartKeyword INSTANCE = new FlexStartKeyword();

  private FlexStartKeyword() {
    super(82, "flexStart", "flex-start");
  }
}
