/*
 * Copyright 2023 Objectos Software LTDA.
 *
 * Reprodução parcial ou total proibida.
 */
package objectos.asciidoc.pseudom;

public sealed interface Node permits Heading, Text {}