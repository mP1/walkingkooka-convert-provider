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

import java.util.List;
import java.util.Set;

public interface ConverterProviderDelegator extends ConverterProvider {
    @Override
    default <C extends ConverterContext> Converter<C> converter(final ConverterSelector selector) {
        return this.converterProvider()
                .converter(selector);
    }

    @Override
    default <C extends ConverterContext> Converter<C> converter(final ConverterName name,
                                                                final List<?> values) {
        return this.converterProvider()
                .converter(
                        name,
                        values
                );
    }

    @Override
    default Set<ConverterInfo> converterInfos() {
        return this.converterProvider()
                .converterInfos();
    }

    ConverterProvider converterProvider();
}
