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
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.Url;
import walkingkooka.plugin.PluginAliasLikeTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ConverterAliasTest implements PluginAliasLikeTesting<ConverterName, ConverterSelector, ConverterAlias> {

    private final static ConverterName NAME = ConverterName.with("Hello");

    private final static Optional<ConverterSelector> SELECTOR = Optional.of(
        ConverterSelector.parse("converter123")
    );

    private final static Optional<AbsoluteUrl> URL = Optional.of(
        Url.parseAbsolute("https://example.com/converter123")
    );

    // with.............................................................................................................

    @Test
    public void testWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> ConverterAlias.with(
                null,
                SELECTOR,
                URL
            )
        );
    }

    @Test
    public void testWithNullSelectorFails() {
        assertThrows(
            NullPointerException.class,
            () -> ConverterAlias.with(
                NAME,
                null,
                URL
            )
        );
    }

    @Test
    public void testWithNullUrlFails() {
        assertThrows(
            NullPointerException.class,
            () -> ConverterAlias.with(
                NAME,
                SELECTOR,
                null
            )
        );
    }

    // parse............................................................................................................

    @Test
    public void testParse() {
        this.parseStringAndCheck(
            "alias1 name1 https://example.com",
            ConverterAlias.with(
                ConverterName.with("alias1"),
                Optional.of(
                    ConverterSelector.parse("name1")
                ),
                Optional.of(
                    Url.parseAbsolute("https://example.com")
                )
            )
        );
    }

    @Override
    public ConverterAlias parseString(final String text) {
        return ConverterAlias.parse(text);
    }

    // Comparable.......................................................................................................

    @Override
    public ConverterAlias createComparable() {
        return ConverterAlias.with(
            NAME,
            SELECTOR,
            URL
        );
    }

    // class............................................................................................................

    @Override
    public Class<ConverterAlias> type() {
        return ConverterAlias.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
