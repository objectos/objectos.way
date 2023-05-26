package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FontSizeValue;

public final class XLargeKeyword extends StandardKeyword implements FontSizeValue {
  static final XLargeKeyword INSTANCE = new XLargeKeyword();

  private XLargeKeyword() {
    super(278, "xLarge", "x-large");
  }
}
