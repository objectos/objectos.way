/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.code.tmpl;

/**
 * An {@link Instruction} that can be used with constructs that can declare
 * formal parameters.
 *
 * @see JavaTemplate#constructor(ParameterElement...)
 * @see JavaTemplate#method(String, ParameterElement...)
 */
@Deprecated
public interface ParameterElement extends Instruction {}