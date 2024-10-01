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
import walkingkooka.plugin.FilteredProviderGuard;
import walkingkooka.plugin.ProviderContext;

import java.util.List;
import java.util.Objects;

/**
 * A {@link ConverterProvider} that provides {@link Converter} from one provider but lists more {@link ConverterInfo}.
 */
final class FilteredConverterProvider implements ConverterProvider {

    static FilteredConverterProvider with(final ConverterProvider provider,
                                          final ConverterInfoSet infos) {
        return new FilteredConverterProvider(
                Objects.requireNonNull(provider, "provider"),
                Objects.requireNonNull(infos, "infos")
        );
    }

    private FilteredConverterProvider(final ConverterProvider provider,
                                      final ConverterInfoSet infos) {
        this.guard = FilteredProviderGuard.with(
                infos.names(),
                ConverterPluginHelper.INSTANCE
        );
        this.provider = provider;
        this.infos = infos;
    }

    @Override
    public <C extends ConverterContext> Converter<C> converter(final ConverterSelector selector,
                                                               final ProviderContext context) {
        return this.provider.converter(
                selector,
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

        return this.provider.converter(
                this.guard.name(name),
                values,
                context
        );
    }

    private final FilteredProviderGuard<ConverterName, ConverterSelector> guard;

    private final ConverterProvider provider;

    @Override
    public ConverterInfoSet converterInfos() {
        return this.infos;
    }

    private final ConverterInfoSet infos;

    @Override
    public String toString() {
        return this.provider.toString();
    }
}
