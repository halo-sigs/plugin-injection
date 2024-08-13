package run.halo.injection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import reactor.core.publisher.Mono;
import run.halo.app.theme.dialect.TemplateFooterProcessor;

@Slf4j
@Component
@RequiredArgsConstructor
public class HtmlFooterProcessor extends AbstractHtmlProcessor implements TemplateFooterProcessor {
    private final HtmlService htmlService;

    @Override
    public Mono<Void> process(ITemplateContext context, IProcessableElementTag tag,
        IElementTagStructureHandler structureHandler, IModel model) {
        return htmlService.listEnabledInjectionsByPoint(HtmlInjection.InjectionPoint.FOOTER)
            .doOnNext(htmlInjection -> {
                if (isContentTemplate(context)) {
                    model.add(
                        context.getModelFactory().createText(
                            htmlInjection.getSpec().getFragment()));
                }
            })
            .onErrorResume(e -> {
                log.error("HtmlFooterProcessor process failed", e);
                return Mono.empty();
            })
            .then();
    }
}
