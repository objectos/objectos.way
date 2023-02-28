/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.code.internal;

import objectos.code.JavaTemplate.Instruction;

public abstract class External implements Instruction {
  protected External() {}

  public abstract void execute(InternalApi api);
}