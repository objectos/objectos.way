package objectos.css.keyword;

import objectos.css.type.DisplayOutsideValue;

public final class RunInKeyword extends StandardKeyword implements DisplayOutsideValue {
  static final RunInKeyword INSTANCE = new RunInKeyword();

  private RunInKeyword() {
    super(204, "runIn", "runIn");
  }
}
