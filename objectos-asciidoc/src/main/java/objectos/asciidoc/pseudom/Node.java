/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.asciidoc.pseudom;

import java.io.IOException;
import objectos.asciidoc.internal.PseudoHeader;
import objectos.asciidoc.internal.PseudoInlineMacro;
import objectos.asciidoc.internal.PseudoListItem;
import objectos.asciidoc.internal.PseudoParagraph;
import objectos.asciidoc.internal.PseudoSection;
import objectos.asciidoc.internal.PseudoText;
import objectos.asciidoc.internal.PseudoTitle;
import objectos.asciidoc.internal.PseudoUnorderedList;

public sealed interface Node {

  sealed interface Header extends Node permits PseudoHeader {

    IterableOnce<Node> nodes();

  }

  sealed interface InlineMacro extends Node permits PseudoInlineMacro {

    Attributes attributes();

    String name();

    void targetTo(Appendable out) throws IOException;

  }

  sealed interface ListItem extends Node permits PseudoListItem {

    IterableOnce<Node> nodes();

  }

  sealed interface Paragraph extends Node permits PseudoParagraph {

    IterableOnce<Node> nodes();

  }

  sealed interface Section extends Node permits PseudoSection {

    Attributes attributes();

    int level();

    IterableOnce<Node> nodes();

  }

  sealed interface Text extends Node permits PseudoText {

    void appendTo(Appendable out) throws IOException;

  }

  sealed interface Title extends Node permits PseudoTitle {

    int level();

    IterableOnce<Node> nodes();

  }

  sealed interface UnorderedList extends Node permits PseudoUnorderedList {

    IterableOnce<Node> nodes();

  }

}