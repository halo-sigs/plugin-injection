package run.halo.injection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import reactor.core.publisher.Mono;
import run.halo.app.theme.dialect.TemplateHeadProcessor;

@Slf4j
@Component
@RequiredArgsConstructor
public class HtmlHeadProcessor extends AbstractHtmlProcessor implements TemplateHeadProcessor {
    private final HtmlService htmlService;

    @Override
    public Mono<Void> process(ITemplateContext context, IModel model,
        IElementModelStructureHandler structureHandler) {
        return htmlService.listEnabledInjectionsByPoint(HtmlInjection.InjectionPoint.HEADER)
            .doOnNext(htmlInjection -> {
                if (isContentTemplate(context)) {
                    model.add(
                        context.getModelFactory().createText(
                            htmlInjection.getSpec().getFragment()));
                }
            })
            .onErrorResume(e -> {
                log.error("HtmlHeadProcessor process failed", e);
                return Mono.empty();
            })
            .then();
    }
}
