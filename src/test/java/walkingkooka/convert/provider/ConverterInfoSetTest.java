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
import walkingkooka.collect.set.Sets;
import walkingkooka.net.Url;
import walkingkooka.plugin.PluginInfoSetLikeTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class ConverterInfoSetTest implements PluginInfoSetLikeTesting<ConverterName, ConverterInfo, ConverterInfoSet, ConverterSelector, ConverterAlias, ConverterAliasSet>,
        ClassTesting<ConverterInfoSet> {

    @Test
    public void testImmutableSet() {
        final ConverterInfoSet set = this.createSet();

        assertSame(
                set,
                Sets.immutable(set)
        );
    }

    // parse............................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ConverterInfoSet parseString(final String text) {
        return ConverterInfoSet.parse(text);
    }

    // Set..............................................................................................................

    @Override
    public ConverterInfoSet createSet() {
        return ConverterInfoSet.with(
                Sets.of(
                        this.info()
                )
        );
    }

    @Override
    public ConverterInfo info() {
        return ConverterInfo.with(
                Url.parseAbsolute("https://example.com/Converter123"),
                ConverterName.with("Converter123")
        );
    }

    // ImmutableSetTesting..............................................................................................

    @Override
    public void testSetElementsNullFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetElementsSame() {
        throw new UnsupportedOperationException();
    }

    // json.............................................................................................................

    @Test
    public void testMarshallEmpty() {
        this.marshallAndCheck(
                ConverterInfoSet.EMPTY,
                JsonNode.array()
        );
    }

    @Test
    public void testMarshallNotEmpty2() {
        this.marshallAndCheck(
                ConverterInfoSet.with(
                        Sets.of(
                                ConverterInfo.with(
                                        Url.parseAbsolute("https://example.com/test123"),
                                        ConverterName.with("test123")
                                )
                        )
                ),
                "[\n" +
                        "  \"https://example.com/test123 test123\"\n" +
                        "]"
        );
    }

    // json............................................................................................................

    @Override
    public ConverterInfoSet unmarshall(final JsonNode node,
                                       final JsonNodeUnmarshallContext context) {
        return ConverterInfoSet.unmarshall(
                node,
                context
        );
    }

    @Override
    public ConverterInfoSet createJsonNodeMarshallingValue() {
        return ConverterInfoSet.with(
                Sets.of(
                        ConverterInfo.with(
                                Url.parseAbsolute("https://example.com/test111"),
                                ConverterName.with("test111")
                        ),
                        ConverterInfo.with(
                                Url.parseAbsolute("https://example.com/test222"),
                                ConverterName.with("test222")
                        )
                )
        );
    }

    // Class............................................................................................................

    @Override
    public Class<ConverterInfoSet> type() {
        return ConverterInfoSet.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
