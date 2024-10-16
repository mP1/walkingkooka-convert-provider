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

import walkingkooka.collect.set.Sets;
import walkingkooka.naming.Name;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.plugin.PluginHelper;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.parser.ParserContext;
import walkingkooka.text.cursor.parser.ParserToken;
import walkingkooka.text.cursor.parser.Parsers;
import walkingkooka.text.cursor.parser.StringParserToken;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Function;

final class ConverterPluginHelper implements PluginHelper<ConverterName,
        ConverterInfo,
        ConverterInfoSet,
        ConverterSelector,
        ConverterAlias,
        ConverterAliasSet> {

    final static ConverterPluginHelper INSTANCE = new ConverterPluginHelper();

    private ConverterPluginHelper() {
    }

    @Override
    public ConverterName name(final String text) {
        return ConverterName.with(text);
    }

    @Override
    public Optional<ConverterName> parseName(final TextCursor cursor,
                                             final ParserContext context) {
        Objects.requireNonNull(cursor, "cursor");
        Objects.requireNonNull(context, "context");

        return Parsers.stringInitialAndPartCharPredicate(
                c -> ConverterName.isChar(0, c),
                c -> ConverterName.isChar(1, c),
                ConverterName.MIN_LENGTH, // minLength
                ConverterName.MAX_LENGTH // maxLength
        ).parse(
                cursor,
                context
        ).map(
                (final ParserToken token) -> this.name(
                        token.cast(StringParserToken.class).value()
                )
        );
    }

    @Override
    public Set<ConverterName> names(final Set<ConverterName> names) {
        return Sets.immutable(
                Objects.requireNonNull(names, "names")
        );
    }

    @Override
    public Function<ConverterName, RuntimeException> unknownName() {
        return n -> new IllegalArgumentException("Unknown Converter " + n);
    }

    @Override
    public Comparator<ConverterName> nameComparator() {
        return Name.comparator(ConverterName.CASE_SENSITIVITY);
    }

    @Override
    public ConverterInfo info(final AbsoluteUrl url,
                              final ConverterName name) {
        return ConverterInfo.with(url, name);
    }

    @Override
    public ConverterInfo parseInfo(final String text) {
        return ConverterInfo.parse(text);
    }

    @Override
    public ConverterInfoSet infoSet(final Set<ConverterInfo> infos) {
        return ConverterInfoSet.with(infos);
    }

    @Override
    public ConverterSelector parseSelector(final String text) {
        return ConverterSelector.parse(text);
    }

    @Override
    public ConverterAlias alias(final ConverterName name,
                                final Optional<ConverterSelector> selector,
                                final Optional<AbsoluteUrl> url) {
        return ConverterAlias.with(
                name,
                selector,
                url
        );
    }

    @Override
    public ConverterAliasSet aliasSet(final SortedSet<ConverterAlias> aliases) {
        return ConverterAliasSet.with(aliases);
    }

    @Override
    public String label() {
        return "Converter";
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
