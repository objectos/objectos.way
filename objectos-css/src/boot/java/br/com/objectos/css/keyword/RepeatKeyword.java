package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BackgroundRepeatArity2Value;

public final class RepeatKeyword extends StandardKeyword implements BackgroundRepeatArity2Value {
  static final RepeatKeyword INSTANCE = new RepeatKeyword();

  private RepeatKeyword() {
    super(190, "repeat", "repeat");
  }
}
