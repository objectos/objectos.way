/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.processing.type;

import br.com.objectos.code.java.type.NamedType;
import br.com.objectos.code.util.AbstractCodeCoreTest.Function;

public class ModelTypeGetName
    implements Function<PTypeMirror, NamedType> {

  private static final Function<PTypeMirror, NamedType> INSTANCE = new ModelTypeGetName();

  public static Function<PTypeMirror, NamedType> get() {
    return INSTANCE;
  }

  @Override
  public final NamedType apply(PTypeMirror input) {
    return input.getName();
  }

}
