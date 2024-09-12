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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converters;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;

import java.util.List;

public final class FilteredConverterProviderTest implements ConverterProviderTesting<FilteredConverterProvider>,
        ToStringTesting<FilteredConverterProvider> {

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Test
    public void testConverter() {
        final ConverterName name = ConverterName.LOCAL_DATE_TIME_TO_NUMBER;
        final List<?> values = Lists.empty();

        this.converterAndCheck(
                name,
                values,
                CONTEXT,
                ConverterProviders.converters()
                        .converter(
                                name,
                                values,
                                CONTEXT
                        )
        );
    }

    @Test
    public void testConverterWithFilteredFails() {
        this.converterFails(
                ConverterName.with("unknown123"),
                Lists.empty(),
                CONTEXT
        );
    }

    @Test
    public void testConverterInfos() {
        this.converterInfosAndCheck(
                ConverterInfoSet.EMPTY.concat(
                        ConverterInfo.parse("https://github.com/mP1/walkingkooka-convert-provider/converter/local-date-time-to-number local-date-time-to-number")
                )
        );
    }

    @Override
    public FilteredConverterProvider createConverterProvider() {
        return FilteredConverterProvider.with(
                ConverterProviders.converters(),
                ConverterInfoSet.EMPTY.concat(
                        ConverterInfo.parse("https://github.com/mP1/walkingkooka-convert-provider/converter/local-date-time-to-number local-date-time-to-number")
                )
        );
    }

    // ToString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createConverterProvider(),
                ConverterProviders.converters()
                        .toString()
        );
    }

    // class............................................................................................................

    @Override
    public Class<FilteredConverterProvider> type() {
        return FilteredConverterProvider.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
