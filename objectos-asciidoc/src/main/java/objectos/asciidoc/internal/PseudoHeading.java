/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.asciidoc.internal;

import java.io.IOException;
import objectos.asciidoc.pseudom.Heading;
import objectos.asciidoc.pseudom.Node;

public final class PseudoHeading implements Heading {

  private final InternalSink sink;

  int level;

  PseudoHeading(InternalSink sink) {
    this.sink = sink;
  }

  @Override
  public final int level() {
    return level;
  }

  @Override
  public boolean hasNext() throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public Node next() throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

}
