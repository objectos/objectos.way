package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInsideValue;

public final class RubyKeyword extends StandardKeyword implements DisplayInsideValue {
  static final RubyKeyword INSTANCE = new RubyKeyword();

  private RubyKeyword() {
    super(199, "ruby", "ruby");
  }
}
