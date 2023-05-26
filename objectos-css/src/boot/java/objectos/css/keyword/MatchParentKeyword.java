package objectos.css.keyword;

import objectos.css.type.TextAlignValue;

public final class MatchParentKeyword extends StandardKeyword implements TextAlignValue {
  static final MatchParentKeyword INSTANCE = new MatchParentKeyword();

  private MatchParentKeyword() {
    super(145, "matchParent", "match-parent");
  }
}
