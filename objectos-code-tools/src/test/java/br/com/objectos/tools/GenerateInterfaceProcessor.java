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
package br.com.objectos.tools;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

public class GenerateInterfaceProcessor extends AbstractProcessor {

  @Override
  public final Set<String> getSupportedAnnotationTypes() {
    return Collections.singleton(GenerateInterface.class.getCanonicalName());
  }

  @Override
  public final SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public final synchronized void init(ProcessingEnvironment processingEnv) {
    if (!isInitialized()) {
      super.init(processingEnv);
    }
  }

  @Override
  public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Set<TypeElement> types;
    types = ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(GenerateInterface.class));

    for (TypeElement element : types) {
      try {
        tryToGenerate(element);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    return false;
  }

  private void tryToGenerate(TypeElement element) throws IOException {
    Elements elements;
    elements = processingEnv.getElementUtils();

    PackageElement packageElement;
    packageElement = elements.getPackageOf(element);

    String packageName;
    packageName = packageElement.getQualifiedName().toString();

    String interfaceName;
    interfaceName = element.getSimpleName() + "Interface";

    String name;
    name = packageName + "." + interfaceName;

    JavaFileObject generated;
    generated = processingEnv.getFiler().createSourceFile(name, element);

    Writer fileWriter;
    fileWriter = generated.openWriter();

    BufferedWriter w;
    w = new BufferedWriter(fileWriter);

    try {
      w.append("package ").append(packageName).append(';');
      w.newLine();
      w.append("public interface ").append(interfaceName).append(" {}");
    } finally {
      w.close();
    }
  }

}