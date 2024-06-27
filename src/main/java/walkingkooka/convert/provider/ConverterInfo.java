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
import walkingkooka.net.Url;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Objects;

/**
 * Provides a few bits of info describing a {@link Converter}. The {@link AbsoluteUrl} must be a unique identifier,
 * with the {@link ConverterName} being a shorter human friendly reference.
 */
public final class ConverterInfo implements PluginInfoLike<ConverterInfo, ConverterName> {

    public static ConverterInfo parse(final String text) {
        return PluginInfoLike.parse(
                text,
                ConverterName::with,
                ConverterInfo::with
        );
    }

    public static ConverterInfo with(final AbsoluteUrl url,
                                     final ConverterName name) {
        return new ConverterInfo(
                Objects.requireNonNull(url, "url"),
                Objects.requireNonNull(name, "name")
        );
    }

    private ConverterInfo(final AbsoluteUrl url,
                          final ConverterName name) {
        this.url = url;
        this.name = name;
    }

    @Override
    public AbsoluteUrl url() {
        return this.url;
    }

    private final AbsoluteUrl url;

    // HasName..........................................................................................................

    @Override
    public ConverterName name() {
        return this.name;
    }

    private final ConverterName name;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
                this.url,
                this.name
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof ConverterInfo &&
                        this.equals0(Cast.to(other));
    }

    private boolean equals0(final ConverterInfo other) {
        return this.url.equals(other.url) &&
                this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return PluginInfoLike.toString(this);
    }

    // Json.............................................................................................................

    static void register() {

    }

    static ConverterInfo unmarshall(final JsonNode node,
                                    final JsonNodeUnmarshallContext context) {
        return PluginInfoLike.unmarshall(
                node,
                context,
                ConverterName.class,
                ConverterInfo::with
        );
    }

    static {
        Url.parseAbsoluteOrRelative("/");
        ConverterName.with("Hello"); // force json registry

        JsonNodeContext.register(
                JsonNodeContext.computeTypeName(ConverterInfo.class),
                ConverterInfo::unmarshall,
                ConverterInfo::marshall,
                ConverterInfo.class
        );
    }
}
