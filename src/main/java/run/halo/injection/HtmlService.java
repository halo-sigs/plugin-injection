package run.halo.injection;

import reactor.core.publisher.Flux;

public interface HtmlService {

    Flux<HtmlInjection> listEnabledHeadInjections();

    Flux<HtmlInjection> listEnabledFooterInjections();
}
