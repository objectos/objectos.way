package br.com.objectos.css.keyword;

import br.com.objectos.css.type.SystemFontValue;

public final class IconKeyword extends StandardKeyword implements SystemFontValue {
  static final IconKeyword INSTANCE = new IconKeyword();

  private IconKeyword() {
    super(103, "icon", "icon");
  }
}
