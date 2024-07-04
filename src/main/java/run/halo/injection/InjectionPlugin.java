package run.halo.injection;

import org.springframework.stereotype.Component;
import run.halo.app.extension.Scheme;
import run.halo.app.extension.SchemeManager;
import run.halo.app.plugin.BasePlugin;
import run.halo.app.plugin.PluginContext;


@Component
public class InjectionPlugin extends BasePlugin {
    private final SchemeManager schemeManager;

    public InjectionPlugin(PluginContext pluginContext, SchemeManager schemeManager) {
        super(pluginContext);
        this.schemeManager = schemeManager;
    }

    @Override
    public void start() {
        schemeManager.register(HtmlInjection.class);
    }

    @Override
    public void stop() {
        schemeManager.unregister(Scheme.buildFromType(HtmlInjection.class));
    }
}

