package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class NotAllowedKeyword extends StandardKeyword implements CursorValue {
  static final NotAllowedKeyword INSTANCE = new NotAllowedKeyword();

  private NotAllowedKeyword() {
    super(168, "notAllowed", "not-allowed");
  }
}
