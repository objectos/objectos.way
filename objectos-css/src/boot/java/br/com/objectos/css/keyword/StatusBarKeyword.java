package br.com.objectos.css.keyword;

import br.com.objectos.css.type.SystemFontValue;

public final class StatusBarKeyword extends StandardKeyword implements SystemFontValue {
  static final StatusBarKeyword INSTANCE = new StatusBarKeyword();

  private StatusBarKeyword() {
    super(233, "statusBar", "status-bar");
  }
}
