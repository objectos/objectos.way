package objectos.css.keyword;

import objectos.css.type.FontSizeValue;

public final class XxLargeKeyword extends StandardKeyword implements FontSizeValue {
  static final XxLargeKeyword INSTANCE = new XxLargeKeyword();

  private XxLargeKeyword() {
    super(280, "xxLarge", "xx-large");
  }
}