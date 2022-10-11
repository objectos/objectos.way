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
package br.com.objectos.code.fakes;

import br.com.objectos.code.apt.AnnotationArray;
import br.com.objectos.code.Testing;
import br.com.objectos.code.apt.Level;
import br.com.objectos.code.apt.MethodAnnotation;
import br.com.objectos.code.apt.ParameterAnnotation;
import br.com.objectos.code.apt.TypeAnnotation;
import javax.validation.constraints.NotNull;

@Testing
@TypeAnnotation(typeInfo = Long.class, value = 456)
@AnnotationArray({
    @ParameterAnnotation(Level.LEVEL_1),
    @ParameterAnnotation(Level.LEVEL_3)
})
public abstract class Annotated {

  Annotated() {
  }

  @MethodAnnotation
  public abstract void annotatedMethod();

  public abstract void annotatedParameter(int id, @ParameterAnnotation(Level.LEVEL_2) String name);
  
  public abstract void notNull(@NotNull Object notNull);

  @TypeAnnotation
  private static class AnnotatedInner {

    AnnotatedInner() {
    }

  }

}