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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.convert.Converter;
import walkingkooka.plugin.ProviderTesting;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ConverterProviderTesting<T extends ConverterProvider> extends ProviderTesting<T> {

    @Test
    default void testConverterWithNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createConverterProvider()
                        .converter(
                                null,
                                Lists.empty()
                        )
        );
    }

    @Test
    default void testConverterWithNullValueFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createConverterProvider()
                        .converter(
                                ConverterName.BOOLEAN_TO_NUMBER,
                                null
                        )
        );
    }

    default void converterAndCheck(final String selector,
                                   final Converter<?> expected) {
        this.converterAndCheck(
                ConverterSelector.parse(selector),
                expected
        );
    }

    default void converterAndCheck(final ConverterSelector selector,
                                   final Converter<?> expected) {
        this.converterAndCheck(
                this.createConverterProvider(),
                selector,
                expected
        );
    }

    default void converterAndCheck(final ConverterProvider provider,
                                   final String selector,
                                   final Converter<?> expected) {
        this.converterAndCheck(
                provider,
                ConverterSelector.parse(selector),
                expected
        );
    }

    default void converterAndCheck(final ConverterProvider provider,
                                   final ConverterSelector selector,
                                   final Converter<?> expected) {
        this.checkEquals(
                expected,
                selector.parseTextAndCreate(provider)
        );
    }

    default void converterAndCheck(final ConverterName name,
                                   final List<?> values) {
        this.converterAndCheck(
                name,
                values,
                Optional.empty()
        );
    }

    default void converterAndCheck(final ConverterName name,
                                   final List<?> values,
                                   final Converter<?> expected) {
        this.converterAndCheck(
                name,
                values,
                Optional.of(expected)
        );
    }

    default void converterAndCheck(final ConverterName name,
                                   final List<?> values,
                                   final Optional<Converter<?>> expected) {
        this.converterAndCheck(
                this.createConverterProvider(),
                name,
                values,
                expected
        );
    }

    default void converterAndCheck(final ConverterProvider provider,
                                   final ConverterName name,
                                   final List<?> values,
                                   final Converter<?> expected) {
        this.converterAndCheck(
                provider,
                name,
                values,
                Optional.of(expected)
        );
    }

    default void converterAndCheck(final ConverterProvider provider,
                                   final ConverterName name,
                                   final List<?> values,
                                   final Optional<Converter<?>> expected) {
        this.checkEquals(
                expected,
                provider.converter(
                        name,
                        values
                ),
                () -> provider + " " + name + " " + values
        );
    }

    default void converterInfosAndCheck(final ConverterInfo... expected) {
        this.converterInfosAndCheck(
                this.createConverterProvider(),
                expected
        );
    }

    default void converterInfosAndCheck(final ConverterProvider provider,
                                        final ConverterInfo... expected) {
        this.converterInfosAndCheck(
                provider,
                Sets.of(
                        expected
                )
        );
    }

    default void converterInfosAndCheck(final Set<ConverterInfo> expected) {
        this.converterInfosAndCheck(
                this.createConverterProvider(),
                expected
        );
    }

    default void converterInfosAndCheck(final ConverterProvider provider,
                                        final Set<ConverterInfo> expected) {
        this.checkEquals(
                expected,
                provider.converterInfos(),
                () -> provider.toString()
        );
    }

    T createConverterProvider();
}
