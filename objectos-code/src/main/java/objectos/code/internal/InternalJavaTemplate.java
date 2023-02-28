/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.code.internal;

import objectos.code.JavaTemplate;
import objectos.lang.Check;

/**
 * @since 0.4.4
 */
public abstract sealed class InternalJavaTemplate permits JavaTemplate {

  private InternalApi api;

  protected InternalJavaTemplate() {}

  protected final InternalApi api() {
    Check.state(api != null, """
    An InternalApi instance was not set.

    Are you trying to execute the method directly?
    Please not that this method should only be invoked inside a definition() method.
    """);

    return api;
  }

  /**
   * TODO
   */
  protected abstract void definition();

  protected final void execute(InternalApi api) {
    Check.state(this.api == null, """
    Another evaluation is already in progress.
    """);

    this.api = api;

    try {
      definition();
    } finally {
      this.api = null;
    }
  }

}