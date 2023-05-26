package objectos.css.keyword;

import objectos.css.type.SystemFontValue;

public final class MessageBoxKeyword extends StandardKeyword implements SystemFontValue {
  static final MessageBoxKeyword INSTANCE = new MessageBoxKeyword();

  private MessageBoxKeyword() {
    super(151, "messageBox", "message-box");
  }
}
