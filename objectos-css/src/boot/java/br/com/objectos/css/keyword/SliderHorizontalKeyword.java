package br.com.objectos.css.keyword;

import br.com.objectos.css.type.AppearanceValue;

public final class SliderHorizontalKeyword extends StandardKeyword implements AppearanceValue {
  static final SliderHorizontalKeyword INSTANCE = new SliderHorizontalKeyword();

  private SliderHorizontalKeyword() {
    super(218, "sliderHorizontal", "slider-horizontal");
  }
}
