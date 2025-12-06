/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import objectos.way.Script.BooleanQuery;
import objectos.way.Script.Callback;
import objectos.way.Script.StringQuery;

final class ScriptWriter {

  private final List<String> actions = new ArrayList<>();

  public final void add(Object next) {
    final ScriptWriter other;
    other = (ScriptWriter) next;

    final List<String> otherActions;
    otherActions = other.actions;

    actions.addAll(otherActions);
  }

  // actions

  public final void delay(int ms, Callback callback) {
    final String call;
    call = toString(callback);

    actions.add(
        """
        ["delay-0",%d,%s]""".formatted(ms, call)
    );
  }

  public final void html(Html.Template template) {
    final String value;
    value = template.toJsonString();

    actions.add(
        """
        ["html-0","%s"]""".formatted(value)
    );
  }

  public final void navigate() {
    actions.add(
        """
        ["navigate-0"]"""
    );
  }

  public final void pushState(String url) {
    actions.add(
        """
        ["push-state-0","%s"]""".formatted(url)
    );
  }

  public final void replaceState(String url) {
    actions.add(
        """
        ["replace-state-0","%s"]""".formatted(url)
    );
  }

  public final void request(ScriptRequestOptions value) {
    final String method;
    method = value.method.name();

    final Object url;
    url = value.url;

    final String tmpl;
    tmpl = url instanceof ScriptStringQuery
        ? """
          ["request-0","%s",%s,%s]"""
        : """
          ["request-0","%s","%s",%s]""";

    final String onSuccess;
    onSuccess = toString(value.onSuccess);

    actions.add(
        tmpl.formatted(method, url, onSuccess)
    );
  }

  public final void stopPropagation() {
    actions.add(
        """
        ["stop-propagation-0"]"""
    );
  }

  // element

  public final void elementAction(Object locator, String action) {
    final String elem;
    elem = elementAction(locator);

    actions.add(
        """
        [%s,"%s"]""".formatted(elem, action)
    );
  }

  public final String elementAttr(Object locator, String attrName) {
    final String elem;
    elem = elementMethodInvocation(locator);

    return """
    [%s,"getAttribute","%s"]""".formatted(elem, attrName);
  }

  public final void elementAttr(Object locator, String attrName, String value) {
    final String elem;
    elem = elementMethodInvocation(locator);

    actions.add(
        """
        [%s,"setAttribute","%s","%s"]""".formatted(elem, attrName, value)
    );
  }

  public final void elementAttr(Object locator, String attrName, StringQuery value) {
    final String elem;
    elem = elementMethodInvocation(locator);

    actions.add(
        """
        [%s,"setAttribute","%s",%s]""".formatted(elem, attrName, value)
    );
  }

  public final void elementScroll(Object locator, int x, int y) {
    final String elem;
    elem = elementMethodInvocation(locator);

    actions.add(
        """
        [%s,"scroll",%d,%d]""".formatted(elem, x, y)
    );
  }

  public final void elementToggleClass(Object locator, String classes) {
    final String elem;
    elem = elementAction(locator);

    final String[] parts;
    parts = classes.split(" ");

    final String args;
    args = join(parts);

    actions.add(
        """
        [%s,"toggle-class-0",%s]""".formatted(elem, args)
    );
  }

  // boolean

  public final void booleanWhen(BooleanQuery query, boolean value, Callback action) {
    final String call;
    call = toString(action);

    actions.add(
        """
        ["boolean-when-0",%s,%b,%s]""".formatted(query, value, call)
    );
  }

  // string

  public final String stringTest(StringQuery query, String value) {
    return """
    ["string-test-0",%s,"%s"]""".formatted(query, value);
  }

  @Override
  public final String toString() {
    return actions.stream().collect(Collectors.joining(",", "[", "]"));
  }

  private String elementAction(Object locator) {
    return switch (locator) {
      case null -> "\"element-2\"";

      case String id -> "\"id-2\",\"" + id + "\"";

      case StringQuery q -> "\"id-2\"," + q;

      default -> throw new IllegalArgumentException("Unknown locator type: " + locator.getClass());
    };
  }

  private String elementMethodInvocation(Object locator) {
    return switch (locator) {
      case null -> "\"element-1\"";

      case String id -> "\"id-1\",\"" + id + "\"";

      case StringQuery q -> "\"id-1\"," + q;

      default -> throw new IllegalArgumentException("Unknown locator type: " + locator.getClass());
    };
  }

  private String join(String[] parts) {
    return Stream.of(parts).collect(Collectors.joining(","));
  }

  private String toString(Callback callback) {
    final int startIndex;
    startIndex = actions.size();

    callback.execute();

    final int endIndex;
    endIndex = actions.size();

    if (endIndex > startIndex) {
      final StringBuilder sb;
      sb = new StringBuilder();

      sb.append('[');

      final ListIterator<String> iter;
      iter = actions.listIterator(startIndex);

      if (iter.hasNext()) {
        sb.append(iter.next());

        iter.remove();

        while (iter.hasNext()) {
          sb.append(',');

          sb.append(iter.next());

          iter.remove();
        }
      }

      sb.append(']');

      return sb.toString();
    } else {
      return "[]";
    }
  }

  // TODO escape json string literal

}