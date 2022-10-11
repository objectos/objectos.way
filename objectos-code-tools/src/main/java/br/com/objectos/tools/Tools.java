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
package br.com.objectos.tools;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import javax.annotation.processing.Processor;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public final class Tools {

  private static final String LINE_SEPARATOR = System.lineSeparator();

  private Tools() {}

  public static JavacOption addModule(final String moduleName) {
    Check.notNull(moduleName, "moduleName == null");

    return new AbstractJavacOption() {
      @Override
      public final void acceptJavacOptionVisitor(JavacOptionVisitor visitor) {
        visitor.visitAddModule(moduleName);
      }
    };
  }

  public static JavacElement compilationUnit(String... lines) {
    UnmodifiableList<String> list;
    list = UnmodifiableList.copyOf(lines);

    String source;
    source = list.join(LINE_SEPARATOR);

    final StringJavaFileObject object;
    object = new StringJavaFileObject(source);

    return new JavacElement() {
      @Override
      public final void acceptJavacBuilder(Builder builder) {
        builder.addJavaFileObject(object);
      }
    };
  }

  public static Compilation javac(JavacElement... elements) {
    Check.notNull(elements, "elements == null");

    Builder b;
    b = new Builder();

    for (int i = 0; i < elements.length; i++) {
      JavacElement element;
      element = elements[i];

      if (element == null) {
        throw new NullPointerException("element[" + i + "] == null");
      }

      element.acceptJavacBuilder(b);
    }

    return b.build();
  }

  public static JavacOption patchModuleWithTestClasses(final String moduleName) {
    Check.notNull(moduleName, "moduleName == null");

    return new AbstractJavacOption() {
      @Override
      public final void acceptJavacOptionVisitor(JavacOptionVisitor visitor) {
        visitor.visitPatchModuleWithTestClasses(moduleName);
      }
    };
  }

  public static JavacElement processor(final Processor processor) {
    return new JavacElement() {
      @Override
      public final void acceptJavacBuilder(Builder builder) {
        builder.addProcessor(processor);
      }
    };
  }

  public static class Builder extends Compilation.Builder {

    private final JavaCompiler compiler;

    private final CompilationJavaFileManager fileManager;

    private final GrowableList<JavaFileObject> javaFiles = new GrowableList<>();

    private final GrowableList<JavacOption> options = new GrowableList<>();

    private final GrowableList<Processor> processors = new GrowableList<>();

    private final StringWriter stdout;

    private Builder() {
      this.compiler = ToolProvider.getSystemJavaCompiler();
      this.fileManager = new CompilationJavaFileManager(compiler);
      this.stdout = new StringWriter();
    }

    public final Builder addJavaFileObject(JavaFileObject object) {
      javaFiles.addWithNullMessage(object, "object == null");
      return this;
    }

    public final Builder addOption(JavacOption option) {
      options.addWithNullMessage(option, "option == null");
      return this;
    }

    public final Builder addProcessor(Processor processor) {
      processors.addWithNullMessage(processor, "processor == null");
      return this;
    }

    @Override
    final Map<String, ? extends GeneratedClassFile> generatedClassFiles() {
      return fileManager.generatedClassFiles;
    }

    @Override
    final Map<String, ? extends GeneratedJavaFile> generatedJavaFiles() {
      return fileManager.generatedJavaFiles;
    }

    @Override
    final Map<String, ? extends GeneratedResource> generatedResources() {
      return fileManager.generatedResources;
    }

    @Override
    final String message() {
      return stdout.toString();
    }

    @Override
    final boolean successful() {
      OptionsBuilder optionsBuilder;
      optionsBuilder = new OptionsBuilder();

      for (int i = 0; i < options.size(); i++) {
        JavacOption option;
        option = options.get(i);

        option.acceptJavacOptionVisitor(optionsBuilder);
      }

      UnmodifiableList<String> taskOptions;
      taskOptions = optionsBuilder.build();

      CompilationTask task;
      task = compiler.getTask(stdout, fileManager, null, taskOptions, null, javaFiles);

      task.setProcessors(processors);

      try {
        return task.call();
      } finally {
        try {
          fileManager.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

  }

}
