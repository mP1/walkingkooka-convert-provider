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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.FakeConverter;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;

import java.util.List;

public final class AliasesConverterProviderTest implements ConverterProviderTesting<AliasesConverterProvider> {

    private final static String NAME1_STRING = "converter1";

    private final static ConverterName NAME1 = ConverterName.with(NAME1_STRING);

    private final static ConverterInfo INFO1 = ConverterInfo.parse("https://example.com/converter1 " + NAME1);

    private final static ConverterName ALIAS2 = ConverterName.with("alias2");

    private final static Converter<FakeConverterContext> CONVERTER1 = converter(NAME1);

    private final static String NAME2_STRING = "converter2";

    private final static ConverterName NAME2 = ConverterName.with(NAME2_STRING);

    private final static Converter<FakeConverterContext> CONVERTER2 = converter(NAME2);

    private final static ConverterInfo INFO2 = ConverterInfo.parse("https://example.com/converter2 " + NAME2);

    private final static String NAME3_STRING = "converter3";

    private final static ConverterName NAME3 = ConverterName.with(NAME3_STRING);

    private final static Converter<FakeConverterContext> CONVERTER3 = converter(NAME3);

    private final static ConverterInfo INFO3 = ConverterInfo.parse("https://example.com/converter3 " + NAME3);

    private final static String VALUE3 = "Value3";

    private final static String NAME4_STRING = "custom4";

    private final static ConverterName NAME4 = ConverterName.with(NAME4_STRING);

    private final static ConverterInfo INFO4 = ConverterInfo.parse("https://example.com/custom4 " + NAME4);

    private static Converter<FakeConverterContext> converter(final ConverterName name) {
        return new FakeConverter() {

            @Override
            public int hashCode() {
                return name.hashCode();
            }

            @Override
            public boolean equals(final Object other) {
                return this == other || other instanceof Converter && this.equals0((Converter<?>) other);
            }

            private boolean equals0(final Converter<?> other) {
                return this.toString().equals(other.toString());
            }

            @Override
            public String toString() {
                return name.toString();
            }
        };
    }

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Test
    public void testWithUnknownConverterName() {
        AliasesConverterProvider.with(
            ConverterAliasSet.parse("unknown-converter404"),
            new FakeConverterProvider() {
                @Override
                public ConverterInfoSet converterInfos() {
                    return ConverterInfoSet.parse("https://example.com/converter111 converter111");
                }
            }
        );
    }

    @Test
    public void testConverterNameWithName() {
        this.converterAndCheck(
            NAME1,
            Lists.empty(),
            CONTEXT,
            CONVERTER1
        );
    }

    @Test
    public void testConverterSelectorWithName() {
        this.converterAndCheck(
            ConverterSelector.parse(NAME1 + ""),
            CONTEXT,
            CONVERTER1
        );
    }

    @Test
    public void testConverterNameWithAlias() {
        this.converterAndCheck(
            ALIAS2,
            Lists.empty(),
            CONTEXT,
            CONVERTER2
        );
    }

    @Test
    public void testConverterSelectorWithAlias() {
        this.converterAndCheck(
            ConverterSelector.parse(ALIAS2 + ""),
            CONTEXT,
            CONVERTER2
        );
    }

    @Test
    public void testConverterNameWithSelector() {
        this.converterAndCheck(
            NAME4,
            Lists.empty(),
            CONTEXT,
            CONVERTER3
        );
    }

    @Test
    public void testConverterSelectorWithSelector() {
        this.converterAndCheck(
            ConverterSelector.parse(NAME4 + ""),
            CONTEXT,
            CONVERTER3
        );
    }

    @Test
    public void testInfos() {
        this.converterInfosAndCheck(
            INFO1,
            INFO2.setName(ALIAS2),
            INFO4.setName(NAME4) // from ConverterAliasSet
        );
    }

    @Override
    public AliasesConverterProvider createConverterProvider() {
        final String aliases = "converter1, alias2 converter2, custom4 converter3(\"Value3\") https://example.com/custom4";

        this.checkEquals(
            NAME1 + ", " + ALIAS2 + " " + NAME2 + ", " + NAME4 + " " + NAME3 + "(\"" + VALUE3 + "\") " + INFO4.url(),
            aliases
        );

        return AliasesConverterProvider.with(
            ConverterAliasSet.parse(aliases),
            new FakeConverterProvider() {
                @Override
                public <C extends ConverterContext> Converter<C> converter(final ConverterSelector selector,
                                                                           final ProviderContext context) {
                    return selector.evaluateValueText(
                        this,
                        context
                    );
                }

                @Override
                public <C extends ConverterContext> Converter<C> converter(final ConverterName name,
                                                                           final List<?> values,
                                                                           final ProviderContext context) {
                    Converter<?> converter;

                    switch (name.toString()) {
                        case NAME1_STRING:
                            checkEquals(Lists.empty(), values, "values");
                            converter = CONVERTER1;
                            break;
                        case NAME2_STRING:
                            checkEquals(Lists.empty(), values, "values");
                            converter = CONVERTER2;
                            break;
                        case NAME3_STRING:
                            checkEquals(Lists.of(VALUE3), values, "values");
                            converter = CONVERTER3;
                            break;
                        default:
                            throw new IllegalArgumentException("Unknown Converter " + name);
                    }

                    return Cast.to(converter);
                }

                @Override
                public ConverterInfoSet converterInfos() {
                    return ConverterInfoSet.with(
                        Sets.of(
                            INFO1,
                            INFO2,
                            INFO3
                        )
                    );
                }
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<AliasesConverterProvider> type() {
        return AliasesConverterProvider.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
