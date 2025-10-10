package walkingkooka.convert.provider;

import walkingkooka.net.*;
import walkingkooka.reflect.JavaVisibility;
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

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
