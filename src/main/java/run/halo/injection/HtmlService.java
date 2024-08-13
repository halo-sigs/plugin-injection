package run.halo.injection;

import reactor.core.publisher.Flux;

public interface HtmlService {

    Flux<HtmlInjection> listEnabledInjectionsByPoint(HtmlInjection.InjectionPoint injectionPoint);
}
