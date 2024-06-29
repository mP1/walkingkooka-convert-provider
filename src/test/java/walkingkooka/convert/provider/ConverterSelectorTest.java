/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.convert.provider;

import org.junit.jupiter.api.Test;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.InvalidCharacterException;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.HasTextTesting;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ConverterSelectorTest implements ClassTesting2<ConverterSelector>,
        HashCodeEqualsDefinedTesting2<ConverterSelector>,
        HasTextTesting,
        ToStringTesting<ConverterSelector>,
        ParseStringTesting<ConverterSelector>,
        JsonNodeMarshallingTesting<ConverterSelector>,
        TreePrintableTesting {

    private final static ConverterName NAME = ConverterName.with("super-magic-converter123");

    private final static String TEXT = "$0.00";

    @Test
    public void testWithNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> ConverterSelector.with(
                        null,
                        TEXT
                )
        );
    }

    @Test
    public void testWithNullTextFails() {
        assertThrows(
                NullPointerException.class,
                () -> ConverterSelector.with(
                        NAME,
                        null
                )
        );
    }

    @Test
    public void testWith() {
        final ConverterSelector selector = ConverterSelector.with(
                NAME,
                TEXT
        );

        this.checkEquals(NAME, selector.name(), "name");
        this.textAndCheck(
                selector,
                TEXT
        );
    }

    // setName..........................................................................................................

    @Test
    public void testSetNameWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> ConverterSelector.with(
                        NAME,
                        TEXT
                ).setName(null)
        );
    }

    @Test
    public void testSetNameWithSame() {
        final ConverterSelector selector = ConverterSelector.with(
                NAME,
                TEXT
        );
        assertSame(
                selector,
                selector.setName(NAME)
        );
    }

    @Test
    public void testSetNameWithDifferent() {
        final ConverterSelector selector = ConverterSelector.with(
                NAME,
                TEXT
        );
        final ConverterName differentName = ConverterName.with("different");
        final ConverterSelector different = selector.setName(differentName);

        assertNotSame(
                different,
                selector
        );
        this.checkEquals(
                differentName,
                different.name(),
                "name"
        );
        this.textAndCheck(
                selector,
                TEXT
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseInvalidConverterNameFails() {
        this.parseStringFails(
                "A!34",
                new InvalidCharacterException("A!34", 1)
                        .appendToMessage(" in \"A!34\"")
        );
    }

    @Test
    public void testParseConverterName() {
        final String text = "super-magic-converter123";
        this.parseStringAndCheck(
                text,
                ConverterSelector.with(
                        ConverterName.with(text),
                        ""
                )
        );
    }

    @Test
    public void testParseConverterNameSpace() {
        final String text = "super-magic-converter123";
        this.parseStringAndCheck(
                text + " ",
                ConverterSelector.with(
                        ConverterName.with(text),
                        ""
                )
        );
    }

    @Test
    public void testParseConverterNameSpacePatternText() {
        final String name = "super-magic-converter123";
        final String patternText = "@@";

        this.parseStringAndCheck(
                name + " " + patternText,
                ConverterSelector.with(
                        ConverterName.with(name),
                        patternText
                )
        );
    }

    // ConverterSelector.parse must be able to parse all ConverterSelector.toString.

    @Test
    public void testParseToString() {
        final ConverterSelector selector = ConverterSelector.parse("super-magic-converter123");

        this.parseStringAndCheck(
                selector.toString(),
                selector
        );
    }

    @Override
    public ConverterSelector parseString(final String text) {
        return ConverterSelector.parse(text);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> type) {
        return type;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException thrown) {
        return thrown;
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentName() {
        this.checkNotEquals(
                ConverterSelector.with(
                        ConverterName.with("different"),
                        TEXT
                )
        );
    }

    @Test
    public void testEqualsDifferentText() {
        this.checkNotEquals(
                ConverterSelector.with(
                        NAME,
                        "different"
                )
        );
    }

    @Override
    public ConverterSelector createObject() {
        return ConverterSelector.with(
                NAME,
                TEXT
        );
    }

    // ToString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                ConverterSelector.with(
                        NAME,
                        TEXT
                ),
                "super-magic-converter123 $0.00"
        );
    }

    @Test
    public void testToStringWithQuotes() {
        this.toStringAndCheck(
                ConverterSelector.with(
                        NAME,
                        "\"Hello\""
                ),
                "super-magic-converter123 \"Hello\""
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ConverterSelector> type() {
        return ConverterSelector.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }

    // Json.............................................................................................................

    @Test
    public void testMarshall() {
        this.marshallAndCheck(
                this.createJsonNodeMarshallingValue(),
                "\"super-magic-converter123 $0.00\""
        );
    }

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(
                "\"super-magic-converter123 $0.00\"",
                this.createJsonNodeMarshallingValue()
        );
    }

    @Override
    public ConverterSelector unmarshall(final JsonNode json,
                                                final JsonNodeUnmarshallContext context) {
        return ConverterSelector.unmarshall(
                json,
                context
        );
    }

    @Override
    public ConverterSelector createJsonNodeMarshallingValue() {
        return this.createObject();
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrintWithoutText() {
        this.treePrintAndCheck(
                ConverterSelector.parse("abc123"),
                "abc123\n"
        );
    }

    @Test
    public void testTreePrintWithText() {
        this.treePrintAndCheck(
                ConverterSelector.parse("super-magic-converter123 $0.00"),
                "super-magic-converter123\n" +
                        "  \"$0.00\"\n"
        );
    }
}
