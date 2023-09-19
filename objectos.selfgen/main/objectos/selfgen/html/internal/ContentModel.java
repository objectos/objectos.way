/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.html.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

abstract class ContentModel {

  private static final Start START = new Start();
  private static final Varargs VARARGS = new Varargs();

  ContentModel() {}

  static ContentModel start() {
    return START;
  }

  public final ContentModel category(CategorySpec category) {
    return new Category(category);
  }

  public ContentModel except(Child element) {
    throw new UnsupportedOperationException("Implement me");
  }

  public boolean hasBuilder() {
    return true;
  }

  public boolean hasChildren() {
    return true;
  }

  public ContentModel one(Name child) {
    throw new UnsupportedOperationException("Implement me");
  }

  public void prepare(ElementSpec element) {
    // noop
  }

  public final ContentModel zeroOrMore(Name child) {
    return VARARGS;
  }

  private static class Category extends Varargs {
    private final Set<Child> exceptionSet = new HashSet<>();
    private final CategorySpec value;

    Category(CategorySpec value) {
      this.value = value;
    }

    @Override
    public final ContentModel except(Child element) {
      exceptionSet.add(element);
      return this;
    }

    @Override
    public final void prepare(ElementSpec element) {
      Iterable<Child> children;
      children = value.childStream();

      for (Child child : children) {
        if (filter0(child)) {
          child.addParent(element);
        }
      }
    }

    private boolean filter0(Child e) {
      return !exceptionSet.contains(e);
    }
  }

  private static class One extends ContentModel {
    private final List<Name> childList = new ArrayList<>();

    One(Name child) {
      childList.add(child);
    }

    @Override
    public final boolean hasBuilder() {
      return false;
    }

    @Override
    public final ContentModel one(Name child) {
      childList.add(child);
      return this;
    }
  }

  private static class Start extends ContentModel {
    @Override
    public final boolean hasBuilder() {
      return false;
    }

    @Override
    public final boolean hasChildren() {
      return false;
    }

    @Override
    public final ContentModel one(Name child) {
      return new One(child);
    }
  }

  private static class Varargs extends ContentModel {
    @Override
    public final ContentModel one(Name child) {
      return this;
    }
  }

}