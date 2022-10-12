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
package br.com.objectos.code.processing.type;

import br.com.objectos.code.java.type.NamedWildcard;
import java.util.NoSuchElementException;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;

public final class PWildcardType extends PTypeMirror {

  private PTypeMirror bound;

  private Kind kind;

  private NamedWildcard name;

  private final WildcardType type;

  PWildcardType(ProcessingEnvironment processingEnv, WildcardType type) {
    super(processingEnv);
    this.type = type;
  }

  public final PTypeMirror getBound() {
    return getWildcardKind().getBound(bound);
  }

  @Override
  public final NamedWildcard getName() {
    if (name == null) {
      name = getName0();
    }

    return name;
  }

  public final Kind getWildcardKind() {
    if (kind == null) {
      kind = getWildcardKind0();
    }

    return kind;
  }

  @Override
  public final boolean isWildcardType() {
    return true;
  }

  @Override
  public final PWildcardType toWildcardType() {
    return this;
  }

  @Override
  final WildcardType getType() {
    return type;
  }

  private NamedWildcard getName0() {
    return getWildcardKind().getName0(bound);
  }

  private Kind getWildcardKind0() {
    TypeMirror extendsBound;
    extendsBound = type.getExtendsBound();

    if (extendsBound != null) {
      bound = PTypeMirror.adapt(processingEnv, extendsBound);

      return Kind.EXTENDS;
    }

    TypeMirror superBound;
    superBound = type.getSuperBound();

    if (superBound != null) {
      bound = PTypeMirror.adapt(processingEnv, superBound);

      return Kind.SUPER;
    }

    return Kind.UNBOUND;
  }

  public enum Kind {

    EXTENDS {
      @Override
      final NamedWildcard getName0(PTypeMirror bound) {
        return NamedWildcard.extendsBound(bound.getName());
      }
    },

    SUPER {
      @Override
      final NamedWildcard getName0(PTypeMirror bound) {
        return NamedWildcard.superBound(bound.getName());
      }
    },

    UNBOUND {
      @Override
      final PTypeMirror getBound(PTypeMirror bound) {
        throw new NoSuchElementException("No bound");
      }

      @Override
      final NamedWildcard getName0(PTypeMirror bound) {
        return NamedWildcard.unbound();
      }
    };

    PTypeMirror getBound(PTypeMirror bound) {
      return bound;
    }

    abstract NamedWildcard getName0(PTypeMirror bound);

  }

}
