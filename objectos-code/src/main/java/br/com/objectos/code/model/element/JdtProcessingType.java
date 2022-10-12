/*
 * Original source:
 *
 * Copyright (c) 2012, 2018, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * ---------
 *
 * Modifications:
 *
 * Copyright (C) 2019-2020 Objectos Software LTDA
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package br.com.objectos.code.model.element;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

final class JdtProcessingType extends ProcessingType {

  JdtProcessingType(ProcessingEnvironment processingEnv, TypeElement subject) {
    super(processingEnv, subject);
  }

  private static void collectSourceOrder(TreeMap<Integer, Object> orderedBindings,
      Object referenceContext, String fieldName) throws Exception {
    Object[] declarations = (Object[]) field(referenceContext, fieldName);
    if (declarations != null) {
      for (int i = 0; i < declarations.length; i++) {
        Integer declarationSourceStart = (Integer) field(declarations[i], "declarationSourceStart");
        orderedBindings.put(declarationSourceStart, field(declarations[i], "binding"));
      }
    }
  }

  private static Object field(Object o, String fieldName) throws Exception {
    if (o == null) {
      return null;
    }
    Class<?> clazz = o.getClass();
    Field field = null;
    try {
      field = clazz.getField(fieldName);
    } catch (NoSuchFieldException e) {
      while (clazz != null) {
        try {
          field = clazz.getDeclaredField(fieldName);
          break;
        } catch (NoSuchFieldException e1) {
          clazz = clazz.getSuperclass();
        }
      }
      if (field == null) {
        throw e;
      }
    }
    field.setAccessible(true);
    return field.get(o);
  }

  private static List<Object> findBinaryTypeOrder(Object binding) throws Exception {
    Object binaryType = lookupBinaryType(binding);
    final Object[] sortedMethods = (Object[]) method(binaryType, "getMethods");

    List<Object> sortedElements = new ArrayList<>();
    if (sortedMethods != null) {
      sortedElements.addAll(Arrays.asList(sortedMethods));
    }
    final Object[] sortedFields = (Object[]) method(binaryType, "getFields");
    if (sortedFields != null) {
      sortedElements.addAll(Arrays.asList(sortedFields));
    }
    final Object[] sortedTypes = (Object[]) method(binaryType, "getMemberTypes", new Class<?>[0]);
    if (sortedTypes != null) {
      sortedElements.addAll(Arrays.asList(sortedTypes));
    }

    Collections.sort(sortedElements, new Comparator<Object>() {
      @Override
      public int compare(Object o1, Object o2) {
        try {
          int structOffset1 = ((Integer) field(o1, "structOffset")).intValue();
          int structOffset2 = ((Integer) field(o2, "structOffset")).intValue();
          return structOffset1 - structOffset2;
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });

    ClassLoader classLoader = binding.getClass().getClassLoader();
    Class<?> binaryMethod
        = classLoader.loadClass("org.eclipse.jdt.internal.compiler.env.IBinaryMethod");
    Class<?> binaryField
        = classLoader.loadClass("org.eclipse.jdt.internal.compiler.env.IBinaryField");
    Class<?> nestedType
        = classLoader.loadClass("org.eclipse.jdt.internal.compiler.env.IBinaryNestedType");

    List<Object> bindings = new ArrayList<>();
    for (Object sortedElement : sortedElements) {
      Class<?> elementClass = sortedElement.getClass();
      if (binaryMethod.isAssignableFrom(elementClass)) {
        char[] selector = (char[]) method(sortedElement, "getSelector");
        Object[] foundBindings
            = (Object[]) method(binding, "getMethods", new Class<?>[] {char[].class}, selector);
        if (foundBindings == null || foundBindings.length == 0) {
          continue;
        } else if (foundBindings.length == 1) {
          bindings.add(foundBindings[0]);
        } else {
          char[] idescriptor = (char[]) method(sortedElement, "getMethodDescriptor");
          for (Object foundBinding : foundBindings) {
            char[] descriptor = (char[]) method(foundBinding, "signature");
            if (descriptor == null && idescriptor == null
                || Arrays.equals(descriptor, idescriptor)) {
              bindings.add(foundBinding);
              break;
            }
          }
        }
      } else if (binaryField.isAssignableFrom(elementClass)) {
        char[] selector = (char[]) method(sortedElement, "getName");
        Object foundField = method(binding, "getField",
          new Class<?>[] {char[].class, boolean.class}, selector, true);
        if (foundField != null) {
          bindings.add(foundField);
        }
      } else if (nestedType.isAssignableFrom(elementClass)) {
        char[] selector = (char[]) method(sortedElement, "getSourceName");
        Object foundType
            = method(binding, "getMemberType", new Class<?>[] {char[].class}, selector);
        if (foundType != null) {
          bindings.add(foundType);
        }
      } else {
        throw new AssertionError("Unexpected encountered type " + elementClass);
      }
    }

    return bindings;
  }

  /*
   * Source of the code below:
   * https://github.com/oracle/graal/blob/master/truffle/src/com.oracle.truffle.dsl.processor/src/com/oracle/truffle/dsl/processor/java/compiler/JDTCompiler.java
   */

  private static List<Object> findSourceTypeOrder(Object binding) throws Exception {
    Object referenceContext = field(field(binding, "scope"), "referenceContext");

    TreeMap<Integer, Object> orderedBindings = new TreeMap<>();

    collectSourceOrder(orderedBindings, referenceContext, "methods");
    collectSourceOrder(orderedBindings, referenceContext, "fields");
    collectSourceOrder(orderedBindings, referenceContext, "memberTypes");

    return new ArrayList<Object>(orderedBindings.values());
  }

  private static String fixECJBinaryNameIssue(String name) {
    if (name.contains("$")) {
      int lastIndex = name.lastIndexOf('$');
      return name.substring(lastIndex + 1, name.length());
    }
    return name;
  }

  private static TypeElement fromTypeMirror(TypeMirror mirror) {
    switch (mirror.getKind()) {
      case DECLARED:
        return (TypeElement) ((DeclaredType) mirror).asElement();
      case ARRAY:
        return fromTypeMirror(((ArrayType) mirror).getComponentType());
      default:
        return null;
    }
  }

  private static String getDeclaredName(DeclaredType element, boolean includeTypeVariables) {
    String simpleName = fixECJBinaryNameIssue(element.asElement().getSimpleName().toString());

    if (!includeTypeVariables || element.getTypeArguments().size() == 0) {
      return simpleName;
    }

    StringBuilder b = new StringBuilder(simpleName);
    b.append("<");
    if (element.getTypeArguments().size() > 0) {
      for (int i = 0; i < element.getTypeArguments().size(); i++) {
        b.append(getSimpleName(element.getTypeArguments().get(i)));
        if (i < element.getTypeArguments().size() - 1) {
          b.append(", ");
        }
      }
    }
    b.append(">");
    return b.toString();
  }

  private static String getQualifiedName(TypeElement element) {
    String qualifiedName = element.getQualifiedName().toString();
    if (qualifiedName.contains("$")) {
      /*
       * If a class gets loaded in its binary form by the ECJ compiler it fails to produce the
       * proper canonical class name. It leaves the $ in the qualified name of the class. So
       * one instance of a TypeElement may be loaded in binary and one in source form. The
       * current type comparison in #typeEquals compares by the qualified name so the
       * qualified name must match. This is basically a hack to fix the returned qualified
       * name of eclipse.
       */
      qualifiedName = qualifiedName.replace('$', '.');
    }
    return qualifiedName;
  }

  private static String getQualifiedName(TypeMirror mirror) {
    switch (mirror.getKind()) {
      case BOOLEAN:
        return "boolean";
      case BYTE:
        return "byte";
      case CHAR:
        return "char";
      case DOUBLE:
        return "double";
      case SHORT:
        return "short";
      case FLOAT:
        return "float";
      case INT:
        return "int";
      case LONG:
        return "long";
      case DECLARED:
        return getQualifiedName(fromTypeMirror(mirror));
      case ARRAY:
        return getQualifiedName(((ArrayType) mirror).getComponentType());
      case VOID:
        return "void";
      case NULL:
        return "null";
      case TYPEVAR:
        return getSimpleName(mirror);
      case ERROR:
        throw new JdtWorkaroundException("Type error " + mirror);
      case EXECUTABLE:
        return ((ExecutableType) mirror).toString();
      case NONE:
        return "$none";
      default:
        throw new RuntimeException("Unknown type specified " + mirror + " mirror: " + mirror);
    }
  }

  private static String getSimpleName(TypeMirror mirror) {
    switch (mirror.getKind()) {
      case BOOLEAN:
        return "boolean";
      case BYTE:
        return "byte";
      case CHAR:
        return "char";
      case DOUBLE:
        return "double";
      case FLOAT:
        return "float";
      case SHORT:
        return "short";
      case INT:
        return "int";
      case LONG:
        return "long";
      case DECLARED:
        return getDeclaredName((DeclaredType) mirror, true);
      case ARRAY:
        return getSimpleName(((ArrayType) mirror).getComponentType()) + "[]";
      case VOID:
        return "void";
      case NULL:
        return "null";
      case WILDCARD:
        return getWildcardName((WildcardType) mirror);
      case TYPEVAR:
        return ((TypeVariable) mirror).asElement().getSimpleName().toString();
      case ERROR:
        throw new JdtWorkaroundException("Type error " + mirror);
      case NONE:
        return "None";
      default:
        throw new RuntimeException(
          "Unknown type specified " + mirror.getKind() + " mirror: " + mirror);
    }
  }

  /*
   * Source of the code below:
   * https://github.com/oracle/graal/blob/master/truffle/src/com.oracle.truffle.dsl.processor/src/com/oracle/truffle/dsl/processor/java/ElementUtils.java
   */

  private static String getUniqueIdentifier(TypeMirror typeMirror) {
    if (typeMirror.getKind() == TypeKind.ARRAY) {
      return getUniqueIdentifier(((ArrayType) typeMirror).getComponentType()) + "[]";
    } else if (typeMirror.getKind() == TypeKind.TYPEVAR) {
      Element element = ((TypeVariable) typeMirror).asElement();
      String variableName = element.getSimpleName().toString();
      if (element.getEnclosingElement().getKind().isClass()) {
        return getUniqueIdentifier(element.getEnclosingElement().asType()) + "." + variableName;
      } else {
        return variableName;
      }
    } else {
      return getQualifiedName(typeMirror);
    }
  }

  private static String getWildcardName(WildcardType type) {
    StringBuilder b = new StringBuilder();
    if (type.getExtendsBound() != null) {
      b.append("? extends ").append(getSimpleName(type.getExtendsBound()));
    } else if (type.getSuperBound() != null) {
      b.append("? super ").append(getSimpleName(type.getExtendsBound()));
    }
    return b.toString();
  }

  private static Object lookupBinaryType(Object binding) throws Exception {
    Object lookupEnvironment = field(binding, "environment");
    Object compoundClassName = field(binding, "compoundName");
    Object nameEnvironment = field(lookupEnvironment, "nameEnvironment");
    Object nameEnvironmentAnswer
        = method(nameEnvironment, "findType", new Class<?>[] {char[][].class}, compoundClassName);
    Object binaryType = method(nameEnvironmentAnswer, "getBinaryType", new Class<?>[0]);
    return binaryType;
  }

  private static List<Object> lookupDeclarationOrder(TypeElement type) {
    List<Object> declarationOrder;
    try {
      Object binding = field(type, "_binding");
      ClassLoader classLoader = binding.getClass().getClassLoader();
      Class<?> sourceTypeBinding
          = classLoader.loadClass("org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding");
      Class<?> binaryTypeBinding
          = classLoader.loadClass("org.eclipse.jdt.internal.compiler.lookup.BinaryTypeBinding");

      declarationOrder = null;
      if (sourceTypeBinding.isAssignableFrom(binding.getClass())) {
        declarationOrder = findSourceTypeOrder(binding);
      } else if (binaryTypeBinding.isAssignableFrom(binding.getClass())) {
        declarationOrder = findBinaryTypeOrder(binding);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return declarationOrder;
  }

  private static Object method(Object o, String methodName) throws Exception {
    Method method = o.getClass().getMethod(methodName);
    method.setAccessible(true);
    return method.invoke(o);
  }

  private static Object method(
      Object o, String methodName, Class<?>[] paramTypes, Object... values) throws Exception {
    Method method = o.getClass().getMethod(methodName, paramTypes);
    method.setAccessible(true);
    return method.invoke(o, values);
  }

  private static boolean typeEquals(TypeMirror type1, TypeMirror type2) {
    if (type1 == type2) {
      return true;
    } else if (type1 == null || type2 == null) {
      return false;
    } else {
      if (type1.getKind() == type2.getKind()) {
        return getUniqueIdentifier(type1).equals(getUniqueIdentifier(type2));
      } else {
        return false;
      }
    }
  }

  @Override
  final List<? extends Element> getAllMembers() {
    return sortBySourceOrder(elementUtils().getAllMembers(element));
  }

  @Override
  final List<? extends Element> getEnclosedElements() {
    return sortBySourceOrder(element.getEnclosedElements());
  }

  /*
   * Source of the code below:
   * https://github.com/oracle/graal/blob/master/truffle/src/com.oracle.truffle.dsl.processor/src/com/oracle/truffle/dsl/processor/java/compiler/AbstractCompiler.java
   */

  @Override
  final ProcessingType newTypeElementQuery(TypeElement e) {
    return new JdtProcessingType(processingEnv, e);
  }

  private Comparator<Element> createSourceOrderComparator(final TypeElement enclosing) {
    Comparator<Element> comparator = new Comparator<Element>() {

      final List<Object> declarationOrder = lookupDeclarationOrder(enclosing);

      @Override
      public int compare(Element o1, Element o2) {
        try {
          Element enclosing1Element = o1.getEnclosingElement();
          Element enclosing2Element = o2.getEnclosingElement();

          if (!typeEquals(enclosing1Element.asType(),
            enclosing2Element.asType())) {
            throw new AssertionError();
          }

          Object o1Binding = field(o1, "_binding");
          Object o2Binding = field(o2, "_binding");

          int i1 = declarationOrder.indexOf(o1Binding);
          int i2 = declarationOrder.indexOf(o2Binding);

          if (i1 == -1 || i2 == -1) {
            return 0;
          }

          return i1 - i2;
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    };
    return comparator;
  }

  private List<? extends Element> sortBySourceOrder(List<? extends Element> elements) {
    Map<TypeElement, List<Element>> groupedByEnclosing = new HashMap<>();
    for (Element element : elements) {
      Element enclosing = element.getEnclosingElement();
      List<Element> grouped = groupedByEnclosing.get(enclosing);
      if (grouped == null) {
        grouped = new ArrayList<>();
        groupedByEnclosing.put((TypeElement) enclosing, grouped);
      }
      grouped.add(element);
    }

    for (TypeElement enclosing : groupedByEnclosing.keySet()) {
      Collections.sort(groupedByEnclosing.get(enclosing), createSourceOrderComparator(enclosing));
    }

    if (groupedByEnclosing.size() == 1) {
      return groupedByEnclosing.get(groupedByEnclosing.keySet().iterator().next());
    } else {
      List<TypeElement> enclosingTypes = new ArrayList<TypeElement>(groupedByEnclosing.keySet());

      Collections.sort(enclosingTypes, new Comparator<TypeElement>() {
        @Override
        public int compare(TypeElement o1, TypeElement o2) {
          if (typeUtils().isSubtype(o1.asType(), o2.asType())) {
            return 1;
          } else {
            return -1;
          }
        }
      });

      List<Element> sourceOrderElements = new ArrayList<>();
      for (TypeElement typeElement : enclosingTypes) {
        sourceOrderElements.addAll(groupedByEnclosing.get(typeElement));
      }
      return sourceOrderElements;
    }
  }

}