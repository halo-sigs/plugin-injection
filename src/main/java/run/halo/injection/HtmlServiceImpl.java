package run.halo.injection;

import static run.halo.app.extension.index.query.QueryFactory.and;
import static run.halo.app.extension.index.query.QueryFactory.equal;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import run.halo.app.extension.ListOptions;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.extension.index.query.Query;

@Service
@RequiredArgsConstructor
public class HtmlServiceImpl implements HtmlService {
    private final ReactiveExtensionClient client;

    /**
     * 根据注入点获取正在启用的HtmlInjection 对象.
     */
    @Override
    public Flux<HtmlInjection> listEnabledInjectionsByPoint(
        HtmlInjection.InjectionPoint injectionPoint) {
        Query query = and(
            equal("spec.enabled", "true"),
            equal("spec.injectionPoint", injectionPoint.name())
        );

        ListOptions options = ListOptions.builder()
            .fieldQuery(query)
            .build();

        return client.listAll(HtmlInjection.class, options,
            Sort.by(Sort.Order.asc("metadata.name")));
    }
}

