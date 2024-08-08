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
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.text.CharacterConstant;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * A {@link ConverterProvider} that wraps a view of new {@link ConverterName} to a wrapped {@link ConverterProvider}.
 */
final class MappedConverterProvider implements ConverterProvider {

    static MappedConverterProvider with(final Set<ConverterInfo> infos,
                                        final ConverterProvider provider) {
        Objects.requireNonNull(infos, "infos");
        Objects.requireNonNull(provider, "provider");

        return new MappedConverterProvider(
                infos,
                provider
        );
    }

    private MappedConverterProvider(final Set<ConverterInfo> infos,
                                    final ConverterProvider provider) {
        this.nameMapper = PluginInfoSetLike.nameMapper(
                infos,
                provider.converterInfos()
        );
        this.provider = provider;
        this.infos = PluginInfoSetLike.merge(
                infos,
                provider.converterInfos()
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
                this.nameMapper.apply(name)
                .orElseThrow(() -> new IllegalArgumentException("Unknown converter " + name)),
                values,
                context
        );
    }

    /**
     * A function that maps incoming {@link ConverterName} to the target provider after mapping them across using the {@link walkingkooka.net.AbsoluteUrl}.
     */
    private final Function<ConverterName, Optional<ConverterName>> nameMapper;

    /**
     * The original wrapped {@link ConverterProvider}.
     */
    private final ConverterProvider provider;

    @Override
    public Set<ConverterInfo> converterInfos() {
        return this.infos;
    }

    private final Set<ConverterInfo> infos;

    @Override
    public String toString() {
        return CharacterConstant.COMMA.toSeparatedString(
                this.infos,
                ConverterInfo::toString
        );
    }
}
