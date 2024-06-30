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
import walkingkooka.collect.set.Sets;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.Converters;
import walkingkooka.net.UrlPath;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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

        this.infos = Sets.readOnly(
                ConverterName.NAME_TO_FACTORY.keySet()
                        .stream()
                        .map(ConvertersConverterProvider::nameToConverterInfo)
                        .collect(Collectors.toCollection(Sets::sorted))
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
    public <C extends ConverterContext> Optional<Converter<C>> converter(final ConverterSelector selector) {
        Objects.requireNonNull(selector, "selector");

        Converter<C> converter = null;

        // first verify the ConverterSelection#name exists...
        if (ConverterName.NAME_TO_FACTORY.containsKey(selector.name())) {

            // try and parseText.
            converter = selector.parseTextAndCreate(
                    (n, p) -> {
                        final Function<List<?>, Converter<?>> creator = ConverterName.NAME_TO_FACTORY.get(n);
                        if (null == creator) {
                            throw new IllegalArgumentException("Unknown converter " + CharSequences.quoteAndEscape(n.value()));
                        }
                        return Cast.to(
                                creator.apply(p)
                        );
                    }
            );
        }

        return Optional.ofNullable(
                converter
        );
    }

    @Override
    public Set<ConverterInfo> converterInfos() {
        return this.infos;
    }

    private final Set<ConverterInfo> infos;

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
