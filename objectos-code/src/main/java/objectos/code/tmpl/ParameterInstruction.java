/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.code.tmpl;

import objectos.code.JavaTemplate;
import objectos.code.internal.ParameterInstructionImpl;

/**
 * TODO
 *
 * @since 0.4.4
 */
public sealed interface ParameterInstruction
    extends
    Instruction permits DeclarationName, JavaTemplate._Item, ParameterInstructionImpl, TypeName {}