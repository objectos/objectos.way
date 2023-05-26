package br.com.objectos.css.keyword;

import br.com.objectos.css.type.TextDecorationStyleValue;

public final class WavyKeyword extends StandardKeyword implements TextDecorationStyleValue {
  static final WavyKeyword INSTANCE = new WavyKeyword();

  private WavyKeyword() {
    super(275, "wavy", "wavy");
  }
}
