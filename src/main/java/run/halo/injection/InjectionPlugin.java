package run.halo.injection;
import org.pf4j.PluginWrapper;
import org.springframework.stereotype.Component;
import run.halo.app.plugin.BasePlugin;
@Component
public class InjectionPlugin extends BasePlugin {
    public InjectionPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }
}

