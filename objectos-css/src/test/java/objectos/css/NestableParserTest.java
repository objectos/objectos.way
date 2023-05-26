/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.css;

import static objectos.css.NestableSymbol.DESCENDANT;
import static objectos.css.NestableSymbol.PARENT;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class NestableParserTest {

  @Test
  public void parse() {
    assertEquals(parser("span").parse(), SimpleNestable.of(PARENT, DESCENDANT, new Ident("span")));
  }

  @Test
  public void parse_should_trim() {
    assertEquals(parser(" #a   ").parse(), SimpleNestable.of(PARENT, DESCENDANT, new Ident("#a")));
  }

  @Test
  public void parse_should_keep_descendant() {
    assertEquals(parser("#a #b").parse(),
        SimpleNestable.of(PARENT, DESCENDANT, new Ident("#a #b")));
  }

  @Test
  public void parse_should_trim_descendant() {
    assertEquals(parser(" #a   #b   ").parse(),
        SimpleNestable.of(PARENT, DESCENDANT, new Ident("#a #b")));
  }

  @Test
  public void parse_parent_symbol() {
    assertEquals(parser("&::after").parse(), SimpleNestable.of(PARENT, new Ident("::after")));
  }

  @Test
  public void parse_parent_symbol_with_descendant() {
    assertEquals(parser("& ::after").parse(),
        SimpleNestable.of(PARENT, DESCENDANT, new Ident("::after")));
  }

  @Test
  public void parser_list() {
    SimpleNestable span = SimpleNestable.of(PARENT, DESCENDANT, new Ident("span"));
    SimpleNestable div = SimpleNestable.of(PARENT, DESCENDANT, new Ident("div"));
    assertEquals(parser("span,div").parse(), ListNestable.of(span, div));
  }

  @Test
  public void parser_should_trim_list() {
    SimpleNestable span = SimpleNestable.of(PARENT, DESCENDANT, new Ident("span"));
    SimpleNestable div = SimpleNestable.of(PARENT, DESCENDANT, new Ident("div"));
    assertEquals(parser(" span , div  ").parse(), ListNestable.of(span, div));
  }

  @Test
  public void parser_list_with_parent() {
    SimpleNestable span = SimpleNestable.of(PARENT, DESCENDANT, new Ident("span"));
    SimpleNestable div = SimpleNestable.of(PARENT, new Ident("::after"));
    assertEquals(parser("span,&::after").parse(), ListNestable.of(span, div));
  }

  @Test
  public void parse_combinator() {
    assertEquals(parser("> li").parse(),
        SimpleNestable.of(PARENT, Combinator.CHILD, new Ident("li")));
  }

  private NestableParser parser(String value) {
    return new NestableParser(value);
  }

}