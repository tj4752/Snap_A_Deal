/*
 * =============================================================================
 *
 *   Copyright (c) 2014, The UNBESCAPE team (http://www.unbescape.org)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 * =============================================================================
 */
package com.domain.apps.snapadeal.unbescape.html;

import java.io.IOException;
import java.io.Writer;

/**
 * <p>
 * Utility class for performing HTML escape/unescape operations.
 * </p>
 *
 * <strong><u>Configuration of escape/unescape operations</u></strong>
 *
 * <p>
 * <strong>Escape</strong> operations can be (optionally) configured by means of:
 * </p>
 * <ul>
 * <li><em>Level</em>, which defines how deep the escape operation must be (what
 * chars are to be considered eligible for escaping, depending on the specific
 * needs of the scenario). Its values are defined by the {@link org.unbescape.html.HtmlEscapeLevel}
 * enum.</li>
 * <li><em>Type</em>, which defines whether escaping should be performed by means of NCRs
 * (Named Character References), by means of decimal/hexadecimal numerical references,
 * using the HTML5 or the HTML 4 NCR set, etc. Its values are defined by the
 * {@link org.unbescape.html.HtmlEscapeType} enum.</li>
 * </ul>
 * <p>
 * <strong>Unescape</strong> operations need no configuration parameters. Unescape operations
 * will always perform <em>complete</em> unescape of NCRs (whole HTML5 set supported), decimal
 * and hexadecimal references.
 * </p>
 *
 * <strong><u>Features</u></strong>
 *
 * <p>
 * Specific features of the HTML escape/unescape operations performed by means of this class:
 * </p>
 * <ul>
 * <li>Whole HTML5 NCR (Named Character Reference) set supported, if required:
 * <tt>&amp;rsqb;</tt>,<tt>&amp;NewLine;</tt>, etc. (HTML 4 set available too).</li>
 * <li>Mixed named and numerical (decimal or hexa) character references supported.</li>
 * <li>Ability to default to numerical (decimal or hexa) references when an applicable NCR does not exist
 * (depending on the selected operation <em>level</em>).</li>
 * <li>Support for the whole Unicode character set: <tt>&#92;u0000</tt> to <tt>&#92;u10FFFF</tt>, including
 * characters not representable by only one <tt>char</tt> in Java (<tt>&gt;&#92;uFFFF</tt>).</li>
 * <li>Support for unescape of double-char NCRs in HTML5: <tt>'&amp;fjlig;'</tt> &rarr; <tt>'fj'</tt>.</li>
 * <li>Support for a set of HTML5 unescape <em>tweaks</em> included in the HTML5 specification:
 * <ul>
 * <li>Unescape of numerical character references not ending in semi-colon
 * (e.g. <tt>'&amp;#x23ac'</tt>).</li>
 * <li>Unescape of specific NCRs not ending in semi-colon (e.g. <tt>'&amp;aacute'</tt>).</li>
 * <li>Unescape of specific numerical character references wrongly specified by their Windows-1252
 * codepage code instead of the Unicode one (e.g. <tt>'&amp;#x80;'</tt> for '&euro;'
 * (<tt>'&amp;euro;'</tt>) instead of <tt>'&amp;#x20ac;'</tt>).</li>
 * </ul>
 * </li>
 * </ul>
 *
 * <strong><u>Input/Output</u></strong>
 *
 * <p>
 * There are two different input/output modes that can be used in escape/unescape operations:
 * </p>
 * <ul>
 * <li><em><tt>String</tt> input, <tt>String</tt> output</em>: Input is specified as a <tt>String</tt> object
 * and output is returned as another. In order to improve memory performance, all escape and unescape
 * operations <u>will return the exact same input object as output if no escape/unescape modifications
 * are required</u>.</li>
 * <li><em><tt>char[]</tt> input, <tt>java.io.Writer</tt> output</em>: Input will be read from a char array
 * (<tt>char[]</tt>) and output will be written into the specified <tt>java.io.Writer</tt>.
 * Two <tt>int</tt> arguments called <tt>offset</tt> and <tt>len</tt> will be
 * used for specifying the part of the <tt>char[]</tt> that should be escaped/unescaped. These methods
 * should be called with <tt>offset = 0</tt> and <tt>len = text.length</tt> in order to process
 * the whole <tt>char[]</tt>.</li>
 * </ul>
 *
 * <strong><u>Glossary</u></strong>
 *
 * <dl>
 * <dt>NCR</dt>
 * <dd>Named Character Reference or <em>Character Entity Reference</em>: textual
 * representation of an Unicode codepoint: <tt>&amp;aacute;</tt></dd>
 * <dt>DCR</dt>
 * <dd>Decimal Character Reference: base-10 numerical representation of an Unicode codepoint:
 * <tt>&amp;#225;</tt></dd>
 * <dt>HCR</dt>
 * <dd>Hexadecimal Character Reference: hexadecimal numerical representation of an Unicode codepoint:
 * <tt>&amp;#xE1;</tt></dd>
 * <dt>Unicode Codepoint</dt>
 * <dd>Each of the <tt>int</tt> values conforming the Unicode code space.
 * Normally corresponding to a Java <tt>char</tt> primitive value (codepoint &lt;= <tt>&#92;uFFFF</tt>),
 * but might be two <tt>char</tt>s for codepoints <tt>&#92;u10000</tt> to <tt>&#92;u10FFFF</tt> if the
 * first <tt>char</tt> is a high surrogate (<tt>&#92;uD800</tt> to <tt>&#92;uDBFF</tt>) and the
 * second is a low surrogate (<tt>&#92;uDC00</tt> to <tt>&#92;uDFFF</tt>).</dd>
 * </dl>
 *
 * <strong><u>References</u></strong>
 *
 * <p>
 * The following references apply:
 * </p>
 * <ul>
 * <li><a href="http://www.w3.org/International/questions/qa-escapes" target="_blank">Using character escapes in
 * markup and CSS</a> [w3.org]</li>
 * <li><a href="http://www.w3.org/TR/html4/sgml/entities.html" target="_blank">Named Character References (or
 * <em>Character entity references</em>) in HTML 4</a> [w3.org]</li>
 * <li><a href="http://www.w3.org/TR/html5/syntax.html#named-character-references" target="_blank">Named Character
 * References (or <em>Character entity references</em>) in HTML5</a> [w3.org]</li>
 * <li><a href="http://www.w3.org/TR/html51/syntax.html#named-character-references" target="_blank">Named Character
 * References (or <em>Character entity references</em>) in HTML 5.1</a> [w3.org]</li>
 * <li><a href="http://www.w3.org/TR/html5/syntax.html#consume-a-character-reference" target="_blank">How to consume a
 * character reference (HTML5 specification)</a> [w3.org]</li>
 * <li><a href="https://www.owasp.org/index.php/XSS_(Cross_Site_Scripting)_Prevention_Cheat_Sheet"
 * target="_blank">OWASP XSS (Cross Site Scripting) Prevention Cheat Sheet</a> [owasp.org]</li>
 * <li><a href="http://www.oracle.com/technetwork/articles/javase/supplementary-142654.html"
 * target="_blank">Supplementary characters in the Java Platform</a> [oracle.com]</li>
 * </ul>
 *
 * @author Daniel Fern&aacute;ndez
 * @since 1.0.0
 */
public final class HtmlEscape {


    private HtmlEscape() {
        super();
    }

    /**
     * <p>
     * Perform a (configurable) HTML <strong>escape</strong> operation on a <tt>String</tt> input.
     * </p>
     * <p>
     * This method will perform an escape operation according to the specified
     * {@link org.unbescape.html.HtmlEscapeType} and {@link org.unbescape.html.HtmlEscapeLevel}
     * argument values.
     * </p>
     * <p>
     * All other <tt>String</tt>-based <tt>escapeHtml*(...)</tt> methods call this one with preconfigured
     * <tt>type</tt> and <tt>level</tt> values.
     * </p>
     * <p>
     * This method is <strong>thread-safe</strong>.
     * </p>
     *
     * @param text  the <tt>String</tt> to be escaped.
     * @param type  the type of escape operation to be performed, see {@link org.unbescape.html.HtmlEscapeType}.
     * @param level the escape level to be applied, see {@link org.unbescape.html.HtmlEscapeLevel}.
     * @return The escaped result <tt>String</tt>. As a memory-performance improvement, will return the exact
     * same object as the <tt>text</tt> input argument if no escaping modifications were required (and
     * no additional <tt>String</tt> objects will be created during processing). Will
     * return <tt>null</tt> if <tt>text</tt> is <tt>null</tt>.
     */
    public static String escapeHtml(final String text, final HtmlEscapeType type, final HtmlEscapeLevel level) {

        if (type == null) {
            throw new IllegalArgumentException("The 'type' argument cannot be null");
        }

        if (level == null) {
            throw new IllegalArgumentException("The 'level' argument cannot be null");
        }

        return HtmlEscapeUtil.escape(text, type, level);

    }

    /**
     * <p>
     * Perform a (configurable) HTML <strong>escape</strong> operation on a <tt>char[]</tt> input.
     * </p>
     * <p>
     * This method will perform an escape operation according to the specified
     * {@link org.unbescape.html.HtmlEscapeType} and {@link org.unbescape.html.HtmlEscapeLevel}
     * argument values.
     * </p>
     * <p>
     * All other <tt>char[]</tt>-based <tt>escapeHtml*(...)</tt> methods call this one with preconfigured
     * <tt>type</tt> and <tt>level</tt> values.
     * </p>
     * <p>
     * This method is <strong>thread-safe</strong>.
     * </p>
     *
     * @param text   the <tt>char[]</tt> to be escaped.
     * @param offset the position in <tt>text</tt> at which the escape operation should start.
     * @param len    the number of characters in <tt>text</tt> that should be escaped.
     * @param writer the <tt>java.io.Writer</tt> to which the escaped result will be written. Nothing will
     *               be written at all to this writer if <tt>text</tt> is <tt>null</tt>.
     * @param type   the type of escape operation to be performed, see {@link org.unbescape.html.HtmlEscapeType}.
     * @param level  the escape level to be applied, see {@link org.unbescape.html.HtmlEscapeLevel}.
     * @throws IOException if an input/output exception occurs
     */
    public static void escapeHtml(final char[] text, final int offset, final int len, final Writer writer,
                                  final HtmlEscapeType type, final HtmlEscapeLevel level)
            throws IOException {

        if (writer == null) {
            throw new IllegalArgumentException("Argument 'writer' cannot be null");
        }

        if (type == null) {
            throw new IllegalArgumentException("The 'type' argument cannot be null");
        }

        if (level == null) {
            throw new IllegalArgumentException("The 'level' argument cannot be null");
        }

        final int textLen = (text == null ? 0 : text.length);

        if (offset < 0 || offset > textLen) {
            throw new IllegalArgumentException(
                    "Invalid (offset, len). offset=" + offset + ", len=" + len + ", text.length=" + textLen);
        }

        if (len < 0 || (offset + len) > textLen) {
            throw new IllegalArgumentException(
                    "Invalid (offset, len). offset=" + offset + ", len=" + len + ", text.length=" + textLen);
        }

        HtmlEscapeUtil.escape(text, offset, len, writer, type, level);

    }

    /**
     * <p>
     * Perform an HTML <strong>unescape</strong> operation on a <tt>String</tt> input.
     * </p>
     * <p>
     * No additional configuration arguments are required. Unescape operations
     * will always perform <em>complete</em> unescape of NCRs (whole HTML5 set supported), decimal
     * and hexadecimal references.
     * </p>
     * <p>
     * This method is <strong>thread-safe</strong>.
     * </p>
     *
     * @param text the <tt>String</tt> to be unescaped.
     * @return The unescaped result <tt>String</tt>. As a memory-performance improvement, will return the exact
     * same object as the <tt>text</tt> input argument if no unescaping modifications were required (and
     * no additional <tt>String</tt> objects will be created during processing). Will
     * return <tt>null</tt> if <tt>text</tt> is <tt>null</tt>.
     */
    public static String unescapeHtml(final String text) {
        return HtmlEscapeUtil.unescape(text);
    }


}

