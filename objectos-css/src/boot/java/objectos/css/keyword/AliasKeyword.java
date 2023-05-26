package objectos.css.keyword;

import objectos.css.type.CursorValue;

public final class AliasKeyword extends StandardKeyword implements CursorValue {
  static final AliasKeyword INSTANCE = new AliasKeyword();

  private AliasKeyword() {
    super(18, "alias", "alias");
  }
}
