/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.carbonated;

import objectos.carbonated.internal.StyleSheetBuilderImpl;
import objectos.css.StyleSheet;

/**
 * @since 0.7.1
 */
public final class Carbon {

  public sealed interface StyleSheetBuilder permits StyleSheetBuilderImpl {

    StyleSheet build();

    StyleSheetBuilder reset();

    StyleSheetBuilder typography();

  }

  private Carbon() {}

  public static StyleSheetBuilder styleSheetBuilder() {
    return new StyleSheetBuilderImpl();
  }

}