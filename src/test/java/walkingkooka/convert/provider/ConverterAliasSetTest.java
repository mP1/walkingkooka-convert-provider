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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.plugin.PluginAliasSetLikeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ConverterAliasSetTest implements PluginAliasSetLikeTesting<ConverterName,
    ConverterInfo,
    ConverterInfoSet,
    ConverterSelector,
    ConverterAlias,
    ConverterAliasSet>,
    HashCodeEqualsDefinedTesting2<ConverterAliasSet>,
    ToStringTesting<ConverterAliasSet>,
    JsonNodeMarshallingTesting<ConverterAliasSet> {

    // with.............................................................................................................

    @Test
    public void testWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> ConverterAliasSet.with(null)
        );
    }

    @Test
    public void testWithEmpty() {
        assertSame(
            ConverterAliasSet.EMPTY,
            ConverterAliasSet.with(SortedSets.empty())
        );
    }

    // name.............................................................................................................

    @Test
    public void testAliasOrNameWithName() {
        final ConverterName abc = ConverterName.with("abc");

        this.aliasOrNameAndCheck(
            this.createSet(),
            abc,
            abc
        );
    }

    @Test
    public void testAliasOrNameWithAlias() {
        this.aliasOrNameAndCheck(
            this.createSet(),
            ConverterName.with("sunshine-alias"),
            ConverterName.with("sunshine")
        );
    }

    @Test
    public void testAliasSelectorWithName() {
        this.aliasSelectorAndCheck(
            this.createSet(),
            ConverterName.with("abc")
        );
    }

    @Test
    public void testAliasSelectorWithAlias() {
        this.aliasSelectorAndCheck(
            this.createSet(),
            ConverterName.with("custom-alias"),
            ConverterSelector.parse("custom(1)")
        );
    }

    @Override
    public ConverterAliasSet createSet() {
        return ConverterAliasSet.parse("abc, moo, mars, custom-alias custom(1) https://example.com/custom , sunshine-alias sunshine");
    }

    // parse............................................................................................................

    @Override
    public ConverterAliasSet parseString(final String text) {
        return ConverterAliasSet.parse(text);
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferent() {
        this.checkNotEquals(
            ConverterAliasSet.parse("different")
        );
    }

    @Override
    public ConverterAliasSet createObject() {
        return ConverterAliasSet.parse("abc, custom-alias custom(1) https://example.com/custom");
    }

    // json.............................................................................................................

    @Override
    public ConverterAliasSet unmarshall(final JsonNode json,
                                        final JsonNodeUnmarshallContext context) {
        return ConverterAliasSet.unmarshall(
            json,
            context
        );
    }

    @Override
    public ConverterAliasSet createJsonNodeMarshallingValue() {
        return ConverterAliasSet.parse("alias1 name1, name2, alias3 name3(\"999\") https://example.com/name3");
    }

    // class............................................................................................................

    @Override
    public Class<ConverterAliasSet> type() {
        return ConverterAliasSet.class;
    }
}
