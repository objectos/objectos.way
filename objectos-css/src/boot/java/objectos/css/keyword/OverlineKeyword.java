package objectos.css.keyword;

import objectos.css.type.TextDecorationLineKind;

public final class OverlineKeyword extends StandardKeyword implements TextDecorationLineKind {
  static final OverlineKeyword INSTANCE = new OverlineKeyword();

  private OverlineKeyword() {
    super(178, "overline", "overline");
  }
}
