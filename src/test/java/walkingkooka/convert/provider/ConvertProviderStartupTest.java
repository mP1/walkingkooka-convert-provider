package walkingkooka.convert.provider;

import walkingkooka.reflect.PublicStaticHelperTesting;

import java.lang.reflect.Method;

public final class ConvertProviderStartupTest implements PublicStaticHelperTesting<ConvertProviderStartup> {

    @Override
    public boolean canHavePublicTypes(final Method method) {
        return false;
    }

    @Override
    public Class<ConvertProviderStartup> type() {
        return ConvertProviderStartup.class;
    }
}
