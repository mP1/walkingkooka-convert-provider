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

import javaemul.internal.annotations.GwtIncompatible;
import walkingkooka.collect.map.Maps;
import walkingkooka.convert.Converter;
import walkingkooka.convert.Converters;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Base class that exists to support shadowing of {@link ConverterName#TEXT_TO_PATH}.
 */
abstract class ConverterNameGwt {

    static Supplier<Converter<?>> textToPathConverterSupplier() {
        return null;
    }

    ConverterNameGwt() {
        super();
    }
}
