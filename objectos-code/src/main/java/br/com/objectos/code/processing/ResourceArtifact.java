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

import java.io.IOException;
import java.io.OutputStream;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import objectos.lang.Check;

public abstract class ResourceArtifact extends Artifact {

  private final String resourceName;

  protected ResourceArtifact(String resourceName) {
    this.resourceName = Check.notNull(resourceName, "resourceName == null");
  }

  public static Builder named(String resourceName) {
    Check.notNull(resourceName, "resourceName == null");

    return new Builder(resourceName);
  }

  @Override
  protected final void execute(ProcessingEnvironment processingEnv) {
    try {
      tryToExecute(processingEnv);
    } catch (IOException e) {
      log(processingEnv, e);
    }
  }

  protected abstract byte[] getBytes();

  private void tryToExecute(ProcessingEnvironment processingEnv) throws IOException {
    Filer filer = processingEnv.getFiler();
    FileObject file = filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceName);
    OutputStream out = file.openOutputStream();
    try {
      out.write(getBytes());
    } finally {
      out.close();
    }
  }

  public static class Builder {
    private final String resourceName;

    Builder(String resourceName) {
      this.resourceName = resourceName;
    }

    public final ResourceArtifact with(String contents) {
      return new StringResourceArtifact(resourceName, contents);
    }
  }

}