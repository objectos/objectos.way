package objectos.css.keyword;

import objectos.css.type.ClearValue;
import objectos.css.type.FloatValue;

public final class InlineStartKeyword extends StandardKeyword implements ClearValue, FloatValue {
  static final InlineStartKeyword INSTANCE = new InlineStartKeyword();

  private InlineStartKeyword() {
    super(111, "inlineStart", "inline-start");
  }
}
