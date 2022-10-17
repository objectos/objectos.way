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