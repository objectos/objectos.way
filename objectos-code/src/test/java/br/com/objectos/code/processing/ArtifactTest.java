/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.processing;

import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.declaration.ClassCode;
import br.com.objectos.code.java.io.JavaFile;
import br.com.objectos.code.util.AbstractCodeCoreTest;
import org.testng.annotations.Test;

public class ArtifactTest extends AbstractCodeCoreTest {

  @Test
  public void builder_withSingleElement() {
    Artifact res = Artifact.builder()
        .withJavaFile(jf())
        .build();
    assertTrue(res.isJavaFile());
  }

  private JavaFile jf() {
    return ClassCode.builder().build().toJavaFile(TESTING_ON);
  }

}