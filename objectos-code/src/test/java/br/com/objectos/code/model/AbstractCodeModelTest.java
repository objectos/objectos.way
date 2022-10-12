/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.model;

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.model.element.ProcessingAnnotation;
import br.com.objectos.code.util.AbstractCodeCoreTest;
import java.util.List;
import java.util.NoSuchElementException;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public abstract class AbstractCodeModelTest extends AbstractCodeCoreTest {

  protected final UnmodifiableList<NamedClass> annotationToClassName(
      List<ProcessingAnnotation> annotations) {
    GrowableList<NamedClass> result;
    result = new GrowableList<>();

    for (int i = 0; i < annotations.size(); i++) {
      ProcessingAnnotation ann;
      ann = annotations.get(i);

      NamedClass className;
      className = ann.className();

      result.add(className);
    }

    return result.toUnmodifiableList();
  }

  protected final <E> E filterAndGetOnly(UnmodifiableList<E> list, Predicate<E> predicate) {
    E result;
    result = null;

    for (int i = 0; i < list.size(); i++) {
      E element = list.get(i);

      if (predicate.test(element)) {
        result = element;

        break;
      }
    }

    if (result != null) {
      return result;
    } else {
      throw new NoSuchElementException();
    }
  }

  protected final <F, T> UnmodifiableList<T> map(List<F> list, Function<? super F, ? extends T> f) {
    GrowableList<T> result;
    result = new GrowableList<>();

    for (F e : list) {
      T apply = f.apply(e);

      result.add(apply);
    }

    return result.toUnmodifiableList();
  }

}
