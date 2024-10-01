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
import walkingkooka.plugin.FilteredProviderMapper;
import walkingkooka.plugin.ProviderContext;

import java.util.List;
import java.util.Objects;

/**
 * A {@link ConverterProvider} that wraps a view of new {@link ConverterName} to a wrapped {@link ConverterProvider}.
 */
final class FilteredMappedConverterProvider implements ConverterProvider {

    static FilteredMappedConverterProvider with(final ConverterInfoSet infos,
                                                final ConverterProvider provider) {
        Objects.requireNonNull(infos, "infos");
        Objects.requireNonNull(provider, "provider");

        return new FilteredMappedConverterProvider(
                infos,
                provider
        );
    }

    private FilteredMappedConverterProvider(final ConverterInfoSet infos,
                                            final ConverterProvider provider) {
        this.provider = provider;
        this.mapper = FilteredProviderMapper.with(
                infos,
                provider.converterInfos(),
                ConverterPluginHelper.INSTANCE
        );
    }

    @Override
    public <C extends ConverterContext> Converter<C> converter(final ConverterSelector selector,
                                                               final ProviderContext context) {
        Objects.requireNonNull(selector, "selector");

        return selector.evaluateText(
                this,
                context
        );
    }

    @Override
    public <C extends ConverterContext> Converter<C> converter(final ConverterName name,
                                                               final List<?> values,
                                                               final ProviderContext context) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(values, "values");

        return this.provider.converter(
                this.mapper.name(name),
                values,
                context
        );
    }

    /**
     * The original wrapped {@link ConverterProvider}.
     */
    private final ConverterProvider provider;

    @Override
    public ConverterInfoSet converterInfos() {
        return this.mapper.infos();
    }

    private final FilteredProviderMapper<ConverterName, ConverterSelector, ConverterInfo, ConverterInfoSet> mapper;

    @Override
    public String toString() {
        return this.mapper.toString();
    }
}
