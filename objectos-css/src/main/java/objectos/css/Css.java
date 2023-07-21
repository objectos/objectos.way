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

import objectos.css.internal.RandomStringGenerator;
import objectos.css.select.AttributeSelector;
import objectos.css.select.AttributeValueElement;
import objectos.css.select.AttributeValueOperator;
import objectos.css.select.AttributeValueSelector;
import objectos.css.select.ClassSelector;
import objectos.css.select.Combinator;
import objectos.css.select.IdSelector;
import objectos.css.select.PseudoClassSelector;
import objectos.css.select.PseudoClassSelectors;
import objectos.css.select.PseudoElementSelector;
import objectos.css.select.PseudoElementSelectors;
import objectos.css.select.Selector;
import objectos.css.select.SelectorElement;
import objectos.css.select.SelectorFactory;
import objectos.css.select.SelectorList;
import objectos.css.select.TypeSelector;
import objectos.css.select.TypeSelectors;
import objectos.css.select.UniversalSelector;
import objectos.css.type.Color;

public final class Css {

  public static final PseudoClassSelector ACTIVE = PseudoClassSelectors.ACTIVE;

  public static final PseudoClassSelector ANY_LINK = PseudoClassSelectors.ANY_LINK;

  public static final PseudoClassSelector BLANK = PseudoClassSelectors.BLANK;

  public static final PseudoClassSelector CHECKED = PseudoClassSelectors.CHECKED;

  public static final PseudoClassSelector CURRENT = PseudoClassSelectors.CURRENT;

  public static final PseudoClassSelector DEFAULT = PseudoClassSelectors.DEFAULT;

  public static final PseudoClassSelector DEFINED = PseudoClassSelectors.DEFINED;

  public static final PseudoClassSelector DISABLED = PseudoClassSelectors.DISABLED;

  public static final PseudoClassSelector DROP = PseudoClassSelectors.DROP;

  public static final PseudoClassSelector EMPTY = PseudoClassSelectors.EMPTY;

  public static final PseudoClassSelector ENABLED = PseudoClassSelectors.ENABLED;

  public static final PseudoClassSelector FIRST = PseudoClassSelectors.FIRST;

  public static final PseudoClassSelector FIRST_CHILD = PseudoClassSelectors.FIRST_CHILD;

  public static final PseudoClassSelector FIRST_OF_TYPE = PseudoClassSelectors.FIRST_OF_TYPE;

  public static final PseudoClassSelector FULLSCREEN = PseudoClassSelectors.FULLSCREEN;

  public static final PseudoClassSelector FUTURE = PseudoClassSelectors.FUTURE;

  public static final PseudoClassSelector FOCUS = PseudoClassSelectors.FOCUS;

  public static final PseudoClassSelector FOCUS_VISIBLE = PseudoClassSelectors.FOCUS_VISIBLE;

  public static final PseudoClassSelector FOCUS_WITHIN = PseudoClassSelectors.FOCUS_WITHIN;

  public static final PseudoClassSelector HOST = PseudoClassSelectors.HOST;

  public static final PseudoClassSelector HOVER = PseudoClassSelectors.HOVER;

  public static final PseudoClassSelector INDETERMINATE = PseudoClassSelectors.INDETERMINATE;

  public static final PseudoClassSelector IN_RANGE = PseudoClassSelectors.IN_RANGE;

  public static final PseudoClassSelector INVALID = PseudoClassSelectors.INVALID;

  public static final PseudoClassSelector LAST_CHILD = PseudoClassSelectors.LAST_CHILD;

  public static final PseudoClassSelector LAST_OF_TYPE = PseudoClassSelectors.LAST_OF_TYPE;

  public static final PseudoClassSelector LEFT = PseudoClassSelectors.LEFT;

  public static final PseudoClassSelector LINK = PseudoClassSelectors.LINK;

  public static final PseudoClassSelector LOCAL_LINK = PseudoClassSelectors.LOCAL_LINK;

  public static final PseudoClassSelector ONLY_CHILD = PseudoClassSelectors.ONLY_CHILD;

  public static final PseudoClassSelector ONLY_OF_TYPE = PseudoClassSelectors.ONLY_OF_TYPE;

  public static final PseudoClassSelector OPTIONAL = PseudoClassSelectors.OPTIONAL;

  public static final PseudoClassSelector OUT_OF_RANGE = PseudoClassSelectors.OUT_OF_RANGE;

  public static final PseudoClassSelector PAST = PseudoClassSelectors.PAST;

  public static final PseudoClassSelector PLACEHOLDER_SHOWN
      = PseudoClassSelectors.PLACEHOLDER_SHOWN;

  public static final PseudoClassSelector READ_ONLY = PseudoClassSelectors.READ_ONLY;

  public static final PseudoClassSelector READ_WRITE = PseudoClassSelectors.READ_WRITE;

  public static final PseudoClassSelector REQUIRED = PseudoClassSelectors.REQUIRED;

  public static final PseudoClassSelector RIGHT = PseudoClassSelectors.RIGHT;

  public static final PseudoClassSelector ROOT = PseudoClassSelectors.ROOT;

  public static final PseudoClassSelector SCOPE = PseudoClassSelectors.SCOPE;

  public static final PseudoClassSelector TARGET = PseudoClassSelectors.TARGET;

  public static final PseudoClassSelector TARGET_WITHIN = PseudoClassSelectors.TARGET_WITHIN;

  public static final PseudoClassSelector USER_INVALID = PseudoClassSelectors.USER_INVALID;

  public static final PseudoClassSelector VALID = PseudoClassSelectors.VALID;

  public static final PseudoClassSelector VISITED = PseudoClassSelectors.VISITED;

  public static final PseudoClassSelector _MOZ_FOCUSRING = PseudoClassSelectors._MOZ_FOCUSRING;

  public static final PseudoClassSelector _MOZ_UI_INVALID = PseudoClassSelectors._MOZ_UI_INVALID;

  public static final PseudoElementSelector AFTER = PseudoElementSelectors.AFTER;

  public static final PseudoElementSelector BACKDROP = PseudoElementSelectors.BACKDROP;

  public static final PseudoElementSelector BEFORE = PseudoElementSelectors.BEFORE;

  public static final PseudoElementSelector CUE = PseudoElementSelectors.CUE;

  public static final PseudoElementSelector FIRST_LETTER = PseudoElementSelectors.FIRST_LETTER;

  public static final PseudoElementSelector FIRST_LINE = PseudoElementSelectors.FIRST_LINE;

  public static final PseudoElementSelector GRAMMAR_ERROR = PseudoElementSelectors.GRAMMAR_ERROR;

  public static final PseudoElementSelector MARKER = PseudoElementSelectors.MARKER;

  public static final PseudoElementSelector PLACEHOLDER = PseudoElementSelectors.PLACEHOLDER;

  public static final PseudoElementSelector SELECTION = PseudoElementSelectors.SELECTION;

  public static final PseudoElementSelector SPELLING_ERROR = PseudoElementSelectors.SPELLING_ERROR;

  public static final PseudoElementSelector _MOZ_FOCUS_INNER
      = PseudoElementSelectors._MOZ_FOCUS_INNER;

  public static final PseudoElementSelector _WEBKIT_INNER_SPIN_BUTTON
      = PseudoElementSelectors._WEBKIT_INNER_SPIN_BUTTON;

  public static final PseudoElementSelector _WEBKIT_OUTER_SPIN_BUTTON
      = PseudoElementSelectors._WEBKIT_OUTER_SPIN_BUTTON;

  public static final PseudoElementSelector _WEBKIT_SEARCH_DECORATION
      = PseudoElementSelectors._WEBKIT_SEARCH_DECORATION;

  public static final PseudoElementSelector _WEBKIT_FILE_UPLOAD_BUTTON
      = PseudoElementSelectors._WEBKIT_FILE_UPLOAD_BUTTON;

  public static final TypeSelector a = TypeSelectors.a;

  public static final TypeSelector abbr = TypeSelectors.abbr;

  public static final TypeSelector article = TypeSelectors.article;

  public static final TypeSelector b = TypeSelectors.b;

  public static final TypeSelector blockquote = TypeSelectors.blockquote;

  public static final TypeSelector body = TypeSelectors.body;

  public static final TypeSelector br = TypeSelectors.br;

  public static final TypeSelector button = TypeSelectors.button;

  public static final TypeSelector clipPath = TypeSelectors.clipPath;

  public static final TypeSelector code = TypeSelectors.code;

  public static final TypeSelector dd = TypeSelectors.dd;

  public static final TypeSelector defs = TypeSelectors.defs;

  public static final TypeSelector details = TypeSelectors.details;

  public static final TypeSelector div = TypeSelectors.div;

  public static final TypeSelector dl = TypeSelectors.dl;

  public static final TypeSelector dt = TypeSelectors.dt;

  public static final TypeSelector em = TypeSelectors.em;

  public static final TypeSelector fieldset = TypeSelectors.fieldset;

  public static final TypeSelector figure = TypeSelectors.figure;

  public static final TypeSelector footer = TypeSelectors.footer;

  public static final TypeSelector form = TypeSelectors.form;

  public static final TypeSelector g = TypeSelectors.g;

  public static final TypeSelector h1 = TypeSelectors.h1;

  public static final TypeSelector h2 = TypeSelectors.h2;

  public static final TypeSelector h3 = TypeSelectors.h3;

  public static final TypeSelector h4 = TypeSelectors.h4;

  public static final TypeSelector h5 = TypeSelectors.h5;

  public static final TypeSelector h6 = TypeSelectors.h6;

  public static final TypeSelector head = TypeSelectors.head;

  public static final TypeSelector header = TypeSelectors.header;

  public static final TypeSelector hgroup = TypeSelectors.hgroup;

  public static final TypeSelector hr = TypeSelectors.hr;

  public static final TypeSelector html = TypeSelectors.html;

  public static final TypeSelector img = TypeSelectors.img;

  public static final TypeSelector input = TypeSelectors.input;

  public static final TypeSelector kbd = TypeSelectors.kbd;

  public static final TypeSelector label = TypeSelectors.label;

  public static final TypeSelector legend = TypeSelectors.legend;

  public static final TypeSelector li = TypeSelectors.li;

  public static final TypeSelector link = TypeSelectors.link;

  public static final TypeSelector main = TypeSelectors.main;

  public static final TypeSelector menu = TypeSelectors.menu;

  public static final TypeSelector meta = TypeSelectors.meta;

  public static final TypeSelector nav = TypeSelectors.nav;

  public static final TypeSelector ol = TypeSelectors.ol;

  public static final TypeSelector optgroup = TypeSelectors.optgroup;

  public static final TypeSelector option = TypeSelectors.option;

  public static final TypeSelector p = TypeSelectors.p;

  public static final TypeSelector path = TypeSelectors.path;

  public static final TypeSelector pre = TypeSelectors.pre;

  public static final TypeSelector progress = TypeSelectors.progress;

  public static final TypeSelector samp = TypeSelectors.samp;

  public static final TypeSelector script = TypeSelectors.script;

  public static final TypeSelector section = TypeSelectors.section;

  public static final TypeSelector select = TypeSelectors.select;

  public static final TypeSelector small = TypeSelectors.small;

  public static final TypeSelector span = TypeSelectors.span;

  public static final TypeSelector strong = TypeSelectors.strong;

  public static final TypeSelector style = TypeSelectors.style;

  public static final TypeSelector sub = TypeSelectors.sub;

  public static final TypeSelector summary = TypeSelectors.summary;

  public static final TypeSelector sup = TypeSelectors.sup;

  public static final TypeSelector svg = TypeSelectors.svg;

  public static final TypeSelector table = TypeSelectors.table;

  public static final TypeSelector tbody = TypeSelectors.tbody;

  public static final TypeSelector td = TypeSelectors.td;

  public static final TypeSelector template = TypeSelectors.template;

  public static final TypeSelector textarea = TypeSelectors.textarea;

  public static final TypeSelector th = TypeSelectors.th;

  public static final TypeSelector thead = TypeSelectors.thead;

  public static final TypeSelector title = TypeSelectors.title;

  public static final TypeSelector tr = TypeSelectors.tr;

  public static final TypeSelector ul = TypeSelectors.ul;

  private Css() {}

  public static Color hex(String text) {
    return Color.hex(text);
  }

  public static ClassSelector randomClassSelector(int length) {
    return RandomClassSelectorGenerator.randomClassSelector(length);
  }

  public static ClassSelector randomDot(int length) {
    return RandomClassSelectorGenerator.randomDot(length);
  }

  public static IdSelector randomHash(int length) {
    return RandomIdSelectorGenerator.randomHash(length);
  }

  public static IdSelector randomIdSelector(int length) {
    return RandomIdSelectorGenerator.randomIdSelector(length);
  }

  public static void randomSeed(long seed) {
    RandomStringGenerator.randomSeed(seed);
  }

  public static UniversalSelector any() {
    return SelectorFactory.any();
  }

  public static AttributeSelector attr(String name) {
    return SelectorFactory.attr(name);
  }

  public static AttributeValueSelector attr(String name, AttributeValueElement element) {
    return SelectorFactory.attr(name, element);
  }

  public static AttributeValueSelector attr(String name, AttributeValueOperator op, String value) {
    return SelectorFactory.attr(name, op, value);
  }

  public static ClassSelector cn(String className) {
    return SelectorFactory.cn(className);
  }

  public static AttributeValueElement contains(String value) {
    return SelectorFactory.contains(value);
  }

  public static ClassSelector dot(String className) {
    return SelectorFactory.dot(className);
  }

  public static AttributeValueElement endsWith(String value) {
    return SelectorFactory.endsWith(value);
  }

  public static AttributeValueElement eq(String value) {
    return SelectorFactory.eq(value);
  }

  public static Combinator gt() {
    return SelectorFactory.gt();
  }

  public static IdSelector id(String id) {
    return SelectorFactory.id(id);
  }

  public static AttributeValueElement lang(String value) {
    return SelectorFactory.lang(value);
  }

  public static SelectorList list(Selector... selectors) {
    return SelectorFactory.list(selectors);
  }

  public static Combinator or() {
    return SelectorFactory.or();
  }

  public static Combinator plus() {
    return SelectorFactory.plus();
  }

  public static Selector sel(Selector selector) {
    return SelectorFactory.sel(selector);
  }

  public static Selector sel(SelectorElement... elements) {
    return SelectorFactory.sel(elements);
  }

  public static Selector sel(SelectorElement e1, SelectorElement e2) {
    return SelectorFactory.sel(e1, e2);
  }

  public static Selector sel(SelectorElement e1, SelectorElement e2, SelectorElement e3) {
    return SelectorFactory.sel(e1, e2, e3);
  }

  public static Selector sel(SelectorElement e1, SelectorElement e2, SelectorElement e3,
      SelectorElement e4) {
    return SelectorFactory.sel(e1, e2, e3, e4);
  }

  public static Selector sel(SelectorElement e1, SelectorElement e2, SelectorElement e3,
      SelectorElement e4, SelectorElement e5) {
    return SelectorFactory.sel(e1, e2, e3, e4, e5);
  }

  public static Selector sel(SelectorElement e1, SelectorElement e2, SelectorElement e3,
      SelectorElement e4, SelectorElement e5, SelectorElement e6) {
    return SelectorFactory.sel(e1, e2, e3, e4, e5, e6);
  }

  public static Selector sel(SelectorElement e1, SelectorElement e2, SelectorElement e3,
      SelectorElement e4, SelectorElement e5, SelectorElement e6, SelectorElement e7) {
    return SelectorFactory.sel(e1, e2, e3, e4, e5, e6, e7);
  }

  public static Selector sel(SelectorElement e1, SelectorElement e2, SelectorElement e3,
      SelectorElement e4, SelectorElement e5, SelectorElement e6, SelectorElement e7,
      SelectorElement e8) {
    return SelectorFactory.sel(e1, e2, e3, e4, e5, e6, e7, e8);
  }

  public static Selector sel(SelectorElement e1, SelectorElement e2, SelectorElement e3,
      SelectorElement e4, SelectorElement e5, SelectorElement e6, SelectorElement e7,
      SelectorElement e8, SelectorElement e9) {
    return SelectorFactory.sel(e1, e2, e3, e4, e5, e6, e7, e8, e9);
  }

  public static Combinator sp() {
    return SelectorFactory.sp();
  }

  public static UniversalSelector star() {
    return SelectorFactory.star();
  }

  public static AttributeValueElement startsWith(String value) {
    return SelectorFactory.startsWith(value);
  }

  public static Combinator tilde() {
    return SelectorFactory.tilde();
  }

  public static AttributeValueElement wsList(String value) {
    return SelectorFactory.wsList(value);
  }

}