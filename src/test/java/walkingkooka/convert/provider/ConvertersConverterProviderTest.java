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
import walkingkooka.convert.Converters;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.plugin.ProviderContexts;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.MethodAttributes;
import walkingkooka.text.CaseKind;

import java.lang.reflect.Method;
import java.util.Set;

public final class ConvertersConverterProviderTest implements ConverterProviderTesting<ConvertersConverterProvider> {

    private final static ProviderContext CONTEXT = ProviderContexts.fake();

    @Test
    public void testConverterSelectorBooleanToNumber() {
        this.converterAndCheck(
                ConverterSelector.with(
                        ConverterName.BOOLEAN_TO_NUMBER,
                        ""
                ),
                CONTEXT,
                Converters.booleanToNumber()
        );
    }

    @Test
    public void testConverterSelectorCharacterOrStringToString() {
        this.converterAndCheck(
                ConverterSelector.with(
                        ConverterName.CHARACTER_OR_STRING_TO_STRING,
                        ""
                ),
                CONTEXT,
                Converters.characterOrStringToString()
        );
    }

    @Test
    public void testConverterNameBooleanToNumber() {
        this.converterAndCheck(
                ConverterName.BOOLEAN_TO_NUMBER,
                Lists.empty(),
                CONTEXT,
                Converters.booleanToNumber()
        );
    }

    @Test
    public void testConverterNameCharacterOrStringToString() {
        this.converterAndCheck(
                ConverterName.CHARACTER_OR_STRING_TO_STRING,
                Lists.empty(),
                CONTEXT,
                Converters.characterOrStringToString()
        );
    }

    @Test
    public void testConverterFactoryMethodWithoutParameters() {
        final Set<ConverterName> missing = Sets.sorted();
        final ConvertersConverterProvider provider = this.createConverterProvider();
        int i = 0;

        for (final Method method : Converters.class.getMethods()) {
            if (JavaVisibility.PUBLIC != JavaVisibility.of(method)) {
                continue;
            }

            if (false == MethodAttributes.STATIC.is(method)) {
                continue;
            }

            final String methodName = method.getName();
            if ("fake".equals(methodName)) {
                continue;
            }

            if (method.getReturnType() != Converter.class) {
                continue;
            }

            if (method.getParameters().length > 0) {
                continue;
            }

            final String name = CaseKind.CAMEL.change(
                    methodName,
                    CaseKind.KEBAB
            );

            System.out.println(method + " " + name);

            final ConverterName converterName = ConverterName.with(name);

            try {
                ConverterSelector.with(
                        converterName,
                        ""
                ).evaluateText(
                        provider,
                        CONTEXT
                );
            } catch (final Exception fail) {
                missing.add(converterName);
            }

            i++;
        }

        this.checkNotEquals(
                0,
                i
        );

        this.checkEquals(
                Sets.empty(),
                missing
        );
    }

    @Test
    public void testConverterCollection() {
        this.converterAndCheck(
                ConverterSelector.parse("collection (boolean-to-number)"),
                CONTEXT,
                Converters.collection(
                        Lists.of(
                                Converters.booleanToNumber()
                        )
                )
        );
    }

    @Test
    public void testConverterCollection2() {
        this.converterAndCheck(
                ConverterSelector.parse("collection (boolean-to-number, character-or-string-to-string)"),
                CONTEXT,
                Converters.collection(
                        Lists.of(
                                Converters.booleanToNumber(),
                                Converters.characterOrStringToString()
                        )
                )
        );
    }

    @Override
    public ConvertersConverterProvider createConverterProvider() {
        return ConvertersConverterProvider.INSTANCE;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    @Override
    public Class<ConvertersConverterProvider> type() {
        return ConvertersConverterProvider.class;
    }
}
