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

import walkingkooka.collect.set.Sets;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link ConverterProvider} that is empty without any {@link Converter}.
 */
final class EmptyConverterProvider implements ConverterProvider{

    final static EmptyConverterProvider INSTANCE = new EmptyConverterProvider();

    private EmptyConverterProvider() {
    }

    @Override
    public <C extends ConverterContext> Converter<C> converter(final ConverterName name,
                                                               final List<?> values) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(values, "values");

        throw new IllegalArgumentException("Unknown converter " + name);
    }

    @Override
    public Set<ConverterInfo> converterInfos() {
        return Sets.empty();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
