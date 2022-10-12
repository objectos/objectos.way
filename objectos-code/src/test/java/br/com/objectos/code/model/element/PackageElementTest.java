/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.model.element;

import static org.testng.Assert.assertEquals;

import br.com.objectos.code.util.AbstractCodeCoreTest;
import javax.lang.model.element.PackageElement;
import javax.lang.model.util.Elements;
import org.testng.annotations.Test;

public class PackageElementTest extends AbstractCodeCoreTest {
  
  @Test
  public void getSimpleName() {
    Elements elements;
    elements = processingEnv.getElementUtils();

    Package thisPackage;
    thisPackage = getClass().getPackage();

    PackageElement thisPackageElement;
    thisPackageElement = elements.getPackageElement(thisPackage.getName());

    assertEquals(
        thisPackageElement.getSimpleName().toString(),
        "element"
    );
  }

}
