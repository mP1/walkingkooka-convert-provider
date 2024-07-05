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
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.Converters;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.reflect.JavaVisibility;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MappedConverterProviderTest implements ConverterProviderTesting<MappedConverterProvider>,
        ToStringTesting<MappedConverterProvider> {

    private final static AbsoluteUrl URL = Url.parseAbsolute("https://example.com/converter123");

    private final static ConverterName NAME = ConverterName.with("different-converter-name-123");

    private final static ConverterName ORIGINAL_NAME = ConverterName.with("original-converter-123");

    private final static Converter<ConverterContext> CONVERTER = Converters.fake();

    @Test
    public void testWithNullViewFails() {
        assertThrows(
                NullPointerException.class,
                () -> MappedConverterProvider.with(
                        null,
                        ConverterProviders.fake()
                )
        );
    }

    @Test
    public void testWithNullProviderFails() {
        assertThrows(
                NullPointerException.class,
                () -> MappedConverterProvider.with(
                        Sets.empty(),
                        null
                )
        );
    }

    @Test
    public void testConverter() {
        this.converterAndCheck(
                NAME,
                Lists.empty(),
                CONVERTER
        );
    }

    @Test
    public void testConverterUnknown() {
        this.converterAndCheck(
                ConverterName.with("unknown"),
                Lists.empty()
        );
    }

    @Test
    public void testInfos() {
        this.converterInfosAndCheck(
                ConverterInfo.with(
                        URL,
                        NAME
                )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createConverterProvider(),
                "https://example.com/converter123 different-converter-name-123"
        );
    }

    @Override
    public MappedConverterProvider createConverterProvider() {
        return MappedConverterProvider.with(
                Sets.of(
                        ConverterInfo.with(
                                URL,
                                NAME
                        )
                ),
                new FakeConverterProvider() {

                    @Override
                    public <C extends ConverterContext> Optional<Converter<C>> converter(final ConverterName name,
                                                                                         final List<?> values) {
                        Objects.requireNonNull(name, "name");
                        Objects.requireNonNull(values, "values");

                        return Optional.ofNullable(
                                name.equals(ORIGINAL_NAME) ?
                                        Cast.to(CONVERTER) :
                                        null
                        );
                    }

                    @Override
                    public Set<ConverterInfo> converterInfos() {
                        return Sets.of(
                                ConverterInfo.with(
                                        URL,
                                        ORIGINAL_NAME
                                )
                        );
                    }
                }
        );
    }

    // Class............................................................................................................

    @Override
    public Class<MappedConverterProvider> type() {
        return MappedConverterProvider.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}