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

import walkingkooka.collect.set.ImmutableSet;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.plugin.PluginInfoSet;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A read only {@link Set} of {@link ConverterInfo} sorted by {@link ConverterName}.
 */
public final class ConverterInfoSet extends AbstractSet<ConverterInfo> implements PluginInfoSetLike<ConverterName, ConverterInfo, ConverterInfoSet, ConverterSelector, ConverterAlias, ConverterAliasSet> {

    public final static ConverterInfoSet EMPTY = new ConverterInfoSet(
        PluginInfoSet.with(
            Sets.<ConverterInfo>empty()
        )
    );

    public static ConverterInfoSet parse(final String text) {
        return new ConverterInfoSet(
            PluginInfoSet.parse(
                text,
                ConverterInfo::parse
            )
        );
    }

    public static ConverterInfoSet with(final Set<ConverterInfo> infos) {
        Objects.requireNonNull(infos, "infos");

        final PluginInfoSet<ConverterName, ConverterInfo> pluginInfoSet = PluginInfoSet.with(infos);
        return pluginInfoSet.isEmpty() ?
            EMPTY :
            new ConverterInfoSet(pluginInfoSet);
    }

    private ConverterInfoSet(final PluginInfoSet<ConverterName, ConverterInfo> pluginInfoSet) {
        this.pluginInfoSet = pluginInfoSet;
    }

    // PluginInfoSetLike................................................................................................

    @Override
    public Set<ConverterName> names() {
        return this.pluginInfoSet.names();
    }

    @Override
    public Set<AbsoluteUrl> url() {
        return this.pluginInfoSet.url();
    }

    @Override
    public ConverterAliasSet aliasSet() {
        return ConverterPluginHelper.INSTANCE.toAliasSet(this);
    }

    @Override
    public ConverterInfoSet filter(final ConverterInfoSet infos) {
        return this.setElements(
            this.pluginInfoSet.filter(
                infos.pluginInfoSet
            )
        );
    }

    @Override
    public ConverterInfoSet renameIfPresent(ConverterInfoSet renameInfos) {
        return this.setElements(
            this.pluginInfoSet.renameIfPresent(
                renameInfos.pluginInfoSet
            )
        );
    }

    @Override
    public ConverterInfoSet concat(final ConverterInfo info) {
        return this.setElements(
            this.pluginInfoSet.concat(info)
        );
    }

    @Override
    public ConverterInfoSet concatAll(final Collection<ConverterInfo> infos) {
        return this.setElements(
            this.pluginInfoSet.concatAll(infos)
        );
    }

    @Override
    public ConverterInfoSet delete(final ConverterInfo info) {
        return this.setElements(
            this.pluginInfoSet.delete(info)
        );
    }

    @Override
    public ConverterInfoSet deleteAll(final Collection<ConverterInfo> infos) {
        return this.setElements(
            this.pluginInfoSet.deleteAll(infos)
        );
    }

    @Override
    public ConverterInfoSet deleteIf(final Predicate<? super ConverterInfo> predicate) {
        return this.setElements(
            this.pluginInfoSet.deleteIf(predicate)
        );
    }

    @Override
    public ConverterInfoSet replace(final ConverterInfo oldInfo,
                                    final ConverterInfo newInfo) {
        return this.setElements(
            this.pluginInfoSet.replace(
                oldInfo,
                newInfo
            )
        );
    }

    @Override
    public ConverterInfoSet setElementsFailIfDifferent(final Collection<ConverterInfo> infos) {
        return this.setElements(
            this.pluginInfoSet.setElementsFailIfDifferent(
                infos
            )
        );
    }

    @Override
    public ConverterInfoSet setElements(final Collection<ConverterInfo> infos) {
        final ConverterInfoSet after = new ConverterInfoSet(
            this.pluginInfoSet.setElements(infos)
        );
        return this.pluginInfoSet.equals(infos) ?
            this :
            after;
    }

    @Override
    public Set<ConverterInfo> toSet() {
        return this.pluginInfoSet.toSet();
    }

    // TreePrintable....................................................................................................

    @Override
    public String text() {
        return this.pluginInfoSet.text();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.pluginInfoSet.printTree(printer);
        }
        printer.outdent();
    }

    // AbstractSet......................................................................................................

    @Override
    public Iterator<ConverterInfo> iterator() {
        return this.pluginInfoSet.iterator();
    }

    @Override
    public int size() {
        return this.pluginInfoSet.size();
    }

    private final PluginInfoSet<ConverterName, ConverterInfo> pluginInfoSet;

    // json.............................................................................................................

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

    static {
        ConverterInfo.register(); // force json registry

        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(ConverterInfoSet.class),
            ConverterInfoSet::unmarshall,
            ConverterInfoSet::marshall,
            ConverterInfoSet.class
        );
    }
}