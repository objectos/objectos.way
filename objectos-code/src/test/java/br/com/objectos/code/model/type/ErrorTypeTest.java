/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.model.type;

import static br.com.objectos.tools.Tools.compilationUnit;
import static br.com.objectos.tools.Tools.javac;
import static br.com.objectos.tools.Tools.patchModuleWithTestClasses;
import static br.com.objectos.tools.Tools.processor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import br.com.objectos.code.processing.type.ErrorTypeAnnotation;
import br.com.objectos.code.util.AbstractCodeCoreTest;
import br.com.objectos.tools.Compilation;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.testng.annotations.Test;

public class ErrorTypeTest extends AbstractCodeCoreTest {

  @Test
  public void test() {
    Compilation compilation = javac(
        processor(new ErrorTypeProcessor()),
        patchModuleWithTestClasses("br.com.objectos.code.core"),
        compilationUnit(
            "abstract class Subject {",
            "  @br.com.objectos.code.model.type.ErrorTypeAnnotation",
            "  abstract testing.code.ToBeGenerated method();",
            "}"
        )
    );

    assertFalse(compilation.wasSuccessful());
  }

  private void processReturnType(TypeMirror returnType) {
    TypeKind kind = returnType.getKind();

    assertEquals(kind, TypeKind.ERROR);

    ErrorType error = (ErrorType) returnType;

    Element element = error.asElement();
    assertEquals(element.getKind(), ElementKind.CLASS);

    TypeElement typeElement = (TypeElement) element;
    assertEquals(typeElement.getQualifiedName().toString(), "testing.code.ToBeGenerated");

    List<? extends TypeMirror> typeArguments = error.getTypeArguments();
    assertEquals(typeArguments.size(), 0);

    List<? extends AnnotationMirror> annotations = error.getAnnotationMirrors();
    assertEquals(annotations.size(), 0);

    String toString = error.toString();
    assertEquals(toString, "testing.code.ToBeGenerated");
  }

  class ErrorTypeProcessor extends AbstractProcessor {
    @Override
    public final Set<String> getSupportedAnnotationTypes() {
      return Collections.singleton(ErrorTypeAnnotation.class.getCanonicalName());
    }

    @Override
    public final SourceVersion getSupportedSourceVersion() {
      return SourceVersion.latestSupported();
    }

    @Override
    public final boolean process(
        Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
      for (TypeElement annotation : annotations) {
        process0(roundEnv, annotation);
      }
      return true;
    }

    private void process0(RoundEnvironment roundEnv, TypeElement annotation) {
      Set<? extends Element> elements;
      elements = roundEnv.getElementsAnnotatedWith(annotation);

      Set<ExecutableElement> methods;
      methods = ElementFilter.methodsIn(elements);

      for (ExecutableElement method : methods) {
        process1(method);
      }
    }

    private void process1(ExecutableElement method) {
      TypeMirror returnType;
      returnType = method.getReturnType();

      processReturnType(returnType);
    }
  }

}
