package objectos.css.keyword;

import objectos.css.type.BackgroundRepeatArity1Value;

public final class RepeatXKeyword extends StandardKeyword implements BackgroundRepeatArity1Value {
  static final RepeatXKeyword INSTANCE = new RepeatXKeyword();

  private RepeatXKeyword() {
    super(191, "repeatX", "repeat-x");
  }
}
