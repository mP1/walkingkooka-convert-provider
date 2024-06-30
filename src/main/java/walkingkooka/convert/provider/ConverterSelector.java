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

import walkingkooka.convert.Converter;
import walkingkooka.naming.HasName;
import walkingkooka.plugin.PluginSelector;
import walkingkooka.text.HasText;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Objects;
import java.util.Optional;

/**
 * Contains the {@link ConverterName} and some text which may contain an expression for a {@link Converter}.
 */
public final class ConverterSelector implements HasName<ConverterName>,
        HasText,
        TreePrintable {

    /**
     * Parses the given text into a {@link ConverterSelector}.
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