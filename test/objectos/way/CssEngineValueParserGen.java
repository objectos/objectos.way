/*
 * Objectos Start
 * Copyright (C) 2025 Objectos Software LTDA.
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
package objectos.way;

final class CssEngineValueParserGen {

  private int state = 0;

  public static void main(String[] args) {
    final CssEngineValueParserGen gen;
    gen = new CssEngineValueParserGen();

    gen.value("$DECLARATION");

    gen.line();

    gen.value("$HYPHEN1");
    gen.value("$HYPHEN2");

    gen.line();

    gen.value("$VAR_NAME");

    gen.line();

    gen.value("$COLON");

    gen.line();

    gen.value("$VALUE");
    gen.value("$VALUE_CHAR");
    gen.value("$VALUE_WS");

    gen.line();

    gen.value("$NS_GLOBAL");
    gen.value("$NS_VALUE");
    gen.value("$NS_VALUE_CHAR");
    gen.value("$NS_VALUE_WS");
  }

  private void line() {
    System.out.println();
  }

  private void value(String name) {
    System.out.printf("static final byte %s = %d;%n", name, state++);
  }

}