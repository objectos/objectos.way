package objectos.css.keyword;

import objectos.css.type.FlexWrapValue;
import objectos.css.type.WhiteSpaceValue;

public final class NowrapKeyword extends StandardKeyword implements FlexWrapValue, WhiteSpaceValue {
  static final NowrapKeyword INSTANCE = new NowrapKeyword();

  private NowrapKeyword() {
    super(169, "nowrap", "nowrap");
  }
}
