/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.processing.type;

import static br.com.objectos.tools.Tools.compilationUnit;
import static br.com.objectos.tools.Tools.javac;
import static br.com.objectos.tools.Tools.patchModuleWithTestClasses;
import static br.com.objectos.tools.Tools.processor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.model.element.ProcessingMethod;
import br.com.objectos.code.processing.AbstractProcessingRoundProcessor;
import br.com.objectos.code.processing.ProcessingRound;
import br.com.objectos.code.util.AbstractCodeCoreTest;
import br.com.objectos.tools.Compilation;
import java.util.Set;
import objectos.util.UnmodifiableSet;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PErrorTypeTest extends AbstractCodeCoreTest {

  @Test
  public void test() {
    Compilation compilation;
    compilation = javac(
        processor(new ErrorTypeProcessor()),

        patchModuleWithTestClasses("br.com.objectos.code"),

        compilationUnit(
            "abstract class Subject {",
            "  @br.com.objectos.code.processing.type.ErrorTypeAnnotation",
            "  abstract testing.code.ToBeGenerated method();",
            "}"
        )
    );

    assertFalse(compilation.wasSuccessful());
  }

  private void processReturnType(PTypeMirror returnType) {
    assertTrue(returnType.isErrorType());

    PErrorType error;
    error = returnType.toErrorType();

    assertNotNull(error);

    assertEquals(
        error.toString(),
        "testing.code.ToBeGenerated"
    );

    try {
      error.throwErrorTypeExceptionIfPossible();

      Assert.fail();
    } catch (ErrorTypeException expected) {

    }
  }

  class ErrorTypeProcessor extends AbstractProcessingRoundProcessor {
    @Override
    public final Set<String> getSupportedAnnotationTypes() {
      return supportedAnnotationTypes(ErrorTypeAnnotation.class);
    }

    @Override
    protected final boolean process(ProcessingRound round) {
      UnmodifiableSet<ProcessingMethod> methods;
      methods = round.getAnnotatedMethods();

      if (methods.isEmpty() && !round.isOver()) {
        Assert.fail("methods is empty");
      }

      for (ProcessingMethod method : methods) {
        processReturnType(method.getReturnType());
      }

      return round.claimTheseAnnotations();
    }
  }

}
