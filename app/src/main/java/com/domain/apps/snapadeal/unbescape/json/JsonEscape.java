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
package com.domain.apps.snapadeal.unbescape.json;

import java.io.IOException;
import java.io.Writer;


/**
 * <p>
 * Utility class for performing JSON escape/unescape operations.
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
 * needs of the scenario). Its values are defined by the {@link JsonEscapeLevel}
 * enum.</li>
 * <li><em>Type</em>, which defines whether escaping should be performed by means of SECs
 * (Single Escape Characters like <tt>&#92;n</tt>) or additionally by means of u-based
 * hexadecimal references (<tt>&#92;u00E1</tt>).
 * Its values are defined by the {@link JsonEscapeType} enum.</li>
 * </ul>
 * <p>
 * <strong>Unescape</strong> operations need no configuration parameters. Unescape operations
 * will always perform <em>complete</em> unescape of SECs (<tt>&#92;n</tt>)
 * and u-based (<tt>&#92;u00E1</tt>) hexadecimal escapes.
 * </p>
 *
 * <strong><u>Features</u></strong>
 *
 * <p>
 * Specific features of the JSON escape/unescape operations performed by means of this class:
 * </p>
 * <ul>
 * <li>The JSON basic escape set is supported. This <em>basic set</em> consists of:
 * <ul>
 * <li>The <em>Single Escape Characters</em>:
 * <tt>&#92;b</tt> (<tt>U+0008</tt>),
 * <tt>&#92;t</tt> (<tt>U+0009</tt>),
 * <tt>&#92;n</tt> (<tt>U+000A</tt>),
 * <tt>&#92;f</tt> (<tt>U+000C</tt>),
 * <tt>&#92;r</tt> (<tt>U+000D</tt>),
 * <tt>&#92;&quot;</tt> (<tt>U+0022</tt>),
 * <tt>&#92;&#92;</tt> (<tt>U+005C</tt>) and
 * <tt>&#92;&#47;</tt> (<tt>U+002F</tt>).
 * Note that <tt>&#92;&#47;</tt> is optional, and will only be used when the <tt>&#47;</tt>
 * symbol appears after <tt>&lt;</tt>, as in <tt>&lt;&#47;</tt>. This is to avoid accidentally
 * closing <tt>&lt;script&gt;</tt> tags in HTML.
 * </li>
 * <li>
 * Two ranges of non-displayable, control characters (some of which are already part of the
 * <em>single escape characters</em> list): <tt>U+0000</tt> to <tt>U+001F</tt> (required
 * by the JSON spec) and <tt>U+007F</tt> to <tt>U+009F</tt> (additional).
 * </li>
 * </ul>
 * </li>
 * <li>U-based hexadecimal escapes (a.k.a. <em>unicode escapes</em>) are supported both in escape
 * and unescape operations: <tt>&#92;u00E1</tt>.</li>
 * <li>Support for the whole Unicode character set: <tt>&#92;u0000</tt> to <tt>&#92;u10FFFF</tt>, including
 * characters not representable by only one <tt>char</tt> in Java (<tt>&gt;&#92;uFFFF</tt>).</li>
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
 * <dt>SEC</dt>
 * <dd>Single Escape Character:
 * <tt>&#92;b</tt> (<tt>U+0008</tt>),
 * <tt>&#92;t</tt> (<tt>U+0009</tt>),
 * <tt>&#92;n</tt> (<tt>U+000A</tt>),
 * <tt>&#92;f</tt> (<tt>U+000C</tt>),
 * <tt>&#92;r</tt> (<tt>U+000D</tt>),
 * <tt>&#92;&quot;</tt> (<tt>U+0022</tt>),
 * <tt>&#92;&#92;</tt> (<tt>U+005C</tt>) and
 * <tt>&#92;&#47;</tt> (<tt>U+002F</tt>) (optional, only in <tt>&lt;&#47;</tt>).
 * </dd>
 * <dt>UHEXA escapes</dt>
 * <dd>Also called <em>u-based hexadecimal escapes</em> or simply <em>unicode escapes</em>:
 * complete representation of unicode codepoints up to <tt>U+FFFF</tt>, with <tt>&#92;u</tt>
 * followed by exactly four hexadecimal figures: <tt>&#92;u00E1</tt>. Unicode codepoints &gt;
 * <tt>U+FFFF</tt> can be represented in JSON by mean of two UHEXA escapes (a
 * <em>surrogate pair</em>).</dd>
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
 * <li><a href="http://www.ietf.org/rfc/rfc4627.txt" target="_blank">RFC4627: The application/json
 * Media Type for JavaScript Object Notation (JSON)</a>
 * [ietf.org]</li>
 * <li><a href="http://timelessrepo.com/json-isnt-a-javascript-subset" target="_blank">JSON:
 * The JavaScript subset that isn't</a> [timelessrepo.com]</li>
 * <li><a href="http://benalman.com/news/2010/03/theres-no-such-thing-as-a-json/" target="_blank">There
 * is no such thing as a &quot;JSON Object&quot;</a> [benalman.com]</li>
 * </ul>
 *
 * @author Daniel Fern&aacute;ndez
 * @since 1.0.0
 */
public final class JsonEscape {


    private JsonEscape() {
        super();
    }

    /**
     * <p>
     * Perform a (configurable) JSON <strong>escape</strong> operation on a <tt>String</tt> input.
     * </p>
     * <p>
     * This method will perform an escape operation according to the specified
     * {@link JsonEscapeType} and
     * {@link JsonEscapeLevel} argument values.
     * </p>
     * <p>
     * All other <tt>String</tt>-based <tt>escapeJson*(...)</tt> methods call this one with preconfigured
     * <tt>type</tt> and <tt>level</tt> values.
     * </p>
     * <p>
     * This method is <strong>thread-safe</strong>.
     * </p>
     *
     * @param text  the <tt>String</tt> to be escaped.
     * @param type  the type of escape operation to be performed, see
     *              {@link JsonEscapeType}.
     * @param level the escape level to be applied, see {@link JsonEscapeLevel}.
     * @return The escaped result <tt>String</tt>. As a memory-performance improvement, will return the exact
     * same object as the <tt>text</tt> input argument if no escaping modifications were required (and
     * no additional <tt>String</tt> objects will be created during processing). Will
     * return <tt>null</tt> if <tt>text</tt> is <tt>null</tt>.
     */
    public static String escapeJson(final String text,
                                    final JsonEscapeType type, final JsonEscapeLevel level) {

        if (type == null) {
            throw new IllegalArgumentException("The 'type' argument cannot be null");
        }

        if (level == null) {
            throw new IllegalArgumentException("The 'level' argument cannot be null");
        }

        return JsonEscapeUtil.escape(text, type, level);

    }

    /**
     * <p>
     * Perform a (configurable) JSON <strong>escape</strong> operation on a <tt>char[]</tt> input.
     * </p>
     * <p>
     * This method will perform an escape operation according to the specified
     * {@link JsonEscapeType} and
     * {@link JsonEscapeLevel} argument values.
     * </p>
     * <p>
     * All other <tt>char[]</tt>-based <tt>escapeJson*(...)</tt> methods call this one with preconfigured
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
     * @param type   the type of escape operation to be performed, see
     *               {@link JsonEscapeType}.
     * @param level  the escape level to be applied, see {@link JsonEscapeLevel}.
     * @throws IOException if an input/output exception occurs
     */
    public static void escapeJson(final char[] text, final int offset, final int len, final Writer writer,
                                  final JsonEscapeType type, final JsonEscapeLevel level)
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

        JsonEscapeUtil.escape(text, offset, len, writer, type, level);

    }


}

