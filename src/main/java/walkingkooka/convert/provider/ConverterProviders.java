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
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.reflect.PublicStaticHelper;

/**
 * A collection of ConverterProvider(s).
 */
public final class ConverterProviders implements PublicStaticHelper {

    /**
     * This is the base {@link AbsoluteUrl} for all {@link Converter} in this package. The name of each
     * converter will be appended to this base.
     */
    public final static AbsoluteUrl BASE_URL = Url.parseAbsolute(
            "https://github.com/mP1/walkingkooka-convert/" + Converter.class.getSimpleName()
    );

    /**
     * {@see ConvertersConverterProvider}
     */
    public static ConverterProvider converters() {
        return ConvertersConverterProvider.INSTANCE;
    }

    /**
     * {@see EmptyConverterProvider}
     */
    public static ConverterProvider empty() {
        return EmptyConverterProvider.INSTANCE;
    }

    /**
     * {@see FakeConverterProvider}
     */
    public static ConverterProvider fake() {
        return new FakeConverterProvider();
    }

    /**
     * Stop creation
     */
    private ConverterProviders() {
        throw new UnsupportedOperationException();
    }
}
