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
import objectos.asciidoc.internal.PseudoEmphasis;
import objectos.asciidoc.internal.PseudoHeader;
import objectos.asciidoc.internal.PseudoInlineMacro;
import objectos.asciidoc.internal.PseudoListItem;
import objectos.asciidoc.internal.PseudoListingBlock;
import objectos.asciidoc.internal.PseudoMonospaced;
import objectos.asciidoc.internal.PseudoParagraph;
import objectos.asciidoc.internal.PseudoSection;
import objectos.asciidoc.internal.PseudoStrong;
import objectos.asciidoc.internal.PseudoText;
import objectos.asciidoc.internal.PseudoTextValue;
import objectos.asciidoc.internal.PseudoTitle;
import objectos.asciidoc.internal.PseudoUnorderedList;

public sealed interface Node {

  sealed interface ContainerNode extends Node {

    IterableOnce<Node> nodes();

  }

  sealed interface Header extends ContainerNode permits PseudoHeader {}

  sealed interface InlineMacro extends ContainerNode permits PseudoInlineMacro {

    String name();

    void targetTo(Appendable out) throws IOException;

  }

  sealed interface ListingBlock extends ContainerNode permits PseudoListingBlock {

    Attributes attributes();

  }

  sealed interface ListItem extends ContainerNode permits PseudoListItem {}

  sealed interface Emphasis extends ContainerNode permits PseudoEmphasis {}

  sealed interface Strong extends ContainerNode permits PseudoStrong {}

  sealed interface Monospaced extends ContainerNode permits PseudoMonospaced {}

  sealed interface Paragraph extends ContainerNode permits PseudoParagraph {}

  sealed interface Section extends ContainerNode permits PseudoSection {

    Attributes attributes();

    int level();

  }

  sealed interface Text extends Node permits PseudoText, PseudoTextValue {

    void appendTo(Appendable out) throws IOException;

  }

  sealed interface Title extends ContainerNode permits PseudoTitle {

    int level();

  }

  sealed interface UnorderedList extends ContainerNode permits PseudoUnorderedList {}

  enum Symbol implements Node {

    RIGHT_SINGLE_QUOTATION_MARK;

  }

}