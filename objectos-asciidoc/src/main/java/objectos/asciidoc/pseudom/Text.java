/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.asciidoc.pseudom;

import objectos.asciidoc.internal.PseudoText;

public sealed interface Text extends Node permits PseudoText {

  String value();

}