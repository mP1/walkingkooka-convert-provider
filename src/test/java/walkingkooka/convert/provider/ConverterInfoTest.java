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
import walkingkooka.plugin.PluginInfoLikeTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

public final class ConverterInfoTest implements PluginInfoLikeTesting<ConverterInfo, ConverterName> {

    @Test
    public void testSetNameWithDifferent() {
        final AbsoluteUrl url = Url.parseAbsolute("https://example/converter123");
        final ConverterName different = ConverterName.with("different");

        this.setNameAndCheck(
            ConverterInfo.with(
                url,
                ConverterName.with("original-converter-name")
            ),
            different,
            ConverterInfo.with(
                url,
                different
            )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ConverterInfo> type() {
        return ConverterInfo.class;
    }

    // PluginInfoLikeTesting..............................................................................

    @Override
    public ConverterName createName(final String value) {
        return ConverterName.with(value);
    }

    @Override
    public ConverterInfo createPluginInfoLike(final AbsoluteUrl url,
                                              final ConverterName name) {
        return ConverterInfo.with(
            url,
            name
        );
    }

    // json.............................................................................................................

    @Override
    public ConverterInfo unmarshall(final JsonNode json,
                                    final JsonNodeUnmarshallContext context) {
        return ConverterInfo.unmarshall(
            json,
            context
        );
    }

    // parse.............................................................................................................

    @Override
    public ConverterInfo parseString(final String text) {
        return ConverterInfo.parse(text);
    }
}
