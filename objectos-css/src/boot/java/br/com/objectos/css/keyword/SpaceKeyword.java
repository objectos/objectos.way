package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BackgroundRepeatArity2Value;

public final class SpaceKeyword extends StandardKeyword implements BackgroundRepeatArity2Value {
  static final SpaceKeyword INSTANCE = new SpaceKeyword();

  private SpaceKeyword() {
    super(224, "space", "space");
  }
}
