/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.asciidoc.internal;

import java.io.IOException;
import objectos.asciidoc.pseudom.Document;
import objectos.asciidoc.pseudom.Node;

public final class PseudoDocument implements Document {

  private final InternalSink outer;

  PseudoDocument(InternalSink outer) {
    this.outer = outer;
  }

  @Override
  public final boolean hasNext() throws IOException {
    return outer.documentHasNext();
  }

  @Override
  public final Node next() {
    throw new UnsupportedOperationException("Implement me");
  }

}