package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FontSizeValue;

public final class XxSmallKeyword extends StandardKeyword implements FontSizeValue {
  static final XxSmallKeyword INSTANCE = new XxSmallKeyword();

  private XxSmallKeyword() {
    super(281, "xxSmall", "xx-small");
  }
}
