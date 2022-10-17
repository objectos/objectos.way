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
