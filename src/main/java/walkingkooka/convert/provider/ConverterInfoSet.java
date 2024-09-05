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

import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.set.ImmutableSetDefaults;
import walkingkooka.collect.set.Sets;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.net.http.server.hateos.HateosResource;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * A read only {@link Set} of {@link ConverterInfo} sorted by {@link ConverterName}.
 */
public final class ConverterInfoSet extends AbstractSet<ConverterInfo> implements PluginInfoSetLike<ConverterInfo, ConverterName>,
        ImmutableSetDefaults<ConverterInfoSet, ConverterInfo> {

    /**
     * Parses the CSV text into a {@link ConverterInfoSet}.
     */
    public static ConverterInfoSet parse(final String text) {
        return PluginInfoSetLike.parse(
                text,
                ConverterInfo::parse,
                ConverterInfoSet::with
        );
    }

    /**
     * Factory that creates a {@link ConverterInfoSet} with the provided {@link ConverterInfo}.
     */
    public static ConverterInfoSet with(final Set<ConverterInfo> infos) {
        Objects.requireNonNull(infos, "infos");

        final Set<ConverterInfo> copy = SortedSets.tree(HateosResource.comparator());
        copy.addAll(infos);
        return new ConverterInfoSet(copy);
    }

    private ConverterInfoSet(final Set<ConverterInfo> infos) {
        this.infos = infos;
    }

    // AbstractSet......................................................................................................

    @Override
    public Iterator<ConverterInfo> iterator() {
        return Iterators.readOnly(
                this.infos.iterator()
        );
    }

    @Override
    public int size() {
        return this.infos.size();
    }

    private final Set<ConverterInfo> infos;

    // ImmutableSet.....................................................................................................

    @Override
    public ConverterInfoSet setElements(final Set<ConverterInfo> elements) {
        final ConverterInfoSet copy = with(elements);
        return this.equals(copy) ?
                this :
                copy;
    }

    @Override
    public Set<ConverterInfo> toSet() {
        return new TreeSet<>(
                this.infos
        );
    }

    // json.............................................................................................................

    static {
        ConverterInfo.register(); // force registry of json marshaller

        JsonNodeContext.register(
                JsonNodeContext.computeTypeName(ConverterInfoSet.class),
                ConverterInfoSet::unmarshall,
                ConverterInfoSet::marshall,
                ConverterInfoSet.class
        );
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return context.marshallCollection(this);
    }

    // @VisibleForTesting
    static ConverterInfoSet unmarshall(final JsonNode node,
                                       final JsonNodeUnmarshallContext context) {
        return with(
                context.unmarshallSet(
                        node,
                        ConverterInfo.class
                )
        );
    }
}
