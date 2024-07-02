package run.halo.injection;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import run.halo.app.extension.AbstractExtension;
import run.halo.app.extension.GVK;

@Data
@EqualsAndHashCode(callSuper = true)
@GVK(group = "theme.halo.run",
    version = "v1alpha1",
    kind = "HtmlInjection",
    plural = "htmlInjections",
    singular = "htmlInjection")
public class HtmlInjection extends AbstractExtension {

    @Schema(requiredMode = REQUIRED)
    private Spec spec;

    @Data
    public static class Spec {
        @Schema(description = "The name of the code snippet", maxLength = 100)
        private String name;

        @Schema(description = "The description of the code snippet", maxLength = 500)
        private String description;

        @Schema(description = "The content of the HTML", maxLength = 5000)
        private String fragment;

        @Schema(description = "where to inject", allowableValues = {"HEADER", "FOOTER"})
        private InjectionPoint injectionPoint;

        @Schema(description = "The pages where the code snippet should be injected")
        private String[] targetPages;

        @Schema(description = "Whether the code snippet is enabled")
        private boolean isEnabled;
    }

    public enum InjectionPoint {
        HEADER, FOOTER
    }
}