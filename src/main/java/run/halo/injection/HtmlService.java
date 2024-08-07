package run.halo.injection;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import run.halo.app.extension.ListOptions;
import run.halo.app.extension.ReactiveExtensionClient;
import run.halo.app.extension.index.query.QueryFactory;

@Service
@RequiredArgsConstructor
public class HtmlService {
    private final ReactiveExtensionClient client;

    /**
     * 获取所有启用的 HtmlInjection 对象.
     */
    public Flux<HtmlInjection> getAllEnabledInjections() {
        ListOptions options = ListOptions.builder()
            // 在这里添加查询条件，只获取正在启用的代码
            .fieldQuery(QueryFactory.equal("spec.enabled", "true"))
            .build();
        return client.listAll(HtmlInjection.class, options,
            Sort.by(Sort.Order.asc("metadata.name")));
    }

    /**
     * 获取启用的注入点为 HEAD 的 HtmlInjection 对象.
     */
    public Flux<HtmlInjection> getHeadEnabledInjections() {
        return getAllEnabledInjections()
            .filter(htmlInjection ->
                htmlInjection.getSpec().getInjectionPoint() == HtmlInjection.InjectionPoint.HEADER);
    }

    /**
     * 获取启用的注入点为 FOOTER 的 HtmlInjection 对象.
     */
    public Flux<HtmlInjection> getFooterEnabledInjections() {
        return getAllEnabledInjections()
            .filter(htmlInjection ->
                htmlInjection.getSpec().getInjectionPoint() == HtmlInjection.InjectionPoint.FOOTER);
    }
}

