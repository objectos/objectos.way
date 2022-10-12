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

import br.com.objectos.code.java.io.JavaFile;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic;
import objectos.lang.Check;
import objectos.util.GrowableList;

public abstract class Artifact {

  private static final Artifact EMPTY = new Artifact() {
    @Override
    protected void execute(ProcessingEnvironment processingEnv) {
      // noop
    }
  };

  protected Artifact() {}

  public static Builder builder() {
    return new Builder();
  }

  public static Artifact empty() {
    return EMPTY;
  }

  public static Artifact of(JavaFile file) {
    Check.notNull(file, "file == null");
    return new JavaFileArtifact(file);
  }

  public boolean isJavaFile() {
    return false;
  }

  protected abstract void execute(ProcessingEnvironment processingEnv);

  protected final void log(ProcessingEnvironment processingEnv, IOException e) {
    var output = new StringWriter();

    var printWriter = new PrintWriter(output);

    e.printStackTrace(printWriter);

    var msg = output.toString();

    processingEnv.getMessager()
        .printMessage(Diagnostic.Kind.ERROR,
          "Processor threw an exception: " + msg);
  }

  public static class Builder {

    private final GrowableList<Artifact> list = new GrowableList<>();

    private Builder() {}

    public Builder addArtifact(Artifact artifact) {
      list.add(artifact);
      return this;
    }

    public Builder addArtifacts(Iterable<Artifact> artifacts) {
      Check.notNull(artifacts, "artifacts == null");

      for (Artifact artifact : artifacts) {
        list.add(artifact);
      }

      return this;
    }

    public final Artifact build() {
      switch (list.size()) {
        case 0:
          return EMPTY;
        case 1:
          return list.get(0);
        default:
          return new ListArtifact(list.toUnmodifiableList());
      }
    }

    public final Builder withJavaFile(JavaFile file) {
      list.add(of(file));
      return this;
    }

  }

}