package br.com.objectos.css.keyword;

import br.com.objectos.css.type.SystemFontValue;

public final class MessageBoxKeyword extends StandardKeyword implements SystemFontValue {
  static final MessageBoxKeyword INSTANCE = new MessageBoxKeyword();

  private MessageBoxKeyword() {
    super(151, "messageBox", "message-box");
  }
}
