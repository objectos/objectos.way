package objectos.css.keyword;

import objectos.css.type.TextDecorationLineValue;

public final class SpellingErrorKeyword extends StandardKeyword implements TextDecorationLineValue {
  static final SpellingErrorKeyword INSTANCE = new SpellingErrorKeyword();

  private SpellingErrorKeyword() {
    super(228, "spellingError", "spelling-error");
  }
}
