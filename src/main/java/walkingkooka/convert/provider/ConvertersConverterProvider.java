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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.Converters;
import walkingkooka.net.UrlPath;
import walkingkooka.plugin.ProviderContext;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link ConverterProvider} that sources all {@link Converter} from {@link Converters}.
 */
final class ConvertersConverterProvider implements ConverterProvider {

    /**
     * Singleton
     */
    final static ConvertersConverterProvider INSTANCE = new ConvertersConverterProvider();

    private ConvertersConverterProvider() {
        super();

        this.infos = ConverterInfoSet.with(
            Sets.readOnly(
                ConverterName.NAME_TO_FACTORY.keySet()
                    .stream()
                    .map(ConvertersConverterProvider::nameToConverterInfo)
                    .collect(Collectors.toCollection(SortedSets::tree))
            )
        );
    }

    private static ConverterInfo nameToConverterInfo(final ConverterName name) {
        return ConverterInfo.with(
            ConverterProviders.BASE_URL.appendPath(
                UrlPath.parse(
                    name.value()
                )
            ),
            name
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
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(values, "values");
        Objects.requireNonNull(context, "context");

        final Function<List<?>, Converter<?>> factory = ConverterName.NAME_TO_FACTORY.get(name);
        if (null == factory) {
            throw new IllegalArgumentException("Unknown converter " + name);
        }

        return Cast.to(
            factory.apply(
                Lists.immutable(values)
            )
        );
    }

    @Override
    public ConverterInfoSet converterInfos() {
        return this.infos;
    }

    private final ConverterInfoSet infos;

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
