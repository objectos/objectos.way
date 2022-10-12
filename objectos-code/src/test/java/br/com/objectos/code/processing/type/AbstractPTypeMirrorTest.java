/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.processing.type;

import br.com.objectos.code.model.element.ProcessingMethod;
import br.com.objectos.code.model.element.ProcessingType;
import br.com.objectos.code.util.AbstractCodeCoreTest;
import java.util.NoSuchElementException;
import objectos.util.UnmodifiableList;

public abstract class AbstractPTypeMirrorTest extends AbstractCodeCoreTest {

  final PTypeMirror getReturnType(Class<?> type, String methodName) {
    ProcessingType processingType;
    processingType = query(type);

    UnmodifiableList<ProcessingMethod> methods;
    methods = processingType.getDeclaredOrInheritedMethods();

    for (int i = 0; i < methods.size(); i++) {
      ProcessingMethod method;
      method = methods.get(i);

      if (method.hasName(methodName)) {
        return method.getReturnType();
      }
    }

    throw new NoSuchElementException(methodName);
  }

}
