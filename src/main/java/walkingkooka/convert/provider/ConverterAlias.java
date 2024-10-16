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
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.plugin.PluginAlias;
import walkingkooka.plugin.PluginAliasLike;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Optional;

public final class ConverterAlias implements PluginAliasLike<ConverterName, ConverterSelector, ConverterAlias> {

    public static ConverterAlias with(final ConverterName name,
                                      final Optional<ConverterSelector> selector,
                                      final Optional<AbsoluteUrl> url) {
        return new ConverterAlias(
                PluginAlias.with(
                        name,
                        selector,
                        url
                )
        );
    }

    private ConverterAlias(final PluginAlias<ConverterName, ConverterSelector> pluginAlias) {
        this.pluginAlias = pluginAlias;
    }

    // PluginAliasLike..................................................................................................

    @Override
    public ConverterName name() {
        return this.pluginAlias.name();
    }

    @Override
    public Optional<ConverterSelector> selector() {
        return this.pluginAlias.selector();
    }

    @Override
    public Optional<AbsoluteUrl> url() {
        return this.pluginAlias.url();
    }

    @Override
    public String text() {
        return this.pluginAlias.text();
    }

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final ConverterAlias other) {
        return this.pluginAlias.compareTo(other.pluginAlias);
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.pluginAlias.printTree(printer);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.pluginAlias.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof ConverterAlias && this.equals0(Cast.to(other));
    }

    private boolean equals0(final ConverterAlias other) {
        return this.pluginAlias.equals(other.pluginAlias);
    }

    @Override
    public String toString() {
        return this.pluginAlias.text();
    }

    private final PluginAlias<ConverterName, ConverterSelector> pluginAlias;
}
