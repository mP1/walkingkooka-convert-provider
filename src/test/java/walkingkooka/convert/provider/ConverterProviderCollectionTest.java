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
import walkingkooka.convert.Converters;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ConverterProviderCollectionTest implements ConverterProviderTesting<ConverterProviderCollection> {

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Test
    public void testWithNullProvidersFails() {
        assertThrows(
                NullPointerException.class,
                () -> ConverterProviderCollection.with(null)
        );
    }

    @Test
    public void testConverterName() {
        final ConverterProvider provider = ConverterProviders.converters();

        this.converterAndCheck(
                ConverterProviderCollection.with(Sets.of(provider)),
                ConverterName.BOOLEAN_TO_NUMBER,
                Lists.empty(),
                CONTEXT,
                Converters.booleanToNumber()
        );
    }

    @Test
    public void testConverterSelector() {
        final ConverterProvider provider = ConverterProviders.converters();

        this.converterAndCheck(
                ConverterProviderCollection.with(Sets.of(provider)),
                ConverterSelector.parse(
                        "" + ConverterName.BOOLEAN_TO_NUMBER
                ),
                CONTEXT,
                Converters.booleanToNumber()
        );
    }

    @Test
    public void testInfos() {
        final ConverterProvider provider = ConverterProviders.converters();

        this.converterInfosAndCheck(
                ConverterProviderCollection.with(Sets.of(provider)),
                provider.converterInfos()
        );
    }

    @Override
    public ConverterProviderCollection createConverterProvider() {
        return ConverterProviderCollection.with(
                Sets.of(
                        ConverterProviders.converters()
                )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ConverterProviderCollection> type() {
        return ConverterProviderCollection.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
