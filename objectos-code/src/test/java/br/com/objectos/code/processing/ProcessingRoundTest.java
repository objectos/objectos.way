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

import static br.com.objectos.code.java.declaration.InterfaceCode._interface;
import static br.com.objectos.tools.Tools.compilationUnit;
import static br.com.objectos.tools.Tools.javac;
import static br.com.objectos.tools.Tools.patchModuleWithTestClasses;
import static br.com.objectos.tools.Tools.processor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.declaration.PackageName;
import br.com.objectos.code.java.io.JavaFile;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.model.AnnotatedElementOrType;
import br.com.objectos.code.model.element.ProcessingAnnotation;
import br.com.objectos.code.model.element.ProcessingAnnotationValue;
import br.com.objectos.code.model.element.ProcessingPackage;
import br.com.objectos.code.model.element.ProcessingType;
import br.com.objectos.code.processing.type.ErrorTypeException;
import br.com.objectos.code.processing.type.PTypeMirror;
import br.com.objectos.tools.Compilation;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.SupportedAnnotationTypes;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableSet;
import objectos.util.GrowableList;
import org.testng.annotations.Test;

public class ProcessingRoundTest {

  @Test
  public void getAnnotatedPackages() {
    final Set<String> names = new HashSet<>();

    @SupportedAnnotationTypes("br.com.objectos.code.util.Marker1")
    class ThisProcessor extends AbstractProcessingRoundProcessor {
      @Override
      protected final boolean process(ProcessingRound round) {
        UnmodifiableSet<ProcessingPackage> packages = round.getAnnotatedPackages();
        for (ProcessingPackage pkg : packages) {
          names.add(pkg.toString());
        }
        return round.allowOtherProcessors();
      }
    }

    Compilation result = javac(
      processor(new ThisProcessor()),
      patchModuleWithTestClasses("br.com.objectos.code"),
      compilationUnit(
        "@br.com.objectos.code.util.Marker1",
        "package testing.yes;"
      ),
      compilationUnit(
        "@br.com.objectos.code.util.Marker2",
        "package testing.no;"
      )
    );

    assertTrue(result.wasSuccessful());
    assertEquals(names.size(), 1);
    assertTrue(names.contains("testing.yes"));
  }

  @Test
  public void getAnnotatedTypes() {
    final Set<String> names = new HashSet<>();

    @SupportedAnnotationTypes("br.com.objectos.code.util.Marker1")
    class ThisProcessor extends AbstractProcessingRoundProcessor {
      @Override
      protected final boolean process(ProcessingRound round) {
        UnmodifiableSet<ProcessingType> types = round.getAnnotatedTypes();
        for (ProcessingType type : types) {
          names.add(type.getName().toString());
        }
        return round.allowOtherProcessors();
      }
    }

    Compilation result = javac(
      processor(new ThisProcessor()),
      patchModuleWithTestClasses("br.com.objectos.code"),
      compilationUnit(
        "package testing.yes;",
        "@br.com.objectos.code.util.Marker1",
        "@interface IncludeMeAnnotation {}"
      ),
      compilationUnit(
        "package testing.yes;",
        "@br.com.objectos.code.util.Marker1",
        "class IncludeMeClass {}"
      ),
      compilationUnit(
        "package testing.yes;",
        "@br.com.objectos.code.util.Marker1",
        "enum IncludeMeEnum { INSTANCE; }"
      ),
      compilationUnit(
        "package testing.yes;",
        "@br.com.objectos.code.util.Marker1",
        "interface IncludeMeIface {}"
      ),
      compilationUnit(
        "package testing.yes;",
        "class NotOnType {",
        "  @br.com.objectos.code.util.Marker1",
        "  void method() {}",
        "}"
      ),
      compilationUnit(
        "@br.com.objectos.code.util.Marker1",
        "package testing.no;"
      )
    );
    assertTrue(result.wasSuccessful());
    assertEquals(names.size(), 4);
    assertTrue(names.contains("testing.yes.IncludeMeAnnotation"));
    assertTrue(names.contains("testing.yes.IncludeMeClass"));
    assertTrue(names.contains("testing.yes.IncludeMeEnum"));
    assertTrue(names.contains("testing.yes.IncludeMeIface"));
  }

  @Test
  public void reprocessIfPossible_packages() {
    final GrowableList<RoundClassName> result;
    result = new GrowableList<>();

    class ClassArrayAnnotationProcesssor extends ReprocessorProcessor {
      @Override
      protected final void processRound(ProcessingRound round) {
        UnmodifiableSet<ProcessingPackage> packages = round.getAnnotatedPackages();

        for (ProcessingPackage pkg : packages) {
          try {
            process0(result, pkg);
          } catch (ErrorTypeException e) {
            generate(round, pkg);
          }
        }
      }

      private void generate(ProcessingRound round, ProcessingPackage pkg) {
        try {
          PackageName packageName;
          packageName = pkg.toNamedPackage();

          generateIface(round, packageName.nestedClass("GeneratedIface"));

          round.reprocessPackage(pkg);
        } catch (IOException e) {
          round.printMessageError(e);
        }
      }
    }

    ClassArrayAnnotationProcesssor processor = new ClassArrayAnnotationProcesssor();

    Compilation compilation = javac(
      processor(processor),
      patchModuleWithTestClasses("br.com.objectos.code"),
      compilationUnit(
        "@br.com.objectos.code.processing.ClassArrayAnnotation(",
        "    { testing.code.GeneratedIface.class }",
        ")",
        "package testing.code;"
      )
    );

    assertTrue(compilation.wasSuccessful());

    assertEquals(result.size(), 1);

    RoundClassName r0 = result.get(0);

    assertEquals(r0.roundIndex, 2);
    assertEquals(r0.className, PackageName.named("testing.code").nestedClass("GeneratedIface"));
  }

  @Test
  public void reprocessIfPossible_types() {
    final GrowableList<RoundClassName> result;
    result = new GrowableList<>();

    class ClassArrayAnnotationProcesssor extends ReprocessorProcessor {
      @Override
      final void processRound(ProcessingRound round) {
        UnmodifiableSet<ProcessingType> types;
        types = round.getAnnotatedTypes();

        for (ProcessingType type : types) {
          try {
            process0(result, type);
          } catch (ErrorTypeException e) {
            generate(round, type);
          }
        }
      }

      private void generate(ProcessingRound round, ProcessingType type) {
        try {
          NamedClass className;
          className = type.getName().withSuffix("Generated");

          generateIface(round, className);

          round.reprocessType(type);
        } catch (IOException e) {
          round.printMessageError(e);
        }
      }
    }

    ClassArrayAnnotationProcesssor processor;
    processor = new ClassArrayAnnotationProcesssor();

    Compilation compilation = javac(
      processor(processor),
      patchModuleWithTestClasses("br.com.objectos.code"),
      compilationUnit(
        "package testing.code;",
        "@br.com.objectos.code.processing.ClassArrayAnnotation(",
        "    { testing.code.DynamicGenerated.class }",
        ")",
        "class Dynamic {}"
      )
    );

    assertTrue(compilation.wasSuccessful());

    assertEquals(result.size(), 1);

    RoundClassName r0;
    r0 = result.get(0);

    assertEquals(r0.roundIndex, 2);
    assertEquals(r0.className, PackageName.named("testing.code").nestedClass("DynamicGenerated"));
  }

  private abstract static class ReprocessorProcessor extends AbstractProcessingRoundProcessor {

    int roundIndex;

    @Override
    public final Set<String> getSupportedAnnotationTypes() {
      return supportedAnnotationTypes(ClassArrayAnnotation.class);
    }

    @Override
    protected final boolean process(ProcessingRound round) {
      roundIndex++;

      processRound(round);

      return round.claimTheseAnnotations();
    }

    final void generateIface(ProcessingRound round, NamedClass className) throws IOException {
      round.writeJavaFile(
        JavaFile.javaFile(
          className.getPackage(),
          _interface(
            className
          )
        )
      );
    }

    final void process0(
        GrowableList<RoundClassName> result, AnnotatedElementOrType element)
        throws ErrorTypeException {
      ProcessingAnnotation annotation;
      annotation = element.getDirectlyPresentAnnotation(ClassArrayAnnotation.class);

      ProcessingAnnotationValue value;
      value = annotation.getDeclaredOrDefaultValue("value");

      UnmodifiableList<PTypeMirror> classValues;
      classValues = value.getTypeArray();

      for (PTypeMirror classValue : classValues) {
        classValue.throwErrorTypeExceptionIfPossible();

        NamedClass forSureIsClassName;
        forSureIsClassName = (NamedClass) classValue.getName();

        result.add(new RoundClassName(roundIndex, forSureIsClassName));
      }
    }

    abstract void processRound(ProcessingRound round);

  }

  private static class RoundClassName {
    final NamedClass className;
    final int roundIndex;

    RoundClassName(int roundIndex, NamedClass className) {
      this.roundIndex = roundIndex;
      this.className = className;
    }
  }

}
