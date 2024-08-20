package run.halo.injection;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.RouteMatcher;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;
import org.springframework.web.util.pattern.PatternParseException;
import org.thymeleaf.context.Contexts;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.web.IWebRequest;

@Slf4j
public abstract class AbstractHtmlProcessor {
    protected static final String TEMPLATE_ID_VARIABLE = "_templateId";
    private final PathPatternRouteMatcher routeMatcher = new PathPatternRouteMatcher();

    protected boolean isContentTemplate(ITemplateContext context) {
        return "post".equals(context.getVariable(TEMPLATE_ID_VARIABLE))
            || "page".equals(context.getVariable(TEMPLATE_ID_VARIABLE));
    }

    // 匹配路径
    protected boolean isRequestPathMatchingRoute(ITemplateContext context, Set<String> pageRules) {
        if (!Contexts.isWebContext(context)) {
            return false;
        }
        IWebRequest request = Contexts.asWebContext(context).getExchange().getRequest();
        String requestPath = request.getRequestPath();
        RouteMatcher.Route requestRoute = routeMatcher.parseRoute(requestPath);

        // 遍历 pageRules 中的路径模式，检查是否有匹配的
        for (String pathPattern : pageRules) {
            try {
                if (routeMatcher.match(pathPattern, requestRoute)) {
                    return true;
                }
            } catch (PatternParseException e) {
                // ignore
                log.warn("Parse route pattern [{}] failed", pathPattern, e);
            }
        }
        return false;
    }
}
