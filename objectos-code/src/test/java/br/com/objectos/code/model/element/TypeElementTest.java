/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.model.element;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.util.AbstractCodeCoreTest;
import java.util.List;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import objectos.util.GrowableList;
import org.testng.annotations.Test;

public class TypeElementTest extends AbstractCodeCoreTest {

  @Test
  public void getAllMembers() {
    Elements elements;
    elements = processingEnv.getElementUtils();

    String subjectCanonicalName;
    subjectCanonicalName = AllMemberSubject.class.getCanonicalName();

    TypeElement subjectTypeElement;
    subjectTypeElement = elements.getTypeElement(subjectCanonicalName);

    List<? extends Element> allMembers;
    allMembers = elements.getAllMembers(subjectTypeElement);

    List<ExecutableElement> subjectAllMethods;
    subjectAllMethods = ElementFilter.methodsIn(allMembers);

    GrowableList<String> methodNames;
    methodNames = new GrowableList<>();

    TypeElement objectTypeElement;
    objectTypeElement = elements.getTypeElement("java.lang.Object");

    for (int i = 0; i < subjectAllMethods.size(); i++) {
      ExecutableElement method;
      method = subjectAllMethods.get(i);

      Element methodEnclosingElement;
      methodEnclosingElement = method.getEnclosingElement();

      if (methodEnclosingElement.equals(objectTypeElement)) {
        continue;
      }

      methodNames.add(method.getSimpleName().toString());
    }

    assertEquals(methodNames.size(), 2);
    assertTrue(methodNames.contains("packageMethod"));
    assertTrue(methodNames.contains("packageMethodFromParent"));

    assertNotEquals(subjectAllMethods.size(), 2);
  }

  @Test
  public void getQualifiedName() {
    Elements elements;
    elements = processingEnv.getElementUtils();

    String subjectCanonicalName;
    subjectCanonicalName = QualifiedNameSubject.class.getCanonicalName();

    TypeElement subjectTypeElement;
    subjectTypeElement = elements.getTypeElement(subjectCanonicalName);

    List<? extends Element> subjectMembers;
    subjectMembers = elements.getAllMembers(subjectTypeElement);

    List<TypeElement> subjectMembersTypes;
    subjectMembersTypes = ElementFilter.typesIn(subjectMembers);

    assertEquals(subjectMembersTypes.size(), 1);

    TypeElement innerTypeElement;
    innerTypeElement = subjectMembersTypes.get(0);

    Name innerQualifiedName;
    innerQualifiedName = innerTypeElement.getQualifiedName();

    assertEquals(
        innerQualifiedName.toString(),
        QualifiedNameSubject.Inner.class.getCanonicalName(),
        "value returned by getQualifiedName() should be equal to the canonical name"
    );
  }

  private static class AllMembersParent {

    void packageMethodFromParent() {
      privateMethodFromParent();
    }

    private void privateMethodFromParent() {}

  }

  @SuppressWarnings("unused")
  private static class AllMemberSubject extends AllMembersParent {

    void packageMethod() {
      packageMethodFromParent();
    }

  }

  private static class QualifiedNameParent {
    static interface Inner {}
  }

  private static class QualifiedNameSubject extends QualifiedNameParent {}

}
