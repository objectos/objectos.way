/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.model.element;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import br.com.objectos.code.util.AbstractCodeCoreTest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.testng.annotations.Test;

public class GetResourceTest {

  @Test
  public void getResource() throws IOException {
    Class<? extends GetResourceTest> type = getClass();

    Module module = type.getModule();

    try (
        InputStream in = module.getResourceAsStream("br/com/objectos/code/util/get-resource.txt")
    ) {
      assertNotNull(in);

      String txt = readString(in);

      assertEquals(txt, "got it!");
    }

    try (
        InputStream in = AbstractCodeCoreTest.class.getResourceAsStream("get-resource.txt")
    ) {
      assertNotNull(in);

      String txt = readString(in);

      assertEquals(txt, "got it!");
    }

    ClassLoader loader = Thread.currentThread().getContextClassLoader();

    URL url = loader.getResource("code-testing/get-resource.txt");

    assertNotNull(url);
  }

  private String readString(InputStream in) throws IOException {
    var out = new ByteArrayOutputStream();

    in.transferTo(out);

    return new String(out.toByteArray());
  }

}
