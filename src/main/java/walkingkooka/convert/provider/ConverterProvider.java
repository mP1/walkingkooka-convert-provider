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
import walkingkooka.plugin.Provider;
import walkingkooka.plugin.ProviderContext;

import java.util.List;

/**
 * A provider supports listing available {@link ConverterInfo} and fetching implementations using a
 * {@link ConverterName}.
 */
public interface ConverterProvider extends Provider {

    /**
     * Resolves the given {@link ConverterSelector} to a {@link Converter}.
     */
    <C extends ConverterContext> Converter<C> converter(final ConverterSelector selector,
                                                        final ProviderContext context);

    /**
     * Resolves the given {@link ConverterName} to a {@link Converter} with the given parameter values.
     */
    <C extends ConverterContext> Converter<C> converter(final ConverterName name,
                                                        final List<?> values,
                                                        final ProviderContext context);

    /**
     * Returns all available {@link ConverterInfo}
     */
    ConverterInfoSet converterInfos();
}
