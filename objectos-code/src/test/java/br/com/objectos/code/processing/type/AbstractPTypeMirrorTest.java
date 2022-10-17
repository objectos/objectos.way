/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
