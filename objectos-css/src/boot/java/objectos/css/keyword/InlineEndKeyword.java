package objectos.css.keyword;

import objectos.css.type.ClearValue;
import objectos.css.type.FloatValue;

public final class InlineEndKeyword extends StandardKeyword implements ClearValue, FloatValue {
  static final InlineEndKeyword INSTANCE = new InlineEndKeyword();

  private InlineEndKeyword() {
    super(108, "inlineEnd", "inline-end");
  }
}
