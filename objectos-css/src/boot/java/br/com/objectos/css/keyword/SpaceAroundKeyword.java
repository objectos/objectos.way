package br.com.objectos.css.keyword;

import br.com.objectos.css.type.ContentDistribution;

public final class SpaceAroundKeyword extends StandardKeyword implements ContentDistribution {
  static final SpaceAroundKeyword INSTANCE = new SpaceAroundKeyword();

  private SpaceAroundKeyword() {
    super(225, "spaceAround", "space-around");
  }
}
