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
package br.com.objectos.css;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class SelectorParserTest {

  @Test
  public void parse() {
    assertEquals(parser("span").parse(), selector("span"));
  }

  @Test
  public void parse_should_trim() {
    assertEquals(parser(" #a   ").parse(), selector("#a"));
  }

  @Test
  public void parse_should_keep_descendant() {
    assertEquals(parser("#a #b").parse(), selector("#a #b"));
  }

  @Test
  public void parse_should_trim_descendant() {
    assertEquals(parser(" #a   #b   ").parse(), selector("#a #b"));
  }

  @Test
  public void parser_list() {
    assertEquals(parser("span,div").parse(), list(selector("span"), selector("div")));
  }

  @Test
  public void parser_should_trim_list() {
    assertEquals(parser(" span , div  ").parse(), list(selector("span"), selector("div")));
  }

  private SelectorParser parser(String value) {
    return new SelectorParser(value);
  }

  private ListSelector list(Selector first, Selector second) {
    return new ListSelector(first, second);
  }

  private SimpleSelector selector(String value) {
    return new SimpleSelector(value);
  }

}