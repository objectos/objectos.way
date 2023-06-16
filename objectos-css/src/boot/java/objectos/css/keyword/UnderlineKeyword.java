package objectos.css.keyword;

import objectos.css.type.TextDecorationLineKind;

public final class UnderlineKeyword extends StandardKeyword implements TextDecorationLineKind {
  static final UnderlineKeyword INSTANCE = new UnderlineKeyword();

  private UnderlineKeyword() {
    super(262, "underline", "underline");
  }
}