package objectos.css.keyword;

import objectos.css.type.BackgroundRepeatArity1Value;

public final class RepeatYKeyword extends StandardKeyword implements BackgroundRepeatArity1Value {
  static final RepeatYKeyword INSTANCE = new RepeatYKeyword();

  private RepeatYKeyword() {
    super(192, "repeatY", "repeat-y");
  }
}
