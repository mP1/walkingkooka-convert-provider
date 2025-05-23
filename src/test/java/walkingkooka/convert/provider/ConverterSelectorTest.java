/*
 * Copyright 2024 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.Converters;
import walkingkooka.plugin.PluginSelectorEvaluateValueTextProvider;
import walkingkooka.plugin.PluginSelectorLikeTesting;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ConverterSelectorTest implements PluginSelectorLikeTesting<ConverterSelector, ConverterName> {

    private final static ConverterName NAME = ConverterName.with("super-magic-converter123");

    private final static ConverterName NAME2 = ConverterName.with("converter2");

    private final static ConverterName NAME3 = ConverterName.with("converter3");

    private final static String TEXT = "$0.00";

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Override
    public ConverterSelector createPluginSelectorLike(final ConverterName name,
                                                      final String text) {
        return ConverterSelector.with(
            name,
            text
        );
    }

    @Override
    public ConverterName createName(final String value) {
        return ConverterName.with(value);
    }

    // parse............................................................................................................

    @Override
    public ConverterSelector parseString(final String text) {
        return ConverterSelector.parse(text);
    }

    // EvaluateValueText................................................................................................

    @Test
    public void testEvaluateValueTextFails() {
        final String text = NAME + " text/plain";

        final InvalidCharacterException thrown = assertThrows(
            InvalidCharacterException.class,
            () -> ConverterSelector.parse(text)
                .evaluateValueText(
                    ConverterProviders.fake(),
                    CONTEXT
                )
        );

        this.checkEquals(
            new InvalidCharacterException(
                text,
                text.indexOf(' ')
            ).getMessage(),
            thrown.getMessage()
        );
    }

    @Test
    public void testEvaluateValueTextNoText() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + "",
            (n, p, x) -> {
                checkName(n, NAME);
                checkParameters(p);

                return expected;
            },
            expected
        );
    }

    @Test
    public void testEvaluateValueTextSpacesText() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + " ",
            (n, p, x) -> {
                checkName(n, NAME);
                checkParameters(p);

                return expected;
            },
            expected
        );
    }

    @Test
    public void testEvaluateValueTextSpacesText2() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + "   ",
            (n, p, x) -> {
                checkName(n, NAME);
                checkParameters(p);

                return expected;
            },
            expected
        );
    }

    @Test
    public void testEvaluateValueTextOpenParensFail() {
        this.evaluateValueTextFails(
            NAME + "(",
            "Invalid character '(' at 24"
        );
    }

    @Test
    public void testEvaluateValueTextDoubleLiteral() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + " (1)",
            (n, p, x) -> {
                checkName(n, NAME);
                checkParameters(p, 1.0);

                return expected;
            },
            expected
        );
    }

    @Test
    public void testEvaluateValueTextNegativeDoubleLiteral() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + " (-1)",
            (n, p, x) -> {
                checkName(n, NAME);
                checkParameters(p, -1.0);

                return expected;
            },
            expected
        );
    }

    @Test
    public void testEvaluateValueTextDoubleLiteralWithDecimals() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + " (1.25)",
            (n, p, x) -> {
                checkName(n, NAME);
                checkParameters(p, 1.25);

                return expected;
            },
            expected
        );
    }

    @Test
    public void testEvaluateValueTextDoubleMissingClosingParensFail() {
        this.checkEquals(
            25,
            NAME.textLength() + 1
        );

        this.evaluateValueTextFails(
            NAME + "(1",
            "Invalid character '1' at 25"
        );
    }

    @Test
    public void testEvaluateValueTextStringUnclosedFail() {
        this.evaluateValueTextFails(
            NAME + " (\"unclosed",
            "Missing closing '\"'"
        );
    }

    @Test
    public void testEvaluateValueTextEmptyParameterList() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + " ()",
            (n, p, x) -> {
                checkName(n, NAME);
                checkParameters(p);

                return expected;
            },
            expected
        );
    }

    @Test
    public void testEvaluateValueTextEmptyParameterListWithExtraSpaces() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + "  ( )",
            (n, p, x) -> {
                checkName(n, NAME);
                checkParameters(p);

                return expected;
            },
            expected
        );
    }

    @Test
    public void testEvaluateValueTextEmptyParameterListWithExtraSpaces2() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + "   (  )",
            (n, p, x) -> {
                checkName(n, NAME);
                checkParameters(p);

                return expected;
            },
            expected
        );
    }

    @Test
    public void testEvaluateValueTextStringLiteral() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + " (\"string-literal-parameter\")",
            (n, p, x) -> {
                checkName(n, NAME);
                checkParameters(p, "string-literal-parameter");

                return expected;
            },
            expected
        );
    }

    @Test
    public void testEvaluateValueTextStringLiteralStringLiteral() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + " (\"string-literal-parameter-1\",\"string-literal-parameter-2\")",
            (n, p, x) -> {
                checkName(n, NAME);
                checkParameters(p, "string-literal-parameter-1", "string-literal-parameter-2");

                return expected;
            },
            expected
        );
    }

    @Test
    public void testEvaluateValueTextStringLiteralStringLiteralWithExtraSpaceIgnored() {
        final Converter<ConverterContext> expected = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + "  ( \"string-literal-parameter-1\" , \"string-literal-parameter-2\" )",
            (n, p, x) -> {
                checkName(n, NAME);
                checkParameters(p, "string-literal-parameter-1", "string-literal-parameter-2");

                return expected;
            },
            expected
        );
    }

    @Test
    public void testEvaluateValueTextConverter() {
        final Converter<ConverterContext> expected1 = Converters.fake();
        final Converter<ConverterContext> expected2 = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + " (" + NAME2 + ")",
            (n, p, x) -> {
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
    public void testEvaluateValueTextConverterConverter() {
        final Converter<ConverterContext> expected1 = Converters.fake();
        final Converter<ConverterContext> expected2 = Converters.fake();
        final Converter<ConverterContext> expected3 = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + " (" + NAME2 + "," + NAME3 + ")",
            (n, p, x) -> {
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
    public void testEvaluateValueTextNestedConverter() {
        final Converter<ConverterContext> expected1 = Converters.fake();
        final Converter<ConverterContext> expected2 = Converters.fake();
        final Converter<ConverterContext> expected3 = Converters.fake();

        this.evaluateValueTextAndCheck(
            NAME + " (" + NAME2 + "(" + NAME3 + "))",
            (n, p, x) -> {
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

    private void evaluateValueTextFails(final String selector,
                                        final String expected) {
        this.evaluateValueTextFails(
            selector,
            CONTEXT,
            expected
        );
    }

    private void evaluateValueTextFails(final String selector,
                                        final ProviderContext context,
                                        final String expected) {
        this.evaluateValueTextFails(
            selector,
            ConverterProviders.fake(),
            context,
            expected
        );
    }

    private void evaluateValueTextFails(final String selector,
                                        final ConverterProvider provider,
                                        final ProviderContext context,
                                        final String expected) {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> ConverterSelector.parse(selector)
                .evaluateValueText(
                    provider,
                    context
                )
        );
        this.checkEquals(
            expected,
            thrown.getMessage(),
            () -> "converter " + CharSequences.quoteAndEscape(selector)
        );
    }

    private void evaluateValueTextAndCheck(final String selector,
                                           final PluginSelectorEvaluateValueTextProvider<ConverterName, Converter<ConverterContext>> factory,
                                           final Converter<ConverterContext> expected) {
        this.evaluateValueTextAndCheck(
            selector,
            new FakeConverterProvider() {
                @Override
                public <C extends ConverterContext> Converter<C> converter(final ConverterName name,
                                                                           final List<?> values,
                                                                           final ProviderContext context) {
                    return Cast.to(
                        factory.get(
                            name,
                            values,
                            context
                        )
                    );
                }
            },
            CONTEXT,
            expected
        );
    }

    private void evaluateValueTextAndCheck(final String selector,
                                           final ConverterProvider provider,
                                           final ProviderContext context,
                                           final Converter<ConverterContext> expected) {
        this.checkEquals(
            expected,
            ConverterSelector.parse(selector)
                .evaluateValueText(
                    provider,
                    context
                )
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
        return ConverterSelector.with(
            NAME,
            TEXT
        );
    }

    // type name testing................................................................................................

    @Override
    public String typeNamePrefix() {
        return Converter.class.getSimpleName();
    }
}
