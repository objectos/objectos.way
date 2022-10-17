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
package br.com.objectos.code.java.io;

import static br.com.objectos.code.java.declaration.ClassCode._class;
import static br.com.objectos.code.java.declaration.EnumCode._enum;
import static br.com.objectos.code.java.declaration.ExtendsOne._extends;
import static br.com.objectos.code.java.declaration.InterfaceCode._interface;
import static br.com.objectos.code.java.declaration.Modifiers._public;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.io.JavaFile.javaFile;
import static br.com.objectos.code.java.type.NamedTypeParameter.typeParam;
import static br.com.objectos.code.java.type.NamedTypes.t;
import static br.com.objectos.tools.Tools.compilationUnit;
import static br.com.objectos.tools.Tools.javac;
import static br.com.objectos.tools.Tools.processor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.declaration.ClassCode;
import br.com.objectos.code.java.declaration.PackageName;
import br.com.objectos.code.java.declaration.PackageNameFake;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import br.com.objectos.tools.Compilation;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import org.testng.annotations.Test;

public class JavaFileTest extends AbstractCodeJavaTest {

  private class WriteToProcessor extends AbstractProcessor {
    @Override
    public final Set<String> getSupportedAnnotationTypes() {
      return Collections.singleton("*");
    }

    @Override
    public final boolean process(
        Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
      if (roundEnv.processingOver()) {
        ClassCode code = _class(id("Subject"));
        JavaFile file = code.toJavaFile(PackageNameFake.TESTING_CODE);
        write(file);
      }
      return false;
    }

    private void write(JavaFile file) {
      try {
        file.writeTo(processingEnv.getFiler());
      } catch (IOException e) {
        processingEnv.getMessager().printMessage(Kind.ERROR, e.getMessage());
      }
    }
  }

  @Test
  public void className() {
    assertEquals(
      javaFile(TESTING_CODE, _class(id("A"))).className(),
      TESTING_CODE.nestedClass("A")
    );
    assertEquals(
      javaFile(TESTING_OTHER, _enum(id("B"))).className(),
      TESTING_OTHER.nestedClass("B")
    );
    assertEquals(
      javaFile(TESTING_OTHER, _interface(id("C"))).className(),
      TESTING_OTHER.nestedClass("C")
    );
  }

  @Test(
      description = ""
          + "verify that the type parameters of class "
          + "correctly generate import declarations"
  )
  public void classWithTypeParametersThatGenerateImport() {
    testToString(
      _class(
        id("Subject"),
        typeParam("I", t(InputStream.class), t(Closeable.class))
      ).toJavaFile(TESTING_CODE),
      "package testing.code;",
      "",
      "import java.io.Closeable;",
      "import java.io.InputStream;",
      "",
      "class Subject<I extends InputStream & Closeable> {}"
    );
  }

  @Test(description = ""
      + "javaFile shorthand should honor _class() shorthand")
  public void javaFileWithClass() {
    testToString(
      javaFile(
        TESTING_CODE,
        _class(id("Subject"))
      ),
      "package testing.code;",
      "",
      "class Subject {}"
    );
    testToString(
      javaFile(
        TESTING_CODE,
        _class(id("Subject"), _extends(TESTING_CODE.nestedClass("Super")))
      ),
      "package testing.code;",
      "",
      "class Subject extends Super {}"
    );
  }

  @Test
  public void resolvePath() {
    ClassCode clazzCode = _class(id("WriteToDirectory"));

    PackageName a = PackageName.named("code.a");
    PackageName b = PackageName.named("code.a.b");
    PackageName c = PackageName.named("code.a.b.c");

    var ra = javaFile(a, clazzCode).resolvePath(TMPDIR);
    var rb = javaFile(b, clazzCode).resolvePath(TMPDIR);
    var rc = javaFile(c, clazzCode).resolvePath(TMPDIR);

    assertEquals(ra, TMPDIR.resolve(Path.of("code", "a", "WriteToDirectory.java")));
    assertEquals(rb, TMPDIR.resolve(Path.of("code", "a", "b", "WriteToDirectory.java")));
    assertEquals(rc, TMPDIR.resolve(Path.of("code", "a", "b", "c", "WriteToDirectory.java")));
  }

  @Test(description = ""
      + "it should return the simpleName of the file, ie, "
      + "the filename without the .java extension")
  public void simpleName() {
    assertEquals(javaFile(TESTING_CODE, _class(id("A"))).simpleName(), "A");
    assertEquals(javaFile(TESTING_OTHER, _enum(id("B"))).simpleName(), "B");
    assertEquals(javaFile(TESTING_OTHER, _interface(id("C"))).simpleName(), "C");
  }

  @Test(description = ""
      + "JavaFile built with an unnamed package should not render the package statement.")
  public void unnamedPackage() {
    testToString(
      JavaFile.builder().addType(ClassCode.builder().build()).build(),
      "class Unnamed {}"
    );
  }

  @Test(description = ""
      + "it should create the directory (package) structure if needed."
      + "it should write itself to a new file (or overwrite it if it exists).")
  public void writeToDirectory() throws IOException {
    ClassCode clazzCode = _class(id("WriteToDirectory"));

    PackageName a = PackageName.named("code.a");
    PackageName b = PackageName.named("code.a.b");
    PackageName c = PackageName.named("code.a.b.c");

    var tmp = Files.createTempDirectory(TMPDIR, "java-file-test-");

    try {
      var ra = javaFile(a, clazzCode).writeTo(tmp);
      var rb = javaFile(b, clazzCode).writeTo(tmp);
      var rc = javaFile(c, clazzCode).writeTo(tmp);

      testToString(
        Files.readString(ra),
        "package code.a;",
        "",
        "class WriteToDirectory {}"
      );

      testToString(
        Files.readString(rb),
        "package code.a.b;",
        "",
        "class WriteToDirectory {}"
      );

      testToString(
        Files.readString(rc),
        "package code.a.b.c;",
        "",
        "class WriteToDirectory {}"
      );

      var rd = javaFile(c, _class(_public(), id("WriteToDirectory"))).writeTo(tmp);

      testToString(
        Files.readString(rd),
        "package code.a.b.c;",
        "",
        "public class WriteToDirectory {}"
      );
    } finally {
      deleteRecursively(tmp);
    }
  }

  @Test(description = ""
      + "it should write its contents to a javax.ann.proc.Filer")
  public void writeToFiler() {
    Compilation compilation = javac(
      processor(new WriteToProcessor()),
      compilationUnit(
        "package testing.code;",
        "class Dummy {}"
      )
    );
    assertTrue(compilation.wasSuccessful());
    testToString(
      compilation.getJavaFile("testing.code.Subject"),
      "package testing.code;",
      "",
      "class Subject {}"
    );
  }

}