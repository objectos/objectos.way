/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.type;

import br.com.objectos.code.java.io.JavaFileImportSet;
import objectos.lang.Check;

public abstract class NamedWildcard extends NamedType {

  static final NamedWildcard UNBOUND = new Unbound();

  NamedWildcard() {}

  public static NamedWildcard extendsBound(NamedType bound) {
    Check.notNull(bound, "bound == null");
    return new Extends((NamedReferenceType) bound);
  }

  public static NamedWildcard superBound(NamedType bound) {
    Check.notNull(bound, "bound == null");
    return new Super((NamedReferenceType) bound);
  }

  public static NamedWildcard unbound() {
    return UNBOUND;
  }

  static NamedWildcard extendsUnchecked(NamedReferenceType bound) {
    return new Extends(bound);
  }

  static NamedWildcard superUnchecked(NamedReferenceType bound) {
    return new Super(bound);
  }

  public static class Extends extends WithBound {
    private Extends(NamedReferenceType bound) {
      super(bound);
    }

    @Override
    public <R, P> R acceptTypeNameVisitor(NamedTypeVisitor<R, P> visitor, P p) {
      return visitor.visitNamedWildcardExtends(this, p);
    }

    @Override
    final String keyword() {
      return "extends";
    }
  }

  public static class Super extends WithBound {
    private Super(NamedReferenceType bound) {
      super(bound);
    }

    @Override
    public final <R, P> R acceptTypeNameVisitor(NamedTypeVisitor<R, P> visitor, P p) {
      return visitor.visitNamedWildcardSuper(this, p);
    }

    @Override
    final String keyword() {
      return "super";
    }
  }

  public static class Unbound extends NamedWildcard {
    private Unbound() {}

    @Override
    public final String acceptJavaFileImportSet(JavaFileImportSet set) {
      return toString();
    }

    @Override
    public final <R, P> R acceptTypeNameVisitor(NamedTypeVisitor<R, P> visitor, P p) {
      return visitor.visitNamedWildcardUnbound(this, p);
    }

    @Override
    public final String toString() {
      return "?";
    }
  }

  private static abstract class WithBound extends NamedWildcard {
    private final NamedReferenceType bound;

    WithBound(NamedReferenceType bound) {
      this.bound = bound;
    }

    @Override
    public final String acceptJavaFileImportSet(JavaFileImportSet set) {
      return toString(set.get(bound));
    }

    @Override
    public final boolean equals(Object obj) {
      if (obj == this) {
        return true;
      }
      if (!(obj instanceof WithBound)) {
        return false;
      }
      WithBound that = (WithBound) obj;
      return getClass().equals(that.getClass())
          && bound.equals(that.bound);
    }

    @Override
    public final int hashCode() {
      return bound.hashCode();
    }

    @Override
    public final String toString() {
      return toString(bound.toString());
    }

    abstract String keyword();

    private String toString(String type) {
      return "? " + keyword() + " " + type;
    }
  }

}