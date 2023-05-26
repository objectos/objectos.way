package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AppearanceValue;

public final class ProgressBarKeyword extends StandardKeyword implements AppearanceValue {
  static final ProgressBarKeyword INSTANCE = new ProgressBarKeyword();

  private ProgressBarKeyword() {
    super(186, "progressBar", "progress-bar");
  }
}
