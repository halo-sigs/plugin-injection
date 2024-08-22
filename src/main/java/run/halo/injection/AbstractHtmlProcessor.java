package run.halo.injection;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.PathContainer;
import org.springframework.util.RouteMatcher;
import org.springframework.web.util.pattern.PathPatternParser;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;
import org.springframework.web.util.pattern.PatternParseException;
import org.thymeleaf.context.Contexts;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.web.IWebRequest;

@Slf4j
public abstract class AbstractHtmlProcessor {
    private final RouteMatcher routeMatcher = createRouteMatcher();

    private RouteMatcher createRouteMatcher() {
        PathPatternParser parser = new PathPatternParser();
        parser.setPathOptions(PathContainer.Options.HTTP_PATH);
        return new PathPatternRouteMatcher(parser);
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
