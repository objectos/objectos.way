package br.com.objectos.css.keyword;

import br.com.objectos.css.type.BackgroundAttachmentValue;

public final class LocalKeyword extends StandardKeyword implements BackgroundAttachmentValue {
  static final LocalKeyword INSTANCE = new LocalKeyword();

  private LocalKeyword() {
    super(137, "local", "local");
  }
}
