/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.element.Keywords._finally;
import static br.com.objectos.code.java.expression.Expressions.id;
import static br.com.objectos.code.java.expression.Expressions.invoke;
import static br.com.objectos.code.java.statement.CatchElement._catch;
import static br.com.objectos.code.java.statement.ResourceElement.resource;
import static br.com.objectos.code.java.statement.ReturnStatement._return;
import static br.com.objectos.code.java.statement.TryStatement._try;

import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import org.testng.annotations.Test;

public class TryStatementTest extends AbstractCodeJavaTest {

  @Test
  public void tryCatchFinallyStatement() {
    Identifier e = id("e");
    test(
        _try(
            invoke("mayThrow"),

            _catch(IOException.class, e),
            invoke("treatEx", e),

            _finally(),
            invoke("closeStuff")
        ),
        "try {",
        "  mayThrow();",
        "} catch (java.io.IOException e) {",
        "  treatEx(e);",
        "} finally {",
        "  closeStuff();",
        "}"
    );
  }

  @Test
  public void tryCatchStatement() {
    Identifier e = id("e");
    test(
        _try(
            invoke("mayThrow"),

            _catch(IOException.class, e),
            invoke("treatEx", e)
        ),
        "try {",
        "  mayThrow();",
        "} catch (java.io.IOException e) {",
        "  treatEx(e);",
        "}"
    );
  }

  @Test
  public void tryWithMultiCatch() {
    Identifier e = id("e");
    test(
        _try(
            id("future").invoke("get"),
            _catch(InterruptedException.class, ExecutionException.class, e),
            invoke("treatEx", e)
        ),
        "try {",
        "  future.get();",
        "} catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException e) {",
        "  treatEx(e);",
        "}"
    );
  }

  @Test
  public void tryWithResources() {
    Identifier reader = id("reader");
    test(
        _try(
            resource(BufferedReader.class, reader, invoke("getReader", id("something"))),
            _return(reader.invoke("readLine"))
        ),
        "try (java.io.BufferedReader reader = getReader(something)) {",
        "  return reader.readLine();",
        "}");
  }

  @Test
  public void tryWithResourcesCatch() {
    Identifier stmt = id("stmt");
    test(
        _try(
            resource(InputStream.class, stmt, id("conn").invoke("createStatement")),
            invoke("process", stmt),

            _catch(IOException.class, id("e")),
            invoke("treatEx", id("e"))
        ),
        "try (java.io.InputStream stmt = conn.createStatement()) {",
        "  process(stmt);",
        "} catch (java.io.IOException e) {",
        "  treatEx(e);",
        "}");
  }

}