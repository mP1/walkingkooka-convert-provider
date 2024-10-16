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
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.plugin.ProviderTesting;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ConverterProviderTesting<T extends ConverterProvider> extends ProviderTesting<T> {


    // converter(ConverterSelector)................................................................................

    @Test
    default void testConverterWithNullSelectorFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createConverterProvider()
                        .converter(
                                null,
                                ProviderContexts.fake()
                        )
        );
    }

    @Test
    default void testConverterWithNullContextFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createConverterProvider()
                        .converter(
                                ConverterSelector.parse("converter"),
                                null
                        )
        );
    }

    default void converterFails(final String selector,
                                final ProviderContext context) {
        this.converterFails(
                ConverterSelector.parse(selector),
                context
        );
    }

    default void converterFails(final ConverterSelector selector,
                                final ProviderContext context) {
        this.converterFails(
                this.createConverterProvider(),
                selector,
                context
        );
    }

    default void converterFails(final ConverterProvider provider,
                                final ConverterSelector selector,
                                final ProviderContext context) {
        assertThrows(
                IllegalArgumentException.class,
                () -> selector.evaluateText(
                        provider,
                        context
                )
        );
    }

    default void converterAndCheck(final String selector,
                                   final ProviderContext context,
                                   final Converter<?> expected) {
        this.converterAndCheck(
                ConverterSelector.parse(selector),
                context,
                expected
        );
    }

    default void converterAndCheck(final ConverterSelector selector,
                                   final ProviderContext context,
                                   final Converter<?> expected) {
        this.converterAndCheck(
                this.createConverterProvider(),
                selector,
                context,
                expected
        );
    }

    default void converterAndCheck(final ConverterProvider provider,
                                   final String selector,
                                   final ProviderContext context,
                                   final Converter<?> expected) {
        this.converterAndCheck(
                provider,
                ConverterSelector.parse(selector),
                context,
                expected
        );
    }

    default void converterAndCheck(final ConverterProvider provider,
                                   final ConverterSelector selector,
                                   final ProviderContext context,
                                   final Converter<?> expected) {
        this.checkEquals(
                expected,
                provider.converter(
                        selector,
                        context
                )
        );
    }

    // converter(ConverterName, List<?>)................................................................................

    @Test
    default void testConverterWithNullNameFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createConverterProvider()
                        .converter(
                                null,
                                Lists.empty(),
                                ProviderContexts.fake()
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
                                null,
                                ProviderContexts.fake()
                        )
        );
    }

    default void converterFails(final ConverterName name,
                                final List<?> values,
                                final ProviderContext context) {
        this.converterFails(
                this.createConverterProvider(),
                name,
                values,
                context
        );
    }

    default void converterFails(final ConverterProvider provider,
                                final ConverterName name,
                                final List<?> values,
                                final ProviderContext context) {
        assertThrows(
                IllegalArgumentException.class,
                () -> provider.converter(
                        name,
                        values,
                        context
                )
        );
    }

    default void converterAndCheck(final ConverterName name,
                                   final List<?> values,
                                   final ProviderContext context,
                                   final Converter<?> expected) {
        this.converterAndCheck(
                this.createConverterProvider(),
                name,
                values,
                context,
                expected
        );
    }

    default void converterAndCheck(final ConverterProvider provider,
                                   final ConverterName name,
                                   final List<?> values,
                                   final ProviderContext context,
                                   final Converter<?> expected) {
        this.checkEquals(
                expected,
                provider.converter(
                        name,
                        values,
                        context
                ),
                () -> provider + " " + name + " " + values
        );
    }

    // converterInfos...................................................................................................

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
                ConverterInfoSet.with(
                        Sets.of(
                                expected
                        )
                )
        );
    }

    default void converterInfosAndCheck(final ConverterInfoSet expected) {
        this.converterInfosAndCheck(
                this.createConverterProvider(),
                expected
        );
    }

    default void converterInfosAndCheck(final ConverterProvider provider,
                                        final ConverterInfoSet expected) {
        this.checkEquals(
                expected,
                provider.converterInfos(),
                () -> provider.toString()
        );
    }

    T createConverterProvider();
}
