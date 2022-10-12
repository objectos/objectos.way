/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.declaration;

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class EnumCodeBuilderTest extends AbstractCodeJavaTest {
  
  @Test
  public void implementedInterface() {
    NamedClass iface1 = TESTING_CODE.nestedClass("Iface1");
    NamedClass iface2 = TESTING_CODE.nestedClass("Iface2");
    test(
        EnumCode.builder()
            .addImplementedInterface(iface1)
            .build(),
        "enum Unnamed implements testing.code.Iface1 {}"
    );
    test(
        EnumCode.builder()
            .addImplementedInterface(iface1)
            .addImplementedInterface(iface2)
            .build(),
        "enum Unnamed implements testing.code.Iface1, testing.code.Iface2 {}"
    );
    test(
        EnumCode.builder()
            .addImplementedInterfaces(UnmodifiableList.of(iface2, iface1))
            .build(),
        "enum Unnamed implements testing.code.Iface2, testing.code.Iface1 {}"
    );
  }

}
