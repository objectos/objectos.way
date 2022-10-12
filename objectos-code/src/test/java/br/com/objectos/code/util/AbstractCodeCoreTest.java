/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.util;

import static br.com.objectos.tools.Tools.compilationUnit;
import static br.com.objectos.tools.Tools.javac;
import static br.com.objectos.tools.Tools.patchModuleWithTestClasses;
import static br.com.objectos.tools.Tools.processor;
import static org.testng.Assert.assertEquals;

import br.com.objectos.code.java.declaration.PackageName;
import br.com.objectos.code.model.element.ProcessingMethod;
import br.com.objectos.code.model.element.ProcessingPackage;
import br.com.objectos.code.model.element.ProcessingType;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

public abstract class AbstractCodeCoreTest implements IHookable {

  public interface Function<F, T> {

    T apply(F from);

  }

  public interface Predicate<T> {

    boolean test(T o);

  }

  @TypeAnnotation
  protected static abstract class Basic {
    abstract String name();
    abstract int value();
  }

  protected static abstract class Generic<//
      U /* U meaning Unbounded */, //
      B extends InputStream /* B meaning Bounded */, //
      I extends InputStream & Closeable /* I as Intersection */> {}

  @SuppressWarnings("serial")
  protected abstract class InstanceOf extends Basic implements Serializable {}

  protected static class Outer {
    public static class Inner {}
  }

  protected static final PackageName TESTING_ON = PackageName.named("testing.on");

  protected static final Path TMPDIR = Path.of(System.getProperty("java.io.tmpdir"));

  protected ProcessingEnvironment processingEnv;

  @SuppressWarnings("exports")
  @Override
  public final void run(final IHookCallBack callBack, final ITestResult testResult) {
    class TestNgProcessor extends AbstractProcessor {
      @Override
      public final Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton("*");
      }

      @Override
      public final SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
      }

      @Override
      public final boolean process(
          Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
          AbstractCodeCoreTest.this.processingEnv = processingEnv;
          callBack.runTestMethod(testResult);
        }

        return false;
      }
    }
    javac(
      processor(new TestNgProcessor()),
      patchModuleWithTestClasses("br.com.objectos.code"),
      compilationUnit("class Dummy {}")
    );
  }

  protected final void deleteRecursively(Path directory) throws IOException {
    if (directory == null) {
      return;
    }

    Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
      @Override
      public final FileVisitResult postVisitDirectory(
          Path dir, IOException e) throws IOException {
        if (e == null) {
          Files.delete(dir);

          return FileVisitResult.CONTINUE;
        } else {
          throw e;
        }
      }

      @Override
      public final FileVisitResult visitFile(
          Path file, BasicFileAttributes attrs) throws IOException {
        Files.delete(file);

        return FileVisitResult.CONTINUE;
      }
    });
  }

  protected final ProcessingMethod getDeclaredMethod(Class<?> type, String methodName) {
    ProcessingType processingType;
    processingType = query(type);

    return getDeclaredMethod(processingType, methodName);
  }

  protected final ProcessingMethod getDeclaredMethod(ProcessingType type, String methodName) {
    UnmodifiableList<ProcessingMethod> methods;
    methods = type.getDeclaredMethods();

    for (int i = 0; i < methods.size(); i++) {
      ProcessingMethod method;
      method = methods.get(i);

      if (method.getName().equals(methodName)) {
        return method;
      }
    }

    throw new NoSuchElementException(methodName);
  }

  protected final ExecutableElement getMethodElement(Class<?> type, String name) {
    List<? extends Element> enclosed = getTypeElement(type).getEnclosedElements();
    List<ExecutableElement> methods = ElementFilter.methodsIn(enclosed);
    for (ExecutableElement method : methods) {
      if (method.getSimpleName().toString().equals(name)) {
        return method;
      }
    }
    throw new NoSuchElementException(name);
  }

  protected final ProcessingPackage getProcessingPackage(Class<?> marker) {
    Package markerPackage;
    markerPackage = marker.getPackage();

    String packageName;
    packageName = markerPackage.getName();

    Elements elements;
    elements = processingEnv.getElementUtils();

    PackageElement element;
    element = elements.getPackageElement(packageName);

    return ProcessingPackage.adapt(processingEnv, element);
  }

  protected final TypeElement getTypeElement(Class<?> type) {
    Elements elements;
    elements = processingEnv.getElementUtils();

    String name;
    name = type.getCanonicalName();

    TypeElement typeElement;
    typeElement = elements.getTypeElement(name);

    return Check.notNull(typeElement, "Return null TypeElement for " + name);
  }

  protected final Types getTypeUtils() {
    return processingEnv.getTypeUtils();
  }

  protected final ProcessingType query(Class<?> type) {
    TypeElement typeElement;
    typeElement = getTypeElement(type);

    return ProcessingType.adapt(processingEnv, typeElement);
  }

  protected final void testToString(Object o, String... lines) {
    UnmodifiableList<String> list = UnmodifiableList.copyOf(lines);

    String expected = list.join("\n");

    assertEquals(o.toString(), expected);
  }

  protected final Function<Object, String> toStringFunction() {
    return new Function<Object, String>() {
      @Override
      public String apply(Object from) {
        return from.toString();
      }
    };
  }

}