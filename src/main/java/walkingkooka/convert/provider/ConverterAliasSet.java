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

import walkingkooka.collect.set.ImmutableSortedSetDefaults;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.plugin.PluginAliasSet;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedSet;

public final class ConverterAliasSet extends AbstractSet<ConverterAlias>
    implements PluginAliasSetLike<ConverterName,
    ConverterInfo,
    ConverterInfoSet,
    ConverterSelector,
    ConverterAlias,
    ConverterAliasSet>,
    ImmutableSortedSetDefaults<ConverterAliasSet, ConverterAlias> {

    /**
     * An empty {@link ConverterAliasSet}.
     */
    public final static ConverterAliasSet EMPTY = new ConverterAliasSet(
        PluginAliasSet.with(
            SortedSets.empty(),
            ConverterPluginHelper.INSTANCE
        )
    );

    /**
     * {@see PluginAliasSet#SEPARATOR}
     */
    public final static CharacterConstant SEPARATOR = PluginAliasSet.SEPARATOR;

    /**
     * Factory that creates {@link ConverterAliasSet} with the given aliases.
     */
    public static ConverterAliasSet with(final SortedSet<ConverterAlias> aliases) {
        return EMPTY.setElements(aliases);
    }

    public static ConverterAliasSet parse(final String text) {
        return new ConverterAliasSet(
            PluginAliasSet.parse(
                text,
                ConverterPluginHelper.INSTANCE
            )
        );
    }

    private ConverterAliasSet(final PluginAliasSet<ConverterName, ConverterInfo, ConverterInfoSet, ConverterSelector, ConverterAlias, ConverterAliasSet> pluginAliasSet) {
        this.pluginAliasSet = pluginAliasSet;
    }

    @Override
    public ConverterSelector selector(final ConverterSelector selector) {
        return this.pluginAliasSet.selector(selector);
    }

    @Override
    public Optional<ConverterSelector> aliasSelector(final ConverterName name) {
        return this.pluginAliasSet.aliasSelector(name);
    }

    @Override
    public Optional<ConverterName> aliasOrName(final ConverterName name) {
        return this.pluginAliasSet.aliasOrName(name);
    }

    @Override
    public ConverterInfoSet merge(final ConverterInfoSet infos) {
        return this.pluginAliasSet.merge(infos);
    }

    @Override
    public boolean containsAliasOrName(final ConverterName aliasOrName) {
        return this.pluginAliasSet.containsAliasOrName(aliasOrName);
    }

    @Override
    public ConverterAliasSet concatOrReplace(final ConverterAlias alias) {
        return new ConverterAliasSet(
            this.pluginAliasSet.concatOrReplace(alias)
        );
    }

    @Override
    public ConverterAliasSet deleteAliasOrNameAll(final Collection<ConverterName> aliasOrNames) {
        return this.setElements(
            this.pluginAliasSet.deleteAliasOrNameAll(aliasOrNames)
        );
    }

    @Override
    public ConverterAliasSet keepAliasOrNameAll(final Collection<ConverterName> aliasOrNames) {
        return this.setElements(
            this.pluginAliasSet.keepAliasOrNameAll(aliasOrNames)
        );
    }

    // ImmutableSortedSet...............................................................................................

    @Override
    public Comparator<? super ConverterAlias> comparator() {
        return this.pluginAliasSet.comparator();
    }

    @Override
    public Iterator<ConverterAlias> iterator() {
        return this.pluginAliasSet.stream().iterator();
    }

    @Override
    public int size() {
        return this.pluginAliasSet.size();
    }

    @Override
    public ConverterAliasSet setElements(final SortedSet<ConverterAlias> aliases) {
        final ConverterAliasSet after = new ConverterAliasSet(
            this.pluginAliasSet.setElements(aliases)
        );
        return this.pluginAliasSet.equals(aliases) ?
            this :
            after;
    }

    @Override
    public ConverterAliasSet setElementsFailIfDifferent(SortedSet<ConverterAlias> sortedSet) {
        return null;
    }

    @Override
    public SortedSet<ConverterAlias> toSet() {
        return this.pluginAliasSet.toSet();
    }

    @Override
    public ConverterAliasSet subSet(final ConverterAlias from,
                                    final ConverterAlias to) {
        return this.setElements(
            this.pluginAliasSet.subSet(
                from,
                to
            )
        );
    }

    @Override
    public ConverterAliasSet headSet(final ConverterAlias alias) {
        return this.setElements(
            this.pluginAliasSet.headSet(alias)
        );
    }

    @Override
    public ConverterAliasSet tailSet(final ConverterAlias alias) {
        return this.setElements(
            this.pluginAliasSet.tailSet(alias)
        );
    }

    @Override
    public ConverterAliasSet concat(final ConverterAlias alias) {
        return this.setElements(
            this.pluginAliasSet.concat(alias)
        );
    }

    @Override
    public ConverterAliasSet concatAll(final Collection<ConverterAlias> aliases) {
        return this.setElements(
            this.pluginAliasSet.concatAll(aliases)
        );
    }

    @Override
    public ConverterAliasSet delete(final ConverterAlias alias) {
        return this.setElements(
            this.pluginAliasSet.delete(alias)
        );
    }

    @Override
    public ConverterAliasSet deleteAll(final Collection<ConverterAlias> aliases) {
        return this.setElements(
            this.pluginAliasSet.deleteAll(aliases)
        );
    }

    @Override
    public ConverterAliasSet replace(final ConverterAlias oldAlias,
                                     final ConverterAlias newAlias) {
        return this.setElements(
            this.pluginAliasSet.replace(
                oldAlias,
                newAlias
            )
        );
    }

    @Override
    public ConverterAlias first() {
        return this.pluginAliasSet.first();
    }

    @Override
    public ConverterAlias last() {
        return this.pluginAliasSet.last();
    }

    @Override
    public void elementCheck(final ConverterAlias alias) {
        Objects.requireNonNull(alias, "alias");
    }

    @Override
    public String text() {
        return this.pluginAliasSet.text();
    }

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.pluginAliasSet.printTree(printer);
    }

    private final PluginAliasSet<ConverterName, ConverterInfo, ConverterInfoSet, ConverterSelector, ConverterAlias, ConverterAliasSet> pluginAliasSet;

    // Json.............................................................................................................

    static void register() {
        // helps force registry of json marshaller
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(
            this.pluginAliasSet.text()
        );
    }

    static ConverterAliasSet unmarshall(final JsonNode node,
                                        final JsonNodeUnmarshallContext context) {
        return parse(
            node.stringOrFail()
        );
    }

    static {
        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(ConverterAliasSet.class),
            ConverterAliasSet::unmarshall,
            ConverterAliasSet::marshall,
            ConverterAliasSet.class
        );
        ConverterInfoSet.EMPTY.size(); // trigger static init and json marshall/unmarshall registry
    }
}