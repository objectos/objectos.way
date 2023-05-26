package br.com.objectos.css.keyword;

import br.com.objectos.css.type.DisplayInternalValue;

public final class RubyBaseKeyword extends StandardKeyword implements DisplayInternalValue {
  static final RubyBaseKeyword INSTANCE = new RubyBaseKeyword();

  private RubyBaseKeyword() {
    super(200, "rubyBase", "ruby-base");
  }
}
