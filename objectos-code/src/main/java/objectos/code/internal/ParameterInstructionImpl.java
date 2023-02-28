/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.code.internal;

import objectos.code.tmpl.ParameterInstruction;

public final class ParameterInstructionImpl extends External implements ParameterInstruction {

  public static final ParameterInstruction ELLIPSIS = new ParameterInstructionImpl(
    ByteProto.ELLIPSIS
  );

  private final int proto;

  private ParameterInstructionImpl(int proto) {
    this.proto = proto;
  }

  @Override
  public final void execute(InternalApi api) {
    api.extStart();
    api.protoAdd(proto, ByteProto.NOOP);
  }

}