package run.halo.injection;

import static run.halo.app.extension.index.IndexAttributeFactory.simpleAttribute;

import org.springframework.stereotype.Component;
import run.halo.app.extension.Scheme;
import run.halo.app.extension.SchemeManager;
import run.halo.app.extension.index.IndexSpec;
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
        schemeManager.register(HtmlInjection.class, indexSpecs -> {
            // 为 enabled 添加索引方便根据启用状态查询
            indexSpecs.add(new IndexSpec()
                .setName("spec.enabled")
                .setIndexFunc(simpleAttribute(HtmlInjection.class, htmlInjection -> {
                    return Boolean.toString(htmlInjection.getSpec().isEnabled());
                }))
            );
            // 为 injectionPoint 添加索引
            indexSpecs.add(new IndexSpec()
                .setName("spec.injectionPoint")
                .setIndexFunc(simpleAttribute(HtmlInjection.class, htmlInjection -> {
                    HtmlInjection.InjectionPoint injectionPoint =
                        htmlInjection.getSpec().getInjectionPoint();
                    return injectionPoint != null ? injectionPoint.name() : null;
                }))
            );
        });
    }

    @Override
    public void stop() {
        schemeManager.unregister(Scheme.buildFromType(HtmlInjection.class));
    }
}

