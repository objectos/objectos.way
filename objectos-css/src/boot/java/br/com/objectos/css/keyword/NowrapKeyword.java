package br.com.objectos.css.keyword;

import br.com.objectos.css.type.FlexWrapValue;
import br.com.objectos.css.type.WhiteSpaceValue;

public final class NowrapKeyword extends StandardKeyword implements FlexWrapValue, WhiteSpaceValue {
  static final NowrapKeyword INSTANCE = new NowrapKeyword();

  private NowrapKeyword() {
    super(169, "nowrap", "nowrap");
  }
}
