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

public interface ConverterProviderDelegator extends ConverterProvider {
    @Override
    default <C extends ConverterContext> Converter<C> converter(final ConverterSelector selector,
                                                                final ProviderContext context) {
        return this.converterProvider()
            .converter(
                selector,
                context
            );
    }

    @Override
    default <C extends ConverterContext> Converter<C> converter(final ConverterName name,
                                                                final List<?> values,
                                                                final ProviderContext context) {
        return this.converterProvider()
            .converter(
                name,
                values,
                context
            );
    }

    @Override
    default ConverterInfoSet converterInfos() {
        return this.converterProvider()
            .converterInfos();
    }

    ConverterProvider converterProvider();
}
