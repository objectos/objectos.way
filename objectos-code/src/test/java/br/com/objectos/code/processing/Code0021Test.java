/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.processing;

import static br.com.objectos.tools.Tools.compilationUnit;
import static br.com.objectos.tools.Tools.javac;
import static br.com.objectos.tools.Tools.patchModuleWithTestClasses;
import static br.com.objectos.tools.Tools.processor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.model.element.ProcessingType;
import br.com.objectos.code.util.Marker1;
import br.com.objectos.tools.Compilation;
import br.com.objectos.tools.GeneratedResource;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import objectos.util.UnmodifiableList;
import objectos.util.UnmodifiableSet;
import objectos.util.GrowableList;
import org.testng.annotations.Test;

public class Code0021Test {

  @Test
  public void shouldIgnoreTypesNotAnnotated() {
    Compilation compilation = javac(
        processor(new Code0021Processor()),
        patchModuleWithTestClasses("br.com.objectos.code"),
        compilationUnit(
            "package code.testing;",
            "@br.com.objectos.code.util.Marker1",
            "class Subject {}"
        ),
        compilationUnit(
            "package code.testing;",
            "class IgnoreMe {}"
        )
    );
    test(
        compilation,
        "code.testing.Subject"
    );
  }

  @Test
  public void shouldProcessAllAnnotated() {
    Compilation compilation = javac(
        processor(new Code0021Processor()),
        patchModuleWithTestClasses("br.com.objectos.code"),
        compilationUnit(
            "package code.testing1;",
            "@br.com.objectos.code.util.Marker1",
            "class Subject {}"
        ),
        compilationUnit(
            "package code.testing2;",
            "@br.com.objectos.code.util.Marker1",
            "class Subject {}"
        )
    );
    test(
        compilation,
        "code.testing1.Subject",
        "code.testing2.Subject"
    );
  }

  @Test
  public void singleAnnotatedTypeElement() {
    Compilation compilation = javac(
        processor(new Code0021Processor()),
        patchModuleWithTestClasses("br.com.objectos.code"),
        compilationUnit(
            "package code.testing;",
            "@br.com.objectos.code.util.Marker1",
            "class Subject {}"
        )
    );
    test(
        compilation,
        "code.testing.Subject"
    );
  }

  private void test(Compilation compilation, String... lines) {
    GeneratedResource output;
    output = compilation.getResource("code-output/code0021.txt");

    List<String> res;
    res = output.readAllLines();

    assertEquals(res.size(), lines.length);

    assertTrue(res.containsAll(UnmodifiableList.copyOf(lines)));
  }

  private static class Code0021Processor extends AbstractProcessingRoundProcessor {

    private final GrowableList<String> nameList = new GrowableList<>();

    @Override
    public final Set<String> getSupportedAnnotationTypes() {
      return supportedAnnotationTypes(Marker1.class);
    }

    @Override
    protected final boolean process(ProcessingRound round) {
      UnmodifiableSet<ProcessingType> types = round.getAnnotatedTypes();
      for (ProcessingType type : types) {
        NamedClass className = type.getName();
        nameList.add(className.getCanonicalName());
      }
      if (round.isOver()) {
        write(round);
      }
      return round.claimTheseAnnotations();
    }

    private void write(ProcessingRound round) {
      try {
        String resourceName = "code-output/code0021.txt";
        String contents = nameList.join("\n");
        round.writeResource(resourceName, contents);
      } catch (IOException e) {
        round.printMessageError(e);
      }
    }

  }
}