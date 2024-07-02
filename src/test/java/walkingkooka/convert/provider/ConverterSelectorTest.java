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
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.InvalidCharacterException;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.Converters;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.HasTextTesting;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

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

    private final static ConverterName NAME2 = ConverterName.with("converter2");

    private final static ConverterName NAME3 = ConverterName.with("converter3");

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

    // converter........................................................................................................

    @Test
    public void testParseTextAndCreateFails() {
        final String text = NAME + " text/plain";

        final InvalidCharacterException thrown = assertThrows(
                InvalidCharacterException.class,
                () -> ConverterSelector.parse(text)
                        .parseTextAndCreate(ConverterProviders.fake())
        );

        this.checkEquals(
                new InvalidCharacterException(
                        text, text.indexOf(' ') + 1)
                        .getMessage(),
                thrown.getMessage()
        );
    }

    @Test
    public void testParseTextAndCreateNoText() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + "",
                (n, p) -> {
                    checkName(n, NAME);
                    checkParameters(p);

                    return expected;
                },
                expected
        );
    }

    @Test
    public void testParseTextAndCreateSpacesText() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + " ",
                (n, p) -> {
                    checkName(n, NAME);
                    checkParameters(p);

                    return expected;
                },
                expected
        );
    }

    @Test
    public void testParseTextAndCreateSpacesText2() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + "   ",
                (n, p) -> {
                    checkName(n, NAME);
                    checkParameters(p);

                    return expected;
                },
                expected
        );
    }

    @Test
    public void testParseTextAndCreateOpenParensFail() {
        this.parseTextAndCreateFails(
                NAME + " (",
                "Invalid character '(' at 25 in \"super-magic-converter123 (\""
        );
    }

    @Test
    public void testParseTextAndCreateDoubleLiteral() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + " (1)",
                (n, p) -> {
                    checkName(n, NAME);
                    checkParameters(p, 1.0);

                    return expected;
                },
                expected
        );
    }

    @Test
    public void testParseTextAndCreateNegativeDoubleLiteral() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + " (-1)",
                (n, p) -> {
                    checkName(n, NAME);
                    checkParameters(p, -1.0);

                    return expected;
                },
                expected
        );
    }

    @Test
    public void testParseTextAndCreateDoubleLiteralWithDecimals() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + " (1.25)",
                (n, p) -> {
                    checkName(n, NAME);
                    checkParameters(p, 1.25);

                    return expected;
                },
                expected
        );
    }

    @Test
    public void testParseTextAndCreateDoubleMissingClosingParensFail() {
        this.parseTextAndCreateFails(
                NAME + " (1",
                "Invalid character '1' at 26 in \"super-magic-converter123 (1\""
        );
    }

    @Test
    public void testParseTextAndCreateStringUnclosedFail() {
        this.parseTextAndCreateFails(
                NAME + " (\"unclosed",
                "Missing terminating '\"'"
        );
    }

    @Test
    public void testParseTextAndCreateEmptyParameterList() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + " ()",
                (n, p) -> {
                    checkName(n, NAME);
                    checkParameters(p);

                    return expected;
                },
                expected
        );
    }

    @Test
    public void testParseTextAndCreateEmptyParameterListWithExtraSpaces() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + "  ( )",
                (n, p) -> {
                    checkName(n, NAME);
                    checkParameters(p);

                    return expected;
                },
                expected
        );
    }

    @Test
    public void testParseTextAndCreateEmptyParameterListWithExtraSpaces2() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + "   (  )",
                (n, p) -> {
                    checkName(n, NAME);
                    checkParameters(p);

                    return expected;
                },
                expected
        );
    }

    @Test
    public void testParseTextAndCreateStringLiteral() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + " (\"string-literal-parameter\")",
                (n, p) -> {
                    checkName(n, NAME);
                    checkParameters(p, "string-literal-parameter");

                    return expected;
                },
                expected
        );
    }

    @Test
    public void testParseTextAndCreateStringLiteralStringLiteral() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + " (\"string-literal-parameter-1\",\"string-literal-parameter-2\")",
                (n, p) -> {
                    checkName(n, NAME);
                    checkParameters(p, "string-literal-parameter-1", "string-literal-parameter-2");

                    return expected;
                },
                expected
        );
    }

    @Test
    public void testParseTextAndCreateStringLiteralStringLiteralWithExtraSpaceIgnored() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + "  ( \"string-literal-parameter-1\" , \"string-literal-parameter-2\" )",
                (n, p) -> {
                    checkName(n, NAME);
                    checkParameters(p, "string-literal-parameter-1", "string-literal-parameter-2");

                    return expected;
                },
                expected
        );
    }

    @Test
    public void testParseTextAndCreateConverter() {
        final Converter<ConverterContext> expected1 = Converters.fake();
        final Converter<ConverterContext> expected2 = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + " (" + NAME2 + ")",
                (n, p) -> {
                    if (n.equals(NAME)) {
                        checkParameters(p, expected2);
                        return expected1;
                    }
                    if (n.equals(NAME2)) {
                        checkParameters(p);
                        return expected2;
                    }

                    throw new IllegalArgumentException("Unknown converter " + n);
                },
                expected1
        );
    }

    @Test
    public void testParseTextAndCreateConverterConverter() {
        final Converter<ConverterContext> expected1 = Converters.fake();
        final Converter<ConverterContext> expected2 = Converters.fake();
        final Converter<ConverterContext> expected3 = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + " (" + NAME2 + "," + NAME3 + ")",
                (n, p) -> {
                    if (n.equals(NAME)) {
                        checkParameters(p, expected2, expected3);
                        return expected1;
                    }
                    if (n.equals(NAME2)) {
                        checkParameters(p);
                        return expected2;
                    }
                    if (n.equals(NAME3)) {
                        checkParameters(p);
                        return expected3;
                    }

                    throw new IllegalArgumentException("Unknown converter " + n);
                },
                expected1
        );
    }

    @Test
    public void testParseTextAndCreateNestedConverter() {
        final Converter<ConverterContext> expected1 = Converters.fake();
        final Converter<ConverterContext> expected2 = Converters.fake();
        final Converter<ConverterContext> expected3 = Converters.fake();

        this.parseTextAndCreateAndCheck(
                NAME + " (" + NAME2 + "(" + NAME3 + "))",
                (n, p) -> {
                    if (n.equals(NAME)) {
                        checkParameters(p, expected2);
                        return expected1;
                    }
                    if (n.equals(NAME2)) {
                        checkParameters(p, expected3);
                        return expected2;
                    }
                    if (n.equals(NAME3)) {
                        checkParameters(p);
                        return expected3;
                    }

                    throw new IllegalArgumentException("Unknown converter " + n);
                },
                expected1
        );
    }

    private void parseTextAndCreateFails(final String selector,
                                         final String expected) {
        this.parseTextAndCreateFails(
                selector,
                ConverterProviders.fake(),
                expected
        );
    }

    private void parseTextAndCreateFails(final String selector,
                                         final ConverterProvider provider,
                                         final String expected) {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> ConverterSelector.parse(selector)
                        .parseTextAndCreate(provider)
        );
        this.checkEquals(
                expected,
                thrown.getMessage(),
                () -> "converter " + CharSequences.quoteAndEscape(selector)
        );
    }

    private void parseTextAndCreateAndCheck(final String selector,
                                            final BiFunction<ConverterName, List<?>, Converter<ConverterContext>> factory,
                                            final Converter<ConverterContext> expected) {
        this.parseTextAndCreateAndCheck(
                selector,
                new FakeConverterProvider() {
                    @Override
                    public <C extends ConverterContext> Optional<Converter<C>> converter(final ConverterName name,
                                                                                         final List<?> values) {
                        return Optional.of(
                                Cast.to(
                                        factory.apply(
                                                name,
                                                values
                                        )
                                )
                        );
                    }
                },
                expected
        );
    }

    private void parseTextAndCreateAndCheck(final String selector,
                                            final ConverterProvider provider,
                                            final Converter<ConverterContext> expected) {
        this.checkEquals(
                expected,
                ConverterSelector.parse(selector)
                        .parseTextAndCreate(provider)
        );
    }

    private void checkName(final ConverterName name,
                           final String expected) {
        this.checkName(
                name,
                ConverterName.with(expected)
        );
    }

    private void checkName(final ConverterName name,
                           final ConverterName expected) {
        this.checkEquals(
                expected,
                name,
                "name"
        );
    }

    private void checkParameters(final List<?> parameters,
                                 final Object... expected) {
        this.checkParameters(
                parameters,
                Lists.of(expected)
        );
    }

    private void checkParameters(final List<?> parameters,
                                 final List<?> expected) {
        this.checkEquals(
                expected,
                parameters,
                "parameters"
        );
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
