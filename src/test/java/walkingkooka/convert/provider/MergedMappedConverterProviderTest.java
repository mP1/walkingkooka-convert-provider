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
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MergedMappedConverterProviderTest implements ConverterProviderTesting<MergedMappedConverterProvider>,
    ToStringTesting<MergedMappedConverterProvider> {

    private final static AbsoluteUrl RENAMED_URL = Url.parseAbsolute("https://example.com/renamed-converter-111");

    private final static ConverterName RENAMED_RENAME_NAME = ConverterName.with("renamed-rename-converter-111");

    private final static ConverterName RENAMED_PROVIDER_NAME = ConverterName.with("renamed-provider-only-converter-111");

    private final static Converter<ConverterContext> RENAME_CONVERTER = Converters.fake();

    private final static AbsoluteUrl PROVIDER_ONLY_URL = Url.parseAbsolute("https://example.com/provider-only-converter-222");

    private final static ConverterName PROVIDER_ONLY_NAME = ConverterName.with("provider-only-converter-222");

    private final static Converter<ConverterContext> PROVIDER_ONLY_CONVERTER = Converters.fake();

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Test
    public void testWithNullInfosFails() {
        assertThrows(
            NullPointerException.class,
            () -> MergedMappedConverterProvider.with(
                null,
                ConverterProviders.fake()
            )
        );
    }

    @Test
    public void testWithNullProviderFails() {
        assertThrows(
            NullPointerException.class,
            () -> MergedMappedConverterProvider.with(
                ConverterInfoSet.EMPTY,
                null
            )
        );
    }

    @Test
    public void testConverterSelectorWithUnknownFails() {
        this.converterFails(
            ConverterSelector.parse("unknown"),
            CONTEXT
        );
    }

    @Test
    public void testConverterSelectorWithRename() {
        this.converterAndCheck(
            ConverterSelector.parse("" + RENAMED_RENAME_NAME),
            CONTEXT,
            RENAME_CONVERTER
        );
    }

    @Test
    public void testConverterSelectorWithProviderOnly() {
        this.converterAndCheck(
            ConverterSelector.parse("" + PROVIDER_ONLY_NAME),
            CONTEXT,
            PROVIDER_ONLY_CONVERTER
        );
    }

    @Test
    public void testConverterNameWithUnknownFails() {
        this.converterFails(
            ConverterName.with("unknown"),
            Lists.empty(),
            CONTEXT
        );
    }

    @Test
    public void testConverterNameWithRename() {
        this.converterAndCheck(
            RENAMED_RENAME_NAME,
            Lists.empty(),
            CONTEXT,
            RENAME_CONVERTER
        );
    }

    @Test
    public void testConverterNameWithProviderOnly() {
        this.converterAndCheck(
            PROVIDER_ONLY_NAME,
            Lists.empty(),
            CONTEXT,
            PROVIDER_ONLY_CONVERTER
        );
    }

    @Test
    public void testInfos() {
        this.converterInfosAndCheck(
            ConverterInfo.with(
                RENAMED_URL,
                RENAMED_RENAME_NAME
            ),
            ConverterInfo.with(
                PROVIDER_ONLY_URL,
                PROVIDER_ONLY_NAME
            )
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createConverterProvider(),
            "https://example.com/provider-only-converter-222 provider-only-converter-222,https://example.com/renamed-converter-111 renamed-rename-converter-111"
        );
    }

    @Override
    public MergedMappedConverterProvider createConverterProvider() {
        return MergedMappedConverterProvider.with(
            ConverterInfoSet.with(
                Sets.of(
                    ConverterInfo.with(
                        RENAMED_URL,
                        RENAMED_RENAME_NAME
                    )
                )
            ),
            new FakeConverterProvider() {

                @Override
                public <C extends ConverterContext> Converter<C> converter(final ConverterName name,
                                                                           final List<?> values,
                                                                           final ProviderContext context) {
                    Objects.requireNonNull(name, "name");
                    Objects.requireNonNull(values, "values");
                    Objects.requireNonNull(context, "context");

                    if (name.equals(RENAMED_PROVIDER_NAME)) {
                        return Cast.to(RENAME_CONVERTER);
                    }
                    if (name.equals(PROVIDER_ONLY_NAME)) {
                        return Cast.to(PROVIDER_ONLY_CONVERTER);
                    }
                    throw new IllegalArgumentException("Unknown Converter " + name);
                }

                @Override
                public ConverterInfoSet converterInfos() {
                    return ConverterInfoSet.with(
                        Sets.of(
                            ConverterInfo.with(
                                RENAMED_URL,
                                RENAMED_PROVIDER_NAME
                            ),
                            ConverterInfo.with(
                                PROVIDER_ONLY_URL,
                                PROVIDER_ONLY_NAME
                            )
                        )
                    );
                }
            }
        );
    }

    // Class............................................................................................................

    @Override
    public Class<MergedMappedConverterProvider> type() {
        return MergedMappedConverterProvider.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
