package objectos.css.keyword;

import objectos.css.type.FontSizeValue;

public final class XSmallKeyword extends StandardKeyword implements FontSizeValue {
  static final XSmallKeyword INSTANCE = new XSmallKeyword();

  private XSmallKeyword() {
    super(279, "xSmall", "x-small");
  }
}
