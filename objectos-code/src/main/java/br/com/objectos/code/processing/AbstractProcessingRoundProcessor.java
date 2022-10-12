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
package br.com.objectos.code.processing;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Name;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import objectos.lang.Check;
import objectos.util.UnmodifiableSet;
import objectos.util.GrowableSet;

public abstract class AbstractProcessingRoundProcessor extends AbstractProcessor {

  private Reprocessor reprocessor;

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latest();
  }

  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    reprocessor = new ThisReprocessor();
  }

  @Override
  public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    ProcessingRound round;
    round = ProcessingRound.adapt(processingEnv, annotations, roundEnv, reprocessor);

    return process(round);
  }

  protected abstract boolean process(ProcessingRound round);

  protected final Set<String> supportedAnnotationTypes(Class<? extends Annotation> annotation) {
    Check.notNull(annotation, "annotation == null");
    return Collections.singleton(annotation.getCanonicalName());
  }

  private static class ThisReprocessor implements Reprocessor {

    private final GrowableSet<Name> packages = new GrowableSet<>();

    private final GrowableSet<Name> types = new GrowableSet<>();

    @Override
    public final UnmodifiableSet<Name> getPackages() {
      UnmodifiableSet<Name> result;
      result = packages.toUnmodifiableSet();

      packages.clear();

      return result;
    }

    @Override
    public final UnmodifiableSet<Name> getTypes() {
      UnmodifiableSet<Name> result;
      result = types.toUnmodifiableSet();

      types.clear();

      return result;
    }

    @Override
    public final void reprocessPackage(PackageElement packageElement) {
      Check.notNull(packageElement, "packageElement == null");

      packages.add(packageElement.getQualifiedName());
    }

    @Override
    public final void reprocessType(TypeElement type) {
      Check.notNull(type, "type == null");

      types.add(type.getQualifiedName());
    }

  }

}
