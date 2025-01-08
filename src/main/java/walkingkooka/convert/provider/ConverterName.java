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

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;
import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginName;
import walkingkooka.plugin.PluginNameLike;
import walkingkooka.text.cursor.parser.Parser;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * The {@link Name} of a {@link Converter}. Note comparator names are case-sensitive.
 */
final public class ConverterName implements PluginNameLike<ConverterName> {

    public static boolean isChar(final int pos,
                                 final char c) {
        return PluginName.isChar(pos, c);
    }

    /**
     * The minimum valid length
     */
    public final static int MIN_LENGTH = 1;

    /**
     * The maximum valid length
     */
    public final static int MAX_LENGTH = PluginName.MAX_LENGTH;

    // constants........................................................................................................

    private static ConverterName registerConstantName(final String name) {
        return registerConstantName(
            name,
            (Function<List<?>, Converter<?>>) null
        );
    }

    private static ConverterName registerConstantName(final String name,
                                                      final Supplier<Converter<?>> converter) {
        return registerConstantName(
            name,
            (p) -> {
                if (false == p.isEmpty()) {
                    throw new IllegalArgumentException("Expected no parameters got " + p.size() + "=" + p);
                }

                return converter.get();
            }
        );
    }

    private static ConverterName registerConstantName(final String name,
                                                      final Function<List<?>, Converter<?>> factory) {
        final ConverterName converterName = new ConverterName(name);
        NAME_TO_FACTORY.put(
            converterName,
            factory
        );
        return converterName;
    }

    /**
     * Holds all constants in a {@link Set}.
     */
    final static Map<ConverterName, Function<List<?>, Converter<?>>> NAME_TO_FACTORY = Maps.sorted();


    private final static String BOOLEAN_TO_NUMBER_STRING = "boolean-to-number";

    /**
     * The name of the {@link Converter} returned by {@link Converters#booleanToNumber()}
     */
    public final static ConverterName BOOLEAN_TO_NUMBER = registerConstantName(
        BOOLEAN_TO_NUMBER_STRING,
        Converters::booleanToNumber
    );

    private final static String CHAIN_STRING = "chain";

    /**
     * The name of the {@link Converter} returned by {@link Converters#chain(Converter, Class, Converter)}.
     */
    public final static ConverterName CHAIN = registerConstantName(
        CHAIN_STRING
    );

    private final static String CHARACTER_OR_STRING_TO_STRING_STRING = "character-or-string-to-string";

    /**
     * The name of the {@link Converter} returned by {@link Converters#characterOrStringToString}
     */
    public final static ConverterName CHARACTER_OR_STRING_TO_STRING = registerConstantName(
        CHARACTER_OR_STRING_TO_STRING_STRING,
        Converters::characterOrStringToString
    );

    private final static String COLLECTION_STRING = "collection";

    /**
     * The name of the {@link Converter} returned by {@link Converters#collection(List)}
     */
    public final static ConverterName COLLECTION = registerConstantName(
        COLLECTION_STRING,
        (p) -> {
            for (final Object parameter : p) {
                if (false == parameter instanceof Converter) {
                    throw new IllegalArgumentException("Expected only Converter(s) got " + parameter);
                }
            }
            return Converters.collection(
                Cast.to(p)
            );
        }
    );

    private final static String CUSTOM_TO_STRING_STRING = "custom-to-string";

    /**
     * The name of the {@link Converter} returned by {@link Converters#customToString(Converter, String)}
     */
    public final static ConverterName CUSTOM_TO_STRING = registerConstantName(
        CUSTOM_TO_STRING_STRING
    );

    private final static String HAS_TEXT_TO_STRING_STRING = "has-text-to-string";

    /**
     * The name of the {@link Converter} returned by {@link Converters#hasTextToString()}
     */
    public final static ConverterName HAS_TEXT_TO_STRING = registerConstantName(
        HAS_TEXT_TO_STRING_STRING,
        Converters::hasTextToString
    );

    private final static String LOCAL_DATE_TIME_TO_LOCAL_DATE_STRING = "local-date-time-to-local-date";

    /**
     * The name of the {@link Converter} returned by {@link Converters#localDateTimeToLocalDate()}
     */
    public final static ConverterName LOCAL_DATE_TIME_TO_LOCAL_DATE = registerConstantName(
        LOCAL_DATE_TIME_TO_LOCAL_DATE_STRING,
        Converters::localDateTimeToLocalDate
    );

    private final static String LOCAL_DATE_TIME_TO_LOCAL_TIME_STRING = "local-date-time-to-local-time";

    /**
     * The name of the {@link Converter} returned by {@link Converters#localDateTimeToLocalTime()}
     */
    public final static ConverterName LOCAL_DATE_TIME_TO_LOCAL_TIME = registerConstantName(
        LOCAL_DATE_TIME_TO_LOCAL_TIME_STRING,
        Converters::localDateTimeToLocalTime
    );

    private final static String LOCAL_DATE_TIME_TO_NUMBER_STRING = "local-date-time-to-number";

    /**
     * The name of the {@link Converter} returned by {@link Converters#localDateTimeToNumber()}.
     */
    public final static ConverterName LOCAL_DATE_TIME_TO_NUMBER = registerConstantName(
        LOCAL_DATE_TIME_TO_NUMBER_STRING,
        Converters::localDateTimeToNumber
    );

    private final static String LOCAL_DATE_TIME_TO_STRING_STRING = "local-date-time-to-string";

    /**
     * The name of the {@link Converter} returned by {@link Converters#localDateTimeToString(Function)}
     */
    public final static ConverterName LOCAL_DATE_TIME_TO_STRING = registerConstantName(
        LOCAL_DATE_TIME_TO_STRING_STRING
    );

    private final static String LOCAL_DATE_TO_LOCAL_DATE_TIME_STRING = "local-date-to-local-date-time";

    /**
     * The name of the {@link Converter} returned by {@link Converters#localDateToLocalDateTime()}
     */
    public final static ConverterName LOCAL_DATE_TO_LOCAL_DATE_TIME = registerConstantName(
        LOCAL_DATE_TO_LOCAL_DATE_TIME_STRING,
        Converters::localDateToLocalDateTime
    );

    private final static String LOCAL_DATE_TO_NUMBER_STRING = "local-date-to-number";

    /**
     * The name of the {@link Converter} returned by {@link Converters#localDateToNumber()}
     */
    public final static ConverterName LOCAL_DATE_TO_NUMBER = registerConstantName(
        LOCAL_DATE_TO_NUMBER_STRING,
        Converters::localDateToNumber
    );

    private final static String LOCAL_DATE_TO_STRING_STRING = "local-date-to-string";

    /**
     * The name of the {@link Converter} returned by {@link Converters#localDateToString(Function)} ()}
     */
    public final static ConverterName LOCAL_DATE_TO_STRING = registerConstantName(
        LOCAL_DATE_TO_STRING_STRING
    );

    private final static String LOCAL_TIME_TO_LOCAL_DATE_TIME_STRING = "local-time-to-local-date-time";

    /**
     * The name of the {@link Converter} returned by {@link Converters#localTimeToLocalDateTime()}
     */
    public final static ConverterName LOCAL_TIME_TO_LOCAL_DATE_TIME = registerConstantName(
        LOCAL_TIME_TO_LOCAL_DATE_TIME_STRING,
        Converters::localTimeToLocalDateTime
    );

    private final static String LOCAL_TIME_TO_NUMBER_STRING = "local-time-to-number";

    /**
     * The name of the {@link Converter} returned by {@link Converters#localTimeToNumber}
     */
    public final static ConverterName LOCAL_TIME_TO_NUMBER = registerConstantName(
        LOCAL_TIME_TO_NUMBER_STRING,
        Converters::localTimeToNumber
    );

    private final static String LOCAL_TIME_TO_STRING_STRING = "local-time-to-string";

    /**
     * The name of the {@link Converter} returned by {@link Converters#localTimeToString(Function)} ()}
     */
    public final static ConverterName LOCAL_TIME_TO_STRING = registerConstantName(
        LOCAL_TIME_TO_STRING_STRING
    );

    private final static String MAPPER_STRING = "mapper";

    /**
     * The name of the {@link Converter} returned by {@link Converters#mapper(Predicate, Predicate, Function)}
     */
    public final static ConverterName MAPPER = registerConstantName(
        MAPPER_STRING
    );

    private final static String NEVER_STRING = "never";

    /**
     * The name of the {@link Converter} returned by {@link Converters#never()}
     */
    public final static ConverterName NEVER = registerConstantName(
        NEVER_STRING,
        Converters::never
    );

    private final static String NUMBER_TO_BOOLEAN_STRING = "number-to-boolean";

    /**
     * The name of the {@link Converter} returned by {@link Converters#numberToBoolean()}
     */
    public final static ConverterName NUMBER_TO_BOOLEAN = registerConstantName(
        NUMBER_TO_BOOLEAN_STRING,
        Converters::numberToBoolean
    );

    private final static String NUMBER_TO_LOCAL_DATE_STRING = "number-to-local-date";

    /**
     * The name of the {@link Converter} returned by {@link Converters#numberToLocalDate()}
     */
    public final static ConverterName NUMBER_TO_LOCAL_DATE = registerConstantName(
        NUMBER_TO_LOCAL_DATE_STRING,
        Converters::numberToLocalDate
    );

    private final static String NUMBER_TO_LOCAL_DATE_TIME_STRING = "number-to-local-date-time";

    /**
     * The name of the {@link Converter} returned by {@link Converters#numberToLocalDateTime()}
     */
    public final static ConverterName NUMBER_TO_LOCAL_DATE_TIME = registerConstantName(
        NUMBER_TO_LOCAL_DATE_TIME_STRING,
        Converters::numberToLocalDateTime
    );

    private final static String NUMBER_TO_LOCAL_TIME_STRING = "number-to-local-time";

    /**
     * The name of the {@link Converter} returned by {@link Converters#numberToLocalTime()}
     */
    public final static ConverterName NUMBER_TO_LOCAL_TIME = registerConstantName(
        NUMBER_TO_LOCAL_TIME_STRING,
        Converters::numberToLocalTime
    );

    private final static String NUMBER_TO_NUMBER_STRING = "number-to-number";

    /**
     * The name of the {@link Converter} returned by {@link Converters#numberToNumber()}
     */
    public final static ConverterName NUMBER_TO_NUMBER = registerConstantName(
        NUMBER_TO_NUMBER_STRING,
        Converters::numberToNumber
    );

    private final static String NUMBER_TO_STRING_STRING = "number-to-string";

    /**
     * The name of the {@link Converter} returned by {@link Converters#numberToString(Function)}.
     */
    public final static ConverterName NUMBER_TO_STRING = registerConstantName(
        NUMBER_TO_STRING_STRING
    );

    private final static String OBJECT_STRING = "object";

    /**
     * The name of the {@link Converter} returned by {@link Converters#object()}.
     */
    public final static ConverterName OBJECT = registerConstantName(
        OBJECT_STRING,
        Converters::object
    );

    private final static String OBJECT_TO_STRING_STRING = "object-to-string";

    /**
     * The name of the {@link Converter} returned by {@link Converters#objectToString()}.
     */
    public final static ConverterName OBJECT_TO_STRING = registerConstantName(
        OBJECT_TO_STRING_STRING,
        Converters::objectToString
    );

    private final static String PARSER_STRING = "parser";

    /**
     * The name of the {@link Converter} returned by {@link Converters#parser(Class, Parser, Function, BiFunction)}.
     */
    public final static ConverterName PARSER = registerConstantName(
        PARSER_STRING
    );

    private final static String SIMPLE_STRING = "simple";

    /**
     * The name of the {@link Converter} returned by {@link Converters#simple()}.
     */
    public final static ConverterName SIMPLE = registerConstantName(
        SIMPLE_STRING,
        Converters::simple
    );

    private final static String STRING_TO_CHARACTER_OR_STRING_STRING = "string-to-character-or-string";

    /**
     * The name of the {@link Converter} returned by {@link Converters#stringToCharacterOrString()}
     */
    public final static ConverterName STRING_TO_CHARACTER_OR_STRING = registerConstantName(
        STRING_TO_CHARACTER_OR_STRING_STRING,
        Converters::stringToCharacterOrString
    );

    private final static String STRING_TO_LOCAL_DATE_STRING = "string-to-local-date";

    /**
     * The name of the {@link Converter} returned by {@link Converters#stringToLocalDate(Function)}
     */
    public final static ConverterName STRING_TO_LOCAL_DATE = registerConstantName(
        STRING_TO_LOCAL_DATE_STRING
    );

    private final static String STRING_TO_LOCAL_DATE_TIME_STRING = "string-to-local-date-time";

    /**
     * The name of the {@link Converter} returned by {@link Converters#stringToLocalDateTime(Function)}
     */
    public final static ConverterName STRING_TO_LOCAL_DATE_TIME = registerConstantName(
        STRING_TO_LOCAL_DATE_TIME_STRING
    );

    private final static String STRING_TO_LOCAL_TIME_STRING = "string-to-local-time";

    /**
     * The name of the {@link Converter} returned by {@link Converters#stringToLocalTime(Function)}
     */
    public final static ConverterName STRING_TO_LOCAL_TIME = registerConstantName(
        STRING_TO_LOCAL_TIME_STRING
    );

    private final static String STRING_TO_NUMBER_STRING = "string-to-number";

    /**
     * The name of the {@link Converter} returned by {@link Converters#stringToNumber(Function)}
     */
    public final static ConverterName STRING_TO_NUMBER = registerConstantName(
        STRING_TO_NUMBER_STRING
    );

    private final static String TO_BOOLEAN_STRING = "to-boolean";

    /**
     * The name of the {@link Converter} returned by {@link Converters#toBoolean(Predicate, Predicate, Predicate, Object, Object)}.
     */
    public final static ConverterName TO_BOOLEAN = registerConstantName(
        TO_BOOLEAN_STRING
    );

    /**
     * Factory that creates a {@link ConverterName}
     */
    public static ConverterName with(final String name) {
        Objects.requireNonNull(name, "name");

        final ConverterName converterName;

        switch (name) {
            case BOOLEAN_TO_NUMBER_STRING:
                converterName = BOOLEAN_TO_NUMBER;
                break;
            case CHARACTER_OR_STRING_TO_STRING_STRING:
                converterName = CHARACTER_OR_STRING_TO_STRING;
                break;
            case CHAIN_STRING:
                converterName = CHAIN;
                break;
            case COLLECTION_STRING:
                converterName = COLLECTION;
                break;
            case CUSTOM_TO_STRING_STRING:
                converterName = CUSTOM_TO_STRING;
                break;
            case HAS_TEXT_TO_STRING_STRING:
                converterName = HAS_TEXT_TO_STRING;
                break;
            case LOCAL_DATE_TIME_TO_LOCAL_DATE_STRING:
                converterName = LOCAL_DATE_TIME_TO_LOCAL_DATE;
                break;
            case LOCAL_DATE_TIME_TO_LOCAL_TIME_STRING:
                converterName = LOCAL_DATE_TIME_TO_LOCAL_TIME;
                break;
            case LOCAL_DATE_TIME_TO_NUMBER_STRING:
                converterName = LOCAL_DATE_TIME_TO_NUMBER;
                break;
            case LOCAL_DATE_TIME_TO_STRING_STRING:
                converterName = LOCAL_DATE_TIME_TO_STRING;
                break;
            case LOCAL_DATE_TO_LOCAL_DATE_TIME_STRING:
                converterName = LOCAL_DATE_TO_LOCAL_DATE_TIME;
                break;
            case LOCAL_DATE_TO_NUMBER_STRING:
                converterName = LOCAL_DATE_TO_NUMBER;
                break;
            case LOCAL_DATE_TO_STRING_STRING:
                converterName = LOCAL_DATE_TO_STRING;
                break;
            case LOCAL_TIME_TO_LOCAL_DATE_TIME_STRING:
                converterName = LOCAL_TIME_TO_LOCAL_DATE_TIME;
                break;
            case LOCAL_TIME_TO_NUMBER_STRING:
                converterName = LOCAL_TIME_TO_NUMBER;
                break;
            case LOCAL_TIME_TO_STRING_STRING:
                converterName = LOCAL_TIME_TO_STRING;
                break;
            case MAPPER_STRING:
                converterName = MAPPER;
                break;
            case NEVER_STRING:
                converterName = NEVER;
                break;
            case NUMBER_TO_BOOLEAN_STRING:
                converterName = NUMBER_TO_BOOLEAN;
                break;
            case NUMBER_TO_LOCAL_DATE_STRING:
                converterName = NUMBER_TO_LOCAL_DATE;
                break;
            case NUMBER_TO_LOCAL_DATE_TIME_STRING:
                converterName = NUMBER_TO_LOCAL_DATE_TIME;
                break;
            case NUMBER_TO_LOCAL_TIME_STRING:
                converterName = NUMBER_TO_LOCAL_TIME;
                break;
            case NUMBER_TO_NUMBER_STRING:
                converterName = NUMBER_TO_NUMBER;
                break;
            case NUMBER_TO_STRING_STRING:
                converterName = NUMBER_TO_STRING;
                break;
            case OBJECT_STRING:
                converterName = OBJECT;
                break;
            case OBJECT_TO_STRING_STRING:
                converterName = OBJECT_TO_STRING;
                break;
            case PARSER_STRING:
                converterName = PARSER;
                break;
            case SIMPLE_STRING:
                converterName = SIMPLE;
                break;
            case STRING_TO_CHARACTER_OR_STRING_STRING:
                converterName = STRING_TO_CHARACTER_OR_STRING;
                break;
            case STRING_TO_LOCAL_DATE_STRING:
                converterName = STRING_TO_LOCAL_DATE;
                break;
            case STRING_TO_LOCAL_DATE_TIME_STRING:
                converterName = STRING_TO_LOCAL_DATE_TIME;
                break;
            case STRING_TO_LOCAL_TIME_STRING:
                converterName = STRING_TO_LOCAL_TIME;
                break;
            case STRING_TO_NUMBER_STRING:
                converterName = STRING_TO_NUMBER;
                break;
            case TO_BOOLEAN_STRING:
                converterName = TO_BOOLEAN;
                break;
            default:
                converterName = new ConverterName(name);
                break;
        }

        return converterName;
    }

    /**
     * Private constructor
     */
    private ConverterName(final String name) {
        super();
        this.name = PluginName.with(name);
    }

    @Override
    public String value() {
        return this.name.value();
    }

    private final PluginName name;

    // Object..................................................................................................

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof ConverterName &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final ConverterName other) {
        return this.compareTo(other) == 0;
    }

    @Override
    public String toString() {
        return this.name.toString();
    }

    // Json.............................................................................................................

    static ConverterName unmarshall(final JsonNode node,
                                    final JsonNodeUnmarshallContext context) {
        return with(node.stringOrFail());
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(this.toString());
    }

    static {
        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(ConverterName.class),
            ConverterName::unmarshall,
            ConverterName::marshall,
            ConverterName.class
        );
    }
}
