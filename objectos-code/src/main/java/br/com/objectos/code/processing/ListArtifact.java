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

import javax.annotation.processing.ProcessingEnvironment;
import objectos.util.UnmodifiableList;

class ListArtifact extends Artifact {

  private final UnmodifiableList<Artifact> list;

  public ListArtifact(UnmodifiableList<Artifact> list) {
    this.list = list;
  }

  @Override
  public String toString() {
    return list.join(System.lineSeparator());
  }

  @Override
  protected void execute(ProcessingEnvironment processingEnv) {
    for (Artifact artifact : list) {
      artifact.execute(processingEnv);
    }
  }

}