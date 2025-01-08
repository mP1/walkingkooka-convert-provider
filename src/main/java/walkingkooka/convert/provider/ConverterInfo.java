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

import walkingkooka.Cast;
import walkingkooka.convert.Converter;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.http.server.hateos.HateosResource;
import walkingkooka.plugin.PluginInfo;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

/**
 * Captures a unique {@link AbsoluteUrl} and {@link ConverterName} for a {@link Converter}.
 */
public final class ConverterInfo implements PluginInfoLike<ConverterInfo, ConverterName>,
    HateosResource<ConverterName> {

    public static ConverterInfo parse(final String text) {
        return new ConverterInfo(
            PluginInfo.parse(
                text,
                ConverterName::with
            )
        );
    }

    public static ConverterInfo with(final AbsoluteUrl url,
                                     final ConverterName name) {
        return new ConverterInfo(
            PluginInfo.with(
                url,
                name
            )
        );
    }

    private ConverterInfo(final PluginInfo<ConverterName> pluginInfo) {
        this.pluginInfo = pluginInfo;
    }

    // HasAbsoluteUrl...................................................................................................

    @Override
    public AbsoluteUrl url() {
        return this.pluginInfo.url();
    }

    // HasName..........................................................................................................

    @Override
    public ConverterName name() {
        return this.pluginInfo.name();
    }

    @Override
    public ConverterInfo setName(final ConverterName name) {
        return this.name().equals(name) ?
            this :
            new ConverterInfo(
                this.pluginInfo.setName(name)
            );
    }

    private final PluginInfo<ConverterName> pluginInfo;

    // Comparable.......................................................................................................

    @Override
    public int compareTo(final ConverterInfo other) {
        return this.pluginInfo.compareTo(other.pluginInfo);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.pluginInfo.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof ConverterInfo &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final ConverterInfo other) {
        return this.pluginInfo.equals(other.pluginInfo);
    }

    @Override
    public String toString() {
        return this.pluginInfo.toString();
    }

    // Json.............................................................................................................

    static void register() {
        // helps force registry of json marshaller
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(this.toString());
    }

    static ConverterInfo unmarshall(final JsonNode node,
                                    final JsonNodeUnmarshallContext context) {
        return ConverterInfo.parse(
            node.stringOrFail()
        );
    }

    static {
        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(ConverterInfo.class),
            ConverterInfo::unmarshall,
            ConverterInfo::marshall,
            ConverterInfo.class
        );
        ConverterName.with("hello"); // trigger static init and json marshall/unmarshall registry
    }
}
