package run.halo.injection;

import java.util.Set;
import org.thymeleaf.context.ITemplateContext;

public abstract class AbstractHtmlProcessor {
    protected static final String TEMPLATE_ID_VARIABLE = "_templateId";

    protected boolean isContentTemplate(ITemplateContext context) {
        return "post".equals(context.getVariable(TEMPLATE_ID_VARIABLE))
            || "page".equals(context.getVariable(TEMPLATE_ID_VARIABLE));
    }

    // 匹配路径接口
    protected boolean isRequestPathMatchingRoute(String requestRoute, Set<String> pageRules) {
        return true;
    }
}
