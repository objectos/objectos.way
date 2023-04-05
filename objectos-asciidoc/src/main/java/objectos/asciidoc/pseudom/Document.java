/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.asciidoc.pseudom;

import java.io.IOException;
import objectos.asciidoc.internal.PseudoDocument;

public sealed interface Document permits PseudoDocument {

  interface Processor {

    void process(Document document) throws IOException;

  }

  boolean hasNext() throws IOException;

  Node next() throws IOException;

}