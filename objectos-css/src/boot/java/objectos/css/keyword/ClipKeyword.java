package objectos.css.keyword;

import objectos.css.type.OverflowValue;

public final class ClipKeyword extends StandardKeyword implements OverflowValue {
  static final ClipKeyword INSTANCE = new ClipKeyword();

  private ClipKeyword() {
    super(46, "clip", "clip");
  }
}
