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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class CssEngineTestUtilities {

  private static abstract class AbstractSubject extends Html.Template {
    @Override
    protected final void render() {
      div(
          renderFragment(this::classes)
      );
    }

    abstract void classes();
  }

  @Test
  public void alignContent() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("align-content:normal align-content:flex-start");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .align-content\\:normal { align-content: normal }
          .align-content\\:flex-start { align-content: flex-start }
        }
        """
    );
  }

  @Test
  public void alignItems() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("align-items:flex-start align-items:center");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .align-items\\:flex-start { align-items: flex-start }
          .align-items\\:center { align-items: center }
        }
        """
    );
  }

  @Test
  public void alignSelf() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("align-self:auto align-self:flex-start");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .align-self\\:auto { align-self: auto }
          .align-self\\:flex-start { align-self: flex-start }
        }
        """
    );
  }

  @Test
  public void appearance() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("appearance:auto appearance:none");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .appearance\\:auto { appearance: auto }
          .appearance\\:none { appearance: none }
        }
        """
    );
  }

  @Test
  public void aspectRatio() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("aspect-ratio:auto aspect-ratio:2 aspect-ratio:16/9");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .aspect-ratio\\:auto { aspect-ratio: auto }
          .aspect-ratio\\:2 { aspect-ratio: 2 }
          .aspect-ratio\\:16\\/9 { aspect-ratio: 16/9 }
        }
        """
    );
  }

  @Test
  public void backgroundColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("background-color:currentColor");
        className("background-color:transparent");
        className("background-color:red-50");
        className("background-color:white");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .background-color\\:currentColor { background-color: currentColor }
          .background-color\\:transparent { background-color: transparent }
          .background-color\\:red-50 { background-color: var(--color-red-50) }
          .background-color\\:white { background-color: var(--color-white) }
        }
        """
    );
  }

  @Test
  public void border() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border:1px");
        className("border:2px_dashed");
        className("border:4px_solid_red-50");
        className("border-top:solid");
        className("border-top:dashed_red-500");
        className("border-top:1rem_solid");
        className("border-top:thick_double_#32a1ce");
        className("border-right:2px");
        className("border-bottom:2px_dotted");
        className("border-left:medium_dashed_green");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .border\\:1px { border: 1px }
          .border\\:2px_dashed { border: 2px dashed }
          .border\\:4px_solid_red-50 { border: 4px solid var(--color-red-50) }
          .border-top\\:solid { border-top: solid }
          .border-top\\:dashed_red-500 { border-top: dashed var(--color-red-500) }
          .border-top\\:1rem_solid { border-top: 1rem solid }
          .border-top\\:thick_double_#32a1ce { border-top: thick double #32a1ce }
          .border-right\\:2px { border-right: 2px }
          .border-bottom\\:2px_dotted { border-bottom: 2px dotted }
          .border-left\\:medium_dashed_green { border-left: medium dashed green }
        }
        """
    );
  }

  @Test
  public void borderCollapse() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border-collapse:collapse border-collapse:separate border-collapse:inherit");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .border-collapse\\:collapse { border-collapse: collapse }
          .border-collapse\\:separate { border-collapse: separate }
          .border-collapse\\:inherit { border-collapse: inherit }
        }
        """
    );
  }

  @Test
  public void borderRadius() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border-radius:10px");
        className("border-radius:10px_5%");
        className("border-radius:2px_4px_2px");
        className("border-radius:1px_0_3px_4px");
        className("border-radius:10px/20px");
        className("border-radius:10px_5%_/_20px_30px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .border-radius\\:10px { border-radius: 10px }
          .border-radius\\:10px_5\\% { border-radius: 10px 5% }
          .border-radius\\:2px_4px_2px { border-radius: 2px 4px 2px }
          .border-radius\\:1px_0_3px_4px { border-radius: 1px 0 3px 4px }
          .border-radius\\:10px\\/20px { border-radius: 10px/20px }
          .border-radius\\:10px_5\\%_\\/_20px_30px { border-radius: 10px 5% / 20px 30px }
        }
        """
    );
  }

  @Test
  public void borderSpacing() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("border-spacing:1px");
        className("border-spacing:1cm_2em");
        className("border-spacing:unset");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .border-spacing\\:1px { border-spacing: 1px }
          .border-spacing\\:1cm_2em { border-spacing: 1cm 2em }
          .border-spacing\\:unset { border-spacing: unset }
        }
        """
    );
  }

  @Test
  public void bottom() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("bottom:auto");
        className("bottom:0px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .bottom\\:auto { bottom: auto }
          .bottom\\:0px { bottom: 0px }
        }
        """
    );
  }

  @Test
  public void boxShadow() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("box-shadow:inset_0_2px_4px_0_rgb(0_0_0_/_0.05)");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .box-shadow\\:inset_0_2px_4px_0_rgb\\(0_0_0_\\/_0\\.05\\) { box-shadow: inset 0 2px 4px 0 rgb(0 0 0 / 0.05) }
        }
        """
    );
  }

  @Test
  public void clear() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("clear:left clear:right clear:both clear:none");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .clear\\:left { clear: left }
          .clear\\:right { clear: right }
          .clear\\:both { clear: both }
          .clear\\:none { clear: none }
        }
        """
    );
  }

  @Test
  public void columnGap() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("column-gap:0");
        className("column-gap:1rem");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .column-gap\\:0 { column-gap: 0 }
          .column-gap\\:1rem { column-gap: 1rem }
        }
        """
    );
  }

  @Test
  public void content() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("content:none");
        className("content:''");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .content\\:none { content: none }
          .content\\:'' { content: '' }
        }
        """
    );
  }

  @Test
  public void cursor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("cursor:auto cursor:pointer cursor:zoom-out");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .cursor\\:auto { cursor: auto }
          .cursor\\:pointer { cursor: pointer }
          .cursor\\:zoom-out { cursor: zoom-out }
        }
        """
    );
  }

  @Test
  public void display() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("display:block display:flex display:none");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .display\\:block { display: block }
          .display\\:flex { display: flex }
          .display\\:none { display: none }
        }
        """
    );
  }

  @Test
  public void fill() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("fill:none");
        className("fill:teal-400");
      }
    }

    test(
        Subject.class,

        """
      @layer utilities {
        .fill\\:none { fill: none }
        .fill\\:teal-400 { fill: var(--color-teal-400) }
      }
        """
    );
  }

  @Test
  public void flex() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("flex:none");
        className("flex:2_2_0%");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .flex\\:none { flex: none }
          .flex\\:2_2_0\\% { flex: 2 2 0% }
        }
        """
    );
  }

  @Test
  public void flexBasis() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("flex-basis:auto");
        className("flex-basis:50%");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .flex-basis\\:auto { flex-basis: auto }
          .flex-basis\\:50\\% { flex-basis: 50% }
        }
        """
    );
  }

  @Test
  public void flexDirection() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("flex-direction:row");
        className("flex-direction:row-reverse");
        className("flex-direction:column");
        className("flex-direction:column-reverse");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .flex-direction\\:row { flex-direction: row }
          .flex-direction\\:row-reverse { flex-direction: row-reverse }
          .flex-direction\\:column { flex-direction: column }
          .flex-direction\\:column-reverse { flex-direction: column-reverse }
        }
        """
    );
  }

  @Test
  public void flexGrow() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("flex-grow:1 flex-grow:0 flex-grow:2");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .flex-grow\\:1 { flex-grow: 1 }
          .flex-grow\\:0 { flex-grow: 0 }
          .flex-grow\\:2 { flex-grow: 2 }
        }
        """
    );
  }

  @Test
  public void flexShrink() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("flex-shrink:1 flex-shrink:0 flex-shrink:2");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .flex-shrink\\:1 { flex-shrink: 1 }
          .flex-shrink\\:0 { flex-shrink: 0 }
          .flex-shrink\\:2 { flex-shrink: 2 }
        }
        """
    );
  }

  @Test
  public void flexWrap() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("flex-wrap:wrap flex-wrap:wrap-reverse flex-wrap:nowrap");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .flex-wrap\\:wrap { flex-wrap: wrap }
          .flex-wrap\\:wrap-reverse { flex-wrap: wrap-reverse }
          .flex-wrap\\:nowrap { flex-wrap: nowrap }
        }
        """
    );
  }

  @Test
  public void floatTest() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("float:right float:left float:none");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .float\\:right { float: right }
          .float\\:left { float: left }
          .float\\:none { float: none }
        }
        """
    );
  }

  @Test
  public void fontSize() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("font-size:16px font-size:2rem");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .font-size\\:16px { font-size: 16px }
          .font-size\\:2rem { font-size: 2rem }
        }
        """
    );
  }

  @Test
  public void fontStyle() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("font-style:italic font-style:normal");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .font-style\\:italic { font-style: italic }
          .font-style\\:normal { font-style: normal }
        }
        """
    );
  }

  @Test
  public void fontWeight() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("font-weight:400 font-weight:normal");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .font-weight\\:400 { font-weight: 400 }
          .font-weight\\:normal { font-weight: normal }
        }
        """
    );
  }

  @Test
  public void gap() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("gap:0");
        className("gap:1rem");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .gap\\:0 { gap: 0 }
          .gap\\:1rem { gap: 1rem }
        }
        """
    );
  }

  @Test
  public void gridColumn() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("grid-column:auto");
        className("grid-column:span_1/span_1");
        className("grid-column:1/-1");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .grid-column\\:auto { grid-column: auto }
          .grid-column\\:span_1\\/span_1 { grid-column: span 1/span 1 }
          .grid-column\\:1\\/-1 { grid-column: 1/-1 }
        }
        """
    );
  }

  @Test
  public void gridColumnEnd() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("grid-column-end:auto");
        className("grid-column-end:4");
        className("grid-column-end:-1");
        className("grid-column-end:span_3");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .grid-column-end\\:auto { grid-column-end: auto }
          .grid-column-end\\:4 { grid-column-end: 4 }
          .grid-column-end\\:-1 { grid-column-end: -1 }
          .grid-column-end\\:span_3 { grid-column-end: span 3 }
        }
        """
    );
  }

  @Test
  public void gridColumnStart() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("grid-column-start:auto");
        className("grid-column-start:4");
        className("grid-column-start:-1");
        className("grid-column-start:span_4");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .grid-column-start\\:auto { grid-column-start: auto }
          .grid-column-start\\:4 { grid-column-start: 4 }
          .grid-column-start\\:-1 { grid-column-start: -1 }
          .grid-column-start\\:span_4 { grid-column-start: span 4 }
        }
        """
    );
  }

  @Test
  public void gridTemplate() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("grid-template:none");
        className("grid-template:'a_a_a'_'b_b_b'");
        className("grid-template:100px_1fr_/_50px_1fr");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .grid-template\\:none { grid-template: none }
          .grid-template\\:'a_a_a'_'b_b_b' { grid-template: 'a a a' 'b b b' }
          .grid-template\\:100px_1fr_\\/_50px_1fr { grid-template: 100px 1fr / 50px 1fr }
        }
        """
    );
  }

  @Test
  public void gridTemplateColumns() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("grid-template-columns:none");
        className("grid-template-columns:100px_1fr");
        className("grid-template-columns:minmax(100px,1fr)");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .grid-template-columns\\:none { grid-template-columns: none }
          .grid-template-columns\\:100px_1fr { grid-template-columns: 100px 1fr }
          .grid-template-columns\\:minmax\\(100px\\2c 1fr\\) { grid-template-columns: minmax(100px,1fr) }
        }
        """
    );
  }

  @Test
  public void gridTemplateRows() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("grid-template-rows:none");
        className("grid-template-rows:100px_1fr");
        className("grid-template-rows:minmax(100px,1fr)");
        className("grid-template-rows:repeat(3,200px)");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .grid-template-rows\\:none { grid-template-rows: none }
          .grid-template-rows\\:100px_1fr { grid-template-rows: 100px 1fr }
          .grid-template-rows\\:minmax\\(100px\\2c 1fr\\) { grid-template-rows: minmax(100px,1fr) }
          .grid-template-rows\\:repeat\\(3\\2c 200px\\) { grid-template-rows: repeat(3,200px) }
        }
        """
    );
  }

  @Test
  public void height() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("height:auto");
        className("height:50%");
        className("height:calc(100%/3)");
        className("height:32rx");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .height\\:auto { height: auto }
          .height\\:50\\% { height: 50% }
          .height\\:calc\\(100\\%\\/3\\) { height: calc(100%/3) }
          .height\\:32rx { height: calc(32px / var(--rx) * 1rem) }
        }
        """
    );
  }

  @Test
  public void inset() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("inset:10px");
        className("inset:4px_8px");
        className("inset:5px_15px_10px");
        className("inset:2.4em_3em_3em_3em");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .inset\\:10px { inset: 10px }
          .inset\\:4px_8px { inset: 4px 8px }
          .inset\\:5px_15px_10px { inset: 5px 15px 10px }
          .inset\\:2\\.4em_3em_3em_3em { inset: 2.4em 3em 3em 3em }
        }
        """
    );
  }

  @Test
  public void justifyContent() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("justify-content:normal justify-content:flex-start");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .justify-content\\:normal { justify-content: normal }
          .justify-content\\:flex-start { justify-content: flex-start }
        }
        """
    );
  }

  @Test
  public void justifyItems() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("justify-items:space-between justify-items:stretch");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .justify-items\\:space-between { justify-items: space-between }
          .justify-items\\:stretch { justify-items: stretch }
        }
        """
    );
  }

  @Test
  public void justifySelf() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("justify-self:normal justify-self:right");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .justify-self\\:normal { justify-self: normal }
          .justify-self\\:right { justify-self: right }
        }
        """
    );
  }

  @Test
  public void left() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("left:auto");
        className("left:10px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .left\\:auto { left: auto }
          .left\\:10px { left: 10px }
        }
        """
    );
  }

  @Test
  public void letterSpacing() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("letter-spacing:-0.05em");
        className("letter-spacing:0em");
        className("letter-spacing:0.1em");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .letter-spacing\\:-0\\.05em { letter-spacing: -0.05em }
          .letter-spacing\\:0em { letter-spacing: 0em }
          .letter-spacing\\:0\\.1em { letter-spacing: 0.1em }
        }
        """
    );
  }

  @Test
  public void lineHeight() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("line-height:1");
        className("line-height:150%");
        className("line-height:32px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .line-height\\:1 { line-height: 1 }
          .line-height\\:150\\% { line-height: 150% }
          .line-height\\:32px { line-height: 32px }
        }
        """
    );
  }

  @Test
  public void listStyleType() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("list-style-type:none list-style-type:square list-style-type:upper-roman");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .list-style-type\\:none { list-style-type: none }
          .list-style-type\\:square { list-style-type: square }
          .list-style-type\\:upper-roman { list-style-type: upper-roman }
        }
        """
    );
  }

  @Test
  public void margin() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("margin:auto");
        className("margin:0");
        className("margin:1em");
        className("margin:5%_0");
        className("margin:10px_50px_20px");
        className("margin:10px_50px_20px_auto");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .margin\\:auto { margin: auto }
          .margin\\:0 { margin: 0 }
          .margin\\:1em { margin: 1em }
          .margin\\:5\\%_0 { margin: 5% 0 }
          .margin\\:10px_50px_20px { margin: 10px 50px 20px }
          .margin\\:10px_50px_20px_auto { margin: 10px 50px 20px auto }
        }
        """
    );
  }

  @Test
  public void marginBlock() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("margin-block:auto");
        className("margin-block:0.25rem");
        className("margin-block:1em_2em");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .margin-block\\:auto { margin-block: auto }
          .margin-block\\:0\\.25rem { margin-block: 0.25rem }
          .margin-block\\:1em_2em { margin-block: 1em 2em }
        }
        """
    );
  }

  @Test
  public void marginBottom() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("margin-bottom:auto");
        className("margin-bottom:0.25rem");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .margin-bottom\\:auto { margin-bottom: auto }
          .margin-bottom\\:0\\.25rem { margin-bottom: 0.25rem }
        }
        """
    );
  }

  @Test
  public void marginInline() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("margin-inline:auto");
        className("margin-inline:0.25rem");
        className("margin-inline:1em_2em");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .margin-inline\\:auto { margin-inline: auto }
          .margin-inline\\:0\\.25rem { margin-inline: 0.25rem }
          .margin-inline\\:1em_2em { margin-inline: 1em 2em }
        }
        """
    );
  }

  @Test
  public void marginLeft() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("margin-left:auto");
        className("margin-left:0.25rem");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .margin-left\\:auto { margin-left: auto }
          .margin-left\\:0\\.25rem { margin-left: 0.25rem }
        }
        """
    );
  }

  @Test
  public void marginRight() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("margin-right:auto");
        className("margin-right:0.25rem");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .margin-right\\:auto { margin-right: auto }
          .margin-right\\:0\\.25rem { margin-right: 0.25rem }
        }
        """
    );
  }

  @Test
  public void marginTop() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("margin-top:auto");
        className("margin-top:0.25rem");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .margin-top\\:auto { margin-top: auto }
          .margin-top\\:0\\.25rem { margin-top: 0.25rem }
        }
        """
    );
  }

  @Test
  public void maxHeight() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("max-height:none");
        className("max-height:max-content");
        className("max-height:100%");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .max-height\\:none { max-height: none }
          .max-height\\:max-content { max-height: max-content }
          .max-height\\:100\\% { max-height: 100% }
        }
        """
    );
  }

  @Test
  public void maxWidth() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("max-width:none");
        className("max-width:1000px");
        className("max-width:var(--breakpoint-xl)");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .max-width\\:none { max-width: none }
          .max-width\\:1000px { max-width: 1000px }
          .max-width\\:var\\(--breakpoint-xl\\) { max-width: var(--breakpoint-xl) }
        }
        """
    );
  }

  @Test
  public void minHeight() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("min-height:100%");
        className("min-height:100vh");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .min-height\\:100\\% { min-height: 100% }
          .min-height\\:100vh { min-height: 100vh }
        }
        """
    );
  }

  @Test
  public void minWidth() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("min-width:100%");
        className("min-width:max-content");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .min-width\\:100\\% { min-width: 100% }
          .min-width\\:max-content { min-width: max-content }
        }
        """
    );
  }

  @Test
  public void opacity() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("opacity:0");
        className("opacity:0.33");
        className("opacity:90%");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .opacity\\:0 { opacity: 0 }
          .opacity\\:0\\.33 { opacity: 0.33 }
          .opacity\\:90\\% { opacity: 90% }
        }
        """
    );
  }

  @Test
  public void outline() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("outline:solid");
        className("outline:dashed_#f66");
        className("outline:thick_inset");
        className("outline:3px_solid_green");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .outline\\:solid { outline: solid }
          .outline\\:dashed_#f66 { outline: dashed #f66 }
          .outline\\:thick_inset { outline: thick inset }
          .outline\\:3px_solid_green { outline: 3px solid green }
        }
        """
    );
  }

  @Test
  public void outlineColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("outline-color:inherit");
        className("outline-color:orange-700");
        className("outline-color:rgb(30_222_121)");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .outline-color\\:inherit { outline-color: inherit }
          .outline-color\\:orange-700 { outline-color: var(--color-orange-700) }
          .outline-color\\:rgb\\(30_222_121\\) { outline-color: rgb(30 222 121) }
        }
        """
    );
  }

  @Test
  public void outlineOffset() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("outline-offset:0");
        className("outline-offset:3px");
        className("outline-offset:0.2em");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .outline-offset\\:0 { outline-offset: 0 }
          .outline-offset\\:3px { outline-offset: 3px }
          .outline-offset\\:0\\.2em { outline-offset: 0.2em }
        }
        """
    );
  }

  @Test
  public void outlineStyle() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("outline-style:none");
        className("outline-style:dashed");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .outline-style\\:none { outline-style: none }
          .outline-style\\:dashed { outline-style: dashed }
        }
        """
    );
  }

  @Test
  public void outlineWidth() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("outline-width:thin");
        className("outline-width:1px");
        className("outline-width:0.1em");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .outline-width\\:thin { outline-width: thin }
          .outline-width\\:1px { outline-width: 1px }
          .outline-width\\:0\\.1em { outline-width: 0.1em }
        }
        """
    );
  }

  @Test
  public void overflow() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("overflow:auto");
        className("overflow:hidden");
        className("overflow:scroll");
        className("overflow:hidden_visible");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .overflow\\:auto { overflow: auto }
          .overflow\\:hidden { overflow: hidden }
          .overflow\\:scroll { overflow: scroll }
          .overflow\\:hidden_visible { overflow: hidden visible }
        }
        """
    );
  }

  @Test
  public void overflowX() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("overflow-x:auto");
        className("overflow-x:hidden");
        className("overflow-x:scroll");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .overflow-x\\:auto { overflow-x: auto }
          .overflow-x\\:hidden { overflow-x: hidden }
          .overflow-x\\:scroll { overflow-x: scroll }
        }
        """
    );
  }

  @Test
  public void overflowY() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("overflow-y:auto");
        className("overflow-y:hidden");
        className("overflow-y:scroll");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .overflow-y\\:auto { overflow-y: auto }
          .overflow-y\\:hidden { overflow-y: hidden }
          .overflow-y\\:scroll { overflow-y: scroll }
        }
        """
    );
  }

  @Test
  public void padding() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("padding:1rem");
        className("padding:5%_10%");
        className("padding:1em_2em_2em");
        className("padding:5px_1em_0_2em");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .padding\\:1rem { padding: 1rem }
          .padding\\:5\\%_10\\% { padding: 5% 10% }
          .padding\\:1em_2em_2em { padding: 1em 2em 2em }
          .padding\\:5px_1em_0_2em { padding: 5px 1em 0 2em }
        }
        """
    );
  }

  @Test
  public void paddingBottom() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("padding-bottom:0");
        className("padding-bottom:20px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .padding-bottom\\:0 { padding-bottom: 0 }
          .padding-bottom\\:20px { padding-bottom: 20px }
        }
        """
    );
  }

  @Test
  public void paddingLeft() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("padding-left:0");
        className("padding-left:20px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .padding-left\\:0 { padding-left: 0 }
          .padding-left\\:20px { padding-left: 20px }
        }
        """
    );
  }

  @Test
  public void paddingRight() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("padding-right:0");
        className("padding-right:20px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .padding-right\\:0 { padding-right: 0 }
          .padding-right\\:20px { padding-right: 20px }
        }
        """
    );
  }

  @Test
  public void paddingTop() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("padding-top:0");
        className("padding-top:20px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .padding-top\\:0 { padding-top: 0 }
          .padding-top\\:20px { padding-top: 20px }
        }
        """
    );
  }

  @Test
  public void pointerEvents() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("pointer-events:none");
        className("pointer-events:auto");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .pointer-events\\:none { pointer-events: none }
          .pointer-events\\:auto { pointer-events: auto }
        }
        """
    );
  }

  @Test
  public void position() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("position:relative");
        className("position:absolute");
        className("position:sticky");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .position\\:relative { position: relative }
          .position\\:absolute { position: absolute }
          .position\\:sticky { position: sticky }
        }
        """
    );
  }

  @Test
  public void right() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("right:auto");
        className("right:10px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .right\\:auto { right: auto }
          .right\\:10px { right: 10px }
        }
        """
    );
  }

  @Test
  public void rotate() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("rotate:none");
        className("rotate:-45deg");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .rotate\\:none { rotate: none }
          .rotate\\:-45deg { rotate: -45deg }
        }
        """
    );
  }

  @Test
  public void rowGap() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("row-gap:0");
        className("row-gap:1rem");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .row-gap\\:0 { row-gap: 0 }
          .row-gap\\:1rem { row-gap: 1rem }
        }
        """
    );
  }

  @Test
  public void scrollBehavior() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("scroll-behavior:auto");
        className("scroll-behavior:smooth");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .scroll-behavior\\:auto { scroll-behavior: auto }
          .scroll-behavior\\:smooth { scroll-behavior: smooth }
        }
        """
    );
  }

  @Test
  public void stroke() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("stroke:none");
        className("stroke:teal-400");
        className("stroke:rgb(153_51_102_/_1);");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .stroke\\:none { stroke: none }
          .stroke\\:teal-400 { stroke: var(--color-teal-400) }
          .stroke\\:rgb\\(153_51_102_\\/_1\\); { stroke: rgb(153 51 102 / 1); }
        }
        """
    );
  }

  @Test
  public void strokeOpacity() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("stroke-opacity:1");
        className("stroke-opacity:0.3");
        className("stroke-opacity:50%");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .stroke-opacity\\:1 { stroke-opacity: 1 }
          .stroke-opacity\\:0\\.3 { stroke-opacity: 0.3 }
          .stroke-opacity\\:50\\% { stroke-opacity: 50% }
        }
        """
    );
  }

  @Test
  public void strokeWidth() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("stroke-width:0");
        className("stroke-width:1");
        className("stroke-width:14px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .stroke-width\\:0 { stroke-width: 0 }
          .stroke-width\\:1 { stroke-width: 1 }
          .stroke-width\\:14px { stroke-width: 14px }
        }
        """
    );
  }

  @Test
  public void tabSize() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("tab-size:10px");
        className("tab-size:4");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .tab-size\\:10px { tab-size: 10px }
          .tab-size\\:4 { tab-size: 4 }
        }
        """
    );
  }

  @Test
  public void tableLayout() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("table-layout:auto");
        className("table-layout:fixed");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .table-layout\\:auto { table-layout: auto }
          .table-layout\\:fixed { table-layout: fixed }
        }
        """
    );
  }

  @Test
  public void textAlign() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-align:start");
        className("text-align:center");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .text-align\\:start { text-align: start }
          .text-align\\:center { text-align: center }
        }
        """
    );
  }

  @Test
  public void textDecoration() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-decoration:underline");
        className("text-decoration:underline_overline");
        className("text-decoration:line-through_double_green");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .text-decoration\\:underline { text-decoration: underline }
          .text-decoration\\:underline_overline { text-decoration: underline overline }
          .text-decoration\\:line-through_double_green { text-decoration: line-through double green }
        }
        """
    );
  }

  @Test
  public void textDecorationColor() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-decoration-color:red");
        className("text-decoration-color:slate-100");
        className("text-decoration-color:#21ff21;");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .text-decoration-color\\:red { text-decoration-color: red }
          .text-decoration-color\\:slate-100 { text-decoration-color: var(--color-slate-100) }
          .text-decoration-color\\:#21ff21; { text-decoration-color: #21ff21; }
        }
        """
    );
  }

  @Test
  public void textDecorationLine() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-decoration-line:none");
        className("text-decoration-line:underline");
        className("text-decoration-line:overline");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .text-decoration-line\\:none { text-decoration-line: none }
          .text-decoration-line\\:underline { text-decoration-line: underline }
          .text-decoration-line\\:overline { text-decoration-line: overline }
        }
        """
    );
  }

  @Test
  public void textDecorationStyle() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-decoration-style:solid");
        className("text-decoration-style:double");
        className("text-decoration-style:wavy");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .text-decoration-style\\:solid { text-decoration-style: solid }
          .text-decoration-style\\:double { text-decoration-style: double }
          .text-decoration-style\\:wavy { text-decoration-style: wavy }
        }
        """
    );
  }

  @Test
  public void textDecorationThickness() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-decoration-thickness:from-font");
        className("text-decoration-thickness:0.1rem");
        className("text-decoration-thickness:3px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .text-decoration-thickness\\:from-font { text-decoration-thickness: from-font }
          .text-decoration-thickness\\:0\\.1rem { text-decoration-thickness: 0.1rem }
          .text-decoration-thickness\\:3px { text-decoration-thickness: 3px }
        }
        """
    );
  }

  @Test
  public void textOverflow() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-overflow:clip");
        className("text-overflow:ellipsis");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .text-overflow\\:clip { text-overflow: clip }
          .text-overflow\\:ellipsis { text-overflow: ellipsis }
        }
        """
    );
  }

  @Test
  public void textShadow() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-shadow:1px_1px_2px_blue-200");
        className("text-shadow:5px_10px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .text-shadow\\:1px_1px_2px_blue-200 { text-shadow: 1px 1px 2px var(--color-blue-200) }
          .text-shadow\\:5px_10px { text-shadow: 5px 10px }
        }
        """
    );
  }

  @Test
  public void textTransform() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-transform:capitalize");
        className("text-transform:uppercase");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .text-transform\\:capitalize { text-transform: capitalize }
          .text-transform\\:uppercase { text-transform: uppercase }
        }
        """
    );
  }

  @Test
  public void textWrap() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("text-wrap:wrap");
        className("text-wrap:nowrap");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .text-wrap\\:wrap { text-wrap: wrap }
          .text-wrap\\:nowrap { text-wrap: nowrap }
        }
        """
    );
  }

  @Test
  public void top() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("top:auto");
        className("top:0px");
        className("top:-2px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .top\\:auto { top: auto }
          .top\\:0px { top: 0px }
          .top\\:-2px { top: -2px }
        }
        """
    );
  }

  @Test
  public void transform() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("transform:rotate(0.5turn)");
        className("transform:translate(120px,50%)");
        className("transform:scale(2,_0.5)");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .transform\\:rotate\\(0\\.5turn\\) { transform: rotate(0.5turn) }
          .transform\\:translate\\(120px\\2c 50\\%\\) { transform: translate(120px,50%) }
          .transform\\:scale\\(2\\2c _0\\.5\\) { transform: scale(2, 0.5) }
        }
        """
    );
  }

  @Test
  public void transformOrigin() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("transform-origin:center");
        className("transform-origin:top_left");
        className("transform-origin:left_5px_-3px");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .transform-origin\\:center { transform-origin: center }
          .transform-origin\\:top_left { transform-origin: top left }
          .transform-origin\\:left_5px_-3px { transform-origin: left 5px -3px }
        }
        """
    );
  }

  @Test
  public void transition() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("transition:margin-right_2s");
        className("transition:all_1s_ease-out");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .transition\\:margin-right_2s { transition: margin-right 2s }
          .transition\\:all_1s_ease-out { transition: all 1s ease-out }
        }
        """
    );
  }

  @Test
  public void transitionDelay() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("transition-delay:250ms");
        className("transition-delay:1s,_250ms");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .transition-delay\\:250ms { transition-delay: 250ms }
          .transition-delay\\:1s\\2c _250ms { transition-delay: 1s, 250ms }
        }
        """
    );
  }

  @Test
  public void transitionDuration() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("transition-duration:123ms");
        className("transition-duration:1s,3s");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .transition-duration\\:123ms { transition-duration: 123ms }
          .transition-duration\\:1s\\2c 3s { transition-duration: 1s,3s }
        }
        """
    );
  }

  @Test
  public void transitionProperty() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("transition-property:all");
        className("transition-property:none");
        className("transition-property:height,color");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .transition-property\\:all { transition-property: all }
          .transition-property\\:none { transition-property: none }
          .transition-property\\:height\\2c color { transition-property: height,color }
        }
        """
    );
  }

  @Test
  public void transitionTimingFunction() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("transition-timing-function:linear");
        className("transition-timing-function:ease-in");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .transition-timing-function\\:linear { transition-timing-function: linear }
          .transition-timing-function\\:ease-in { transition-timing-function: ease-in }
        }
        """
    );
  }

  @Test
  public void translate() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("translate:none");
        className("translate:40px");
        className("translate:50%_-40%");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .translate\\:none { translate: none }
          .translate\\:40px { translate: 40px }
          .translate\\:50\\%_-40\\% { translate: 50% -40% }
        }
        """
    );
  }

  @Test
  public void userSelect() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("user-select:none");
        className("user-select:text");
        className("user-select:all");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .user-select\\:none { user-select: none }
          .user-select\\:text { user-select: text }
          .user-select\\:all { user-select: all }
        }
        """
    );
  }

  @Test
  public void verticalAlign() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("vertical-align:baseline");
        className("vertical-align:top");
        className("vertical-align:super");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .vertical-align\\:baseline { vertical-align: baseline }
          .vertical-align\\:top { vertical-align: top }
          .vertical-align\\:super { vertical-align: super }
        }
        """
    );
  }

  @Test
  public void visibility() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("visibility:visible");
        className("visibility:hidden");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .visibility\\:visible { visibility: visible }
          .visibility\\:hidden { visibility: hidden }
        }
        """
    );
  }

  @Test
  public void whiteSpace() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("white-space:normal");
        className("white-space:nowrap");
        className("white-space:pre");
        className("white-space:pre-line");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .white-space\\:normal { white-space: normal }
          .white-space\\:nowrap { white-space: nowrap }
          .white-space\\:pre { white-space: pre }
          .white-space\\:pre-line { white-space: pre-line }
        }
        """
    );
  }

  @Test
  public void width() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("width:auto");
        className("width:50%");
        className("width:fit-content");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .width\\:auto { width: auto }
          .width\\:50\\% { width: 50% }
          .width\\:fit-content { width: fit-content }
        }
        """
    );
  }

  @Test
  public void wordSpacing() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("word-spacing:normal");
        className("word-spacing:3px");
        className("word-spacing:0.3em");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .word-spacing\\:normal { word-spacing: normal }
          .word-spacing\\:3px { word-spacing: 3px }
          .word-spacing\\:0\\.3em { word-spacing: 0.3em }
        }
        """
    );
  }

  @Test
  public void zIndex() {
    class Subject extends AbstractSubject {
      @Override
      final void classes() {
        className("z-index:auto");
        className("z-index:0");
        className("z-index:289");
        className("z-index:-1");
      }
    }

    test(
        Subject.class,

        """
        @layer utilities {
          .z-index\\:auto { z-index: auto }
          .z-index\\:0 { z-index: 0 }
          .z-index\\:289 { z-index: 289 }
          .z-index\\:-1 { z-index: -1 }
        }
        """
    );
  }

  private void test(Class<?> type, String expected) {
    CssEngine config;
    config = new CssEngine();

    config.scanClass(type);

    config.execute();

    assertEquals(
        config.testUtilities(),

        expected
    );
  }

}