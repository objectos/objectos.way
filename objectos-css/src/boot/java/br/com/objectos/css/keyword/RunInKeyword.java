package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayOutsideValue;

public final class RunInKeyword extends StandardKeyword implements DisplayOutsideValue {
  static final RunInKeyword INSTANCE = new RunInKeyword();

  private RunInKeyword() {
    super(204, "runIn", "runIn");
  }
}
