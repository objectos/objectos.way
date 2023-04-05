/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.asciidoc.pseudom;

import java.io.IOException;
import objectos.asciidoc.internal.PseudoHeading;

public sealed interface Heading extends Node permits PseudoHeading {

  int level();

  boolean hasNext() throws IOException;

  Node next() throws IOException;

}