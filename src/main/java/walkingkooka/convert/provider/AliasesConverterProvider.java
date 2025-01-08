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

import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.plugin.ProviderContext;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link ConverterProvider} that uses the given aliases definition and {@link ConverterProvider} to present another view.
 */
final class AliasesConverterProvider implements ConverterProvider {

    static AliasesConverterProvider with(final ConverterAliasSet aliases,
                                         final ConverterProvider provider) {
        return new AliasesConverterProvider(
            Objects.requireNonNull(aliases, "aliases"),
            Objects.requireNonNull(provider, "provider")
        );
    }

    private AliasesConverterProvider(final ConverterAliasSet aliases,
                                     final ConverterProvider provider) {
        this.aliases = aliases;
        this.provider = provider;

        this.infos = aliases.merge(provider.converterInfos());
    }

    @Override
    public <C extends ConverterContext> Converter<C> converter(final ConverterSelector selector,
                                                               final ProviderContext context) {
        return this.provider.converter(
            this.aliases.selector(selector),
            context
        );
    }

    @Override
    public <C extends ConverterContext> Converter<C> converter(final ConverterName name,
                                                               final List<?> values,
                                                               final ProviderContext context) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(context, "context");

        Converter<C> converter;

        final ConverterAliasSet aliases = this.aliases;
        final ConverterProvider provider = this.provider;

        final Optional<ConverterSelector> selector = aliases.aliasSelector(name);
        if (selector.isPresent()) {
            if (false == values.isEmpty()) {
                throw new IllegalArgumentException("Alias " + name + " should have no values");
            }
            // assumes that $provider caches selectors to converter
            converter = provider.converter(
                selector.get(),
                context
            );
        } else {
            converter = provider.converter(
                aliases.aliasOrName(name)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown Converter " + name)),
                values,
                context
            );
        }

        return converter;
    }

    private final ConverterAliasSet aliases;

    private final ConverterProvider provider;

    @Override
    public ConverterInfoSet converterInfos() {
        return this.infos;
    }

    private final ConverterInfoSet infos;

    @Override
    public String toString() {
        return this.converterInfos().toString();
    }
}
