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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.plugin.ProviderCollection;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

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
                Function.identity(), // inputToName
                (p, n, v) -> Cast.to(
                        p.converter(n, v)
                ),
                ConverterProvider::converterInfos,
                Converter.class.getSimpleName(),
                providers
        );
    }

    @Override
    public <C extends ConverterContext> Optional<Converter<C>> converter(final ConverterName name,
                                                                         final List<?> values) {
        return Cast.to(
                this.providers.get(
                name,
                values
            )
        );
    }

    @Override
    public Set<ConverterInfo> converterInfos() {
        return this.providers.infos();
    }

    private final ProviderCollection<ConverterName, ConverterInfo, ConverterProvider,
            ConverterName,
            Converter<?>> providers;

    @Override
    public String toString() {
        return this.providers.toString();
    }
}