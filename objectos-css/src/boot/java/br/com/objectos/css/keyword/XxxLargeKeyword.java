package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FontSizeValue;

public final class XxxLargeKeyword extends StandardKeyword implements FontSizeValue {
  static final XxxLargeKeyword INSTANCE = new XxxLargeKeyword();

  private XxxLargeKeyword() {
    super(282, "xxxLarge", "xxx-large");
  }
}
