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
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.plugin.ProviderCollection;
import walkingkooka.plugin.ProviderCollectionProviderGetter;
import walkingkooka.plugin.ProviderContext;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link ConverterProvider} view of a collection of {@link ConverterProvider providers}.
 */
final class ConverterProviderCollection implements ConverterProvider {

    static <C extends ConverterContext> ConverterProviderCollection with(final Set<ConverterProvider> providers) {
        return new ConverterProviderCollection(
            Objects.requireNonNull(providers, "providers")
        );
    }

    private ConverterProviderCollection(final Set<ConverterProvider> providers) {
        this.providers = ProviderCollection.with(
            new ProviderCollectionProviderGetter<>() {
                @Override
                public Converter<?> get(final ConverterProvider provider,
                                        final ConverterName name,
                                        final List<?> values,
                                        final ProviderContext context) {
                    return Cast.to(
                        provider.converter(
                            name,
                            values,
                            context
                        )
                    );
                }

                @Override
                public Converter<?> get(final ConverterProvider provider,
                                        final ConverterSelector selector,
                                        final ProviderContext context) {
                    throw new UnsupportedOperationException();
                }
            },
            ConverterProvider::converterInfos,
            Converter.class.getSimpleName(),
            providers
        );
    }

    @Override
    public <C extends ConverterContext> Converter<C> converter(final ConverterSelector selector,
                                                               final ProviderContext context) {
        Objects.requireNonNull(selector, "selector");

        return selector.evaluateValueText(
            this,
            context
        );
    }

    @Override
    public <C extends ConverterContext> Converter<C> converter(final ConverterName name,
                                                               final List<?> values,
                                                               final ProviderContext context) {
        return Cast.to(
            this.providers.get(
                name,
                values,
                context
            )
        );
    }

    @Override
    public ConverterInfoSet converterInfos() {
        return ConverterInfoSet.with(
            this.providers.infos()
        );
    }

    private final ProviderCollection<ConverterProvider, ConverterName, ConverterInfo, ConverterSelector, Converter<?>> providers;

    @Override
    public String toString() {
        return this.providers.toString();
    }
}
