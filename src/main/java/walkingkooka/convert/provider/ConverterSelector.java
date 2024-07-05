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

import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.datetime.DateTimeContexts;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.naming.HasName;
import walkingkooka.plugin.PluginSelector;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.HasText;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorLineInfo;
import walkingkooka.text.cursor.TextCursors;
import walkingkooka.text.cursor.parser.DoubleParserToken;
import walkingkooka.text.cursor.parser.DoubleQuotedParserToken;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserContexts;
import walkingkooka.text.cursor.parser.ParserException;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.math.MathContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Contains the {@link ConverterName} and some text which may contain an expression for a {@link Converter}.
 */
public final class ConverterSelector implements HasName<ConverterName>,
        HasText,
        TreePrintable {

    /**
     * Parses the given text into a {@link ConverterSelector}. Note the text following the {@link ConverterName} is not validated in any form and simply stored.
     */
    public static ConverterSelector parse(final String text) {
        return new ConverterSelector(
                PluginSelector.parse(
                        text,
                        ConverterName::with
                )
        );
    }

    /**
     * Factory that creates a new {@link ConverterSelector}.
     */
    public static ConverterSelector with(final ConverterName name,
                                         final String text) {
        return new ConverterSelector(
                PluginSelector.with(
                        name,
                        text
                )
        );
    }

    private ConverterSelector(final PluginSelector<ConverterName> selector) {
        this.selector = selector;
    }

    // HasName..........................................................................................................

    @Override
    public ConverterName name() {
        return this.selector.name();
    }

    /**
     * Would be setter that returns a {@link ConverterSelector} with the given {@link ConverterName},
     * creating a new instance if necessary.
     */
    public ConverterSelector setName(final ConverterName name) {
        Objects.requireNonNull(name, "name");

        return this.name().equals(name) ?
                this :
                new ConverterSelector(
                        PluginSelector.with(
                                name,
                                this.text()
                        )
                );
    }

    // HasText..........................................................................................................

    /**
     * If the {@link ConverterName} identifies a {@link Converter}, this will
     * hold the pattern text itself.
     */
    @Override
    public String text() {
        return this.selector.text();
    }

    private final PluginSelector<ConverterName> selector;

    // parseTextAndCreate...............................................................................................

    /**
     * Parses the {@link #text()} as an expression that contains an optional parameter list which may include
     * <ul>
     * <li>{@link ConverterName}</li>
     * <li>double literals including negative or leading minus signs.</li>
     * <li>a double quoted string literal</li>
     * </ul>
     * Sample text.
     * <pre>
     * number-to-number
     * collection ( number-to-boolen, number-number, string-to-local-date "yyyy-mm-dd")
     * </pre>
     * The {@link ConverterProvider} will be used to fetch {@link Converter} with any parameters.
     */
    public <C extends ConverterContext> Converter<C> parseTextAndCreate(final ConverterProvider provider) {
        Objects.requireNonNull(provider, "provider");

        final TextCursor cursor = TextCursors.charSequence(this.text());

        final List<?> parameters = parseParameters(
                cursor,
                provider
        );

        skipSpaces(cursor);

        if (false == cursor.isEmpty()) {
            invalidCharacter(cursor);
        }

        return provider.converterOrFail(
                this.name(),
                parameters
        );
    }

    /**
     * Attempts to parse an optional {@link Converter}.
     */
    private <C extends ConverterContext> Optional<Converter<C>> parseConverterAndParameters(final TextCursor cursor,
                                                                                            final ConverterProvider provider) {
        Converter<C> converter;

        final Optional<ConverterName> maybeConverterName = CONVERTER_NAME_PARSER.parse(
                cursor,
                PARSER_CONTEXT
        ).map((ParserToken token) -> ConverterName.with(token.text()));

        if (maybeConverterName.isPresent()) {

            converter = provider.converterOrFail(
                    maybeConverterName.get(),
                    parseParameters(
                            cursor,
                            provider
                    )
            );
        } else {
            converter = null;
        }

        return Optional.ofNullable(converter);
    }

    /**
     * A parser that returns a {@link ConverterName}.
     */
    private final static Parser<ParserContext> CONVERTER_NAME_PARSER = Parsers.stringInitialAndPartCharPredicate(
            (c) -> ConverterName.isChar(0, c),
            (c) -> ConverterName.isChar(1, c),
            1,
            ConverterName.MAX_LENGTH
    );

    /**
     * Tries to parse a parameter list if an OPEN-PARENS is present.
     */
    private <C extends ConverterContext> List<Object> parseParameters(final TextCursor cursor,
                                                                      final ConverterProvider provider) {
        skipSpaces(cursor);

        final List<Object> parameters = Lists.array();

        if (tryMatch(PARAMETER_BEGIN, cursor)) {
            for (; ; ) {
                skipSpaces(cursor);

                // try a converter
                final Optional<Converter<C>> maybeConverter = parseConverterAndParameters(
                        cursor,
                        provider
                );
                if (maybeConverter.isPresent()) {
                    parameters.add(maybeConverter.get());
                    continue;
                }

                // try for a double literal
                final Optional<Double> maybeNumber = NUMBER_LITERAL.parse(
                        cursor,
                        PARSER_CONTEXT
                ).map(
                        t -> t.cast(DoubleParserToken.class).value()
                );
                if (maybeNumber.isPresent()) {
                    parameters.add(maybeNumber.get());
                    continue;
                }

                // try for a string literal
                try {
                    final Optional<String> maybeString = STRING_LITERAL.parse(
                            cursor,
                            PARSER_CONTEXT
                    ).map(
                            t -> t.cast(DoubleQuotedParserToken.class).value()
                    );
                    if (maybeString.isPresent()) {
                        parameters.add(maybeString.get());
                        continue;
                    }
                } catch (final ParserException cause) {
                    throw new IllegalArgumentException(cause.getMessage(), cause);
                }

                if (tryMatch(PARAMETER_SEPARATOR, cursor)) {
                    continue;
                }

                if (tryMatch(PARAMETER_END, cursor)) {
                    break;
                }

                // must be an invalid character complain!
                invalidCharacter(cursor);
            }
        }

        return Lists.immutable(parameters);
    }

    /**
     * Consumes any whitespace, don't really care how many or if any were skipped.
     */
    private static void skipSpaces(final TextCursor cursor) {
        SPACE.parse(cursor, PARSER_CONTEXT);
    }

    /**
     * Matches any whitespace.
     */
    private final static Parser<ParserContext> SPACE = Parsers.character(CharPredicates.whitespace())
            .repeating();

    /**
     * Returns true if the token represented by the given {@link Parser} was found.
     */
    private static boolean tryMatch(final Parser<ParserContext> parser,
                                    final TextCursor cursor) {
        return parser.parse(
                cursor,
                PARSER_CONTEXT
        ).isPresent();
    }

    /**
     * Matches a LEFT PARENS which marks the start of a converter parameters.
     */
    private final static Parser<ParserContext> PARAMETER_BEGIN = Parsers.string("(", CaseSensitivity.SENSITIVE);

    /**
     * Matches a COMMA which separates individual parameters.
     */
    private final static Parser<ParserContext> PARAMETER_SEPARATOR = Parsers.string(",", CaseSensitivity.SENSITIVE);


    /**
     * Matches a RIGHT PARENS which marks the end of a converter parameters.
     */
    private final static Parser<ParserContext> PARAMETER_END = Parsers.string(")", CaseSensitivity.SENSITIVE);

    /**
     * Number literal parameters are double literals using DOT as the decimal separator.
     */
    private final static Parser<ParserContext> NUMBER_LITERAL = Parsers.doubleParser();

    /**
     * String literal parameters must be double-quoted and support backslash escaping.
     */
    private final static Parser<ParserContext> STRING_LITERAL = Parsers.doubleQuoted();

    /**
     * Singleton which can be reused.
     */
    private final static ParserContext PARSER_CONTEXT = ParserContexts.basic(
            DateTimeContexts.fake(), // dates are not supported
            DecimalNumberContexts.american(MathContext.UNLIMITED) // only the decimal char is actually required.
    );

    private void invalidCharacter(final TextCursor cursor) {
        final TextCursorLineInfo lineInfo = cursor.lineInfo();
        final int pos = Math.max(
                lineInfo.textOffset() - 1,
                0
        );

        throw new InvalidCharacterException(
                lineInfo.text()
                        .toString(),
                pos
        ).setTextAndPosition(
                this.toString(),
                this.name().textLength() + pos + 1 // +1 or the SPACE following the name
        );
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.selector.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof ConverterSelector && this.equals0((ConverterSelector) other);
    }

    private boolean equals0(final ConverterSelector other) {
        return this.selector.equals(other.selector);
    }

    /**
     * Note it is intentional that the {@link #text()} is not quoted, to ensure {@link #parse(String)} and {@link #toString()}
     * are round-trippable.
     */
    @Override
    public String toString() {
        return this.selector.toString();
    }

    // JsonNodeContext..................................................................................................

    /**
     * Factory that creates a {@link ConverterSelector} from a {@link JsonNode}.
     */
    static ConverterSelector unmarshall(final JsonNode node,
                                        final JsonNodeUnmarshallContext context) {
        return parse(node.stringOrFail());
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(
                this.toString()
        );
    }

    static {
        JsonNodeContext.register(
                JsonNodeContext.computeTypeName(ConverterSelector.class),
                ConverterSelector::unmarshall,
                ConverterSelector::marshall,
                ConverterSelector.class
        );
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.selector.printTree(printer);
    }
}
