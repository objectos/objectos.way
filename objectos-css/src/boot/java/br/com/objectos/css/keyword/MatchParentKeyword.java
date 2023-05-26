package br.com.objectos.css.keyword;

import br.com.objectos.css.type.TextAlignValue;

public final class MatchParentKeyword extends StandardKeyword implements TextAlignValue {
  static final MatchParentKeyword INSTANCE = new MatchParentKeyword();

  private MatchParentKeyword() {
    super(145, "matchParent", "match-parent");
  }
}
