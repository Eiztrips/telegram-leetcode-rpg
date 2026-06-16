package dev.eiztrips.telegramleetcoderpg.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = "dev.eiztrips.telegramleetcoderpg")
public class ArchitectureLayersTest {

    /*
        В проекте используется HEXAGONAL архитектура,
        как общий подход, но в тесте ONION, т. к.
        он концептуально расширяет HEXAGONAL
     */
    @ArchTest
    static final ArchRule check_architecture_layers = onionArchitecture()
            .domainModels("dev.eiztrips.telegramleetcoderpg.domain.model..")
            .domainServices("dev.eiztrips.telegramleetcoderpg.domain.service..")
            .applicationServices("dev.eiztrips.telegramleetcoderpg.application..")

            .adapter("schedule", new String[]{
                    "dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.scheduler..",
                    "dev.eiztrips.telegramleetcoderpg.infrastructure.configuration.."
            })
            .adapter("telegram", new String[]{
                    "dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.inbound.telegram..",
                    "dev.eiztrips.telegramleetcoderpg.infrastructure.configuration.."
            })
            .adapter("leetcode", new String[]{
                    "dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.leetcode..",
                    "dev.eiztrips.telegramleetcoderpg.infrastructure.configuration.."
            })
            .adapter("database", new String[]{
                    "dev.eiztrips.telegramleetcoderpg.infrastructure.adapters.outbound.persistence..",
                    "dev.eiztrips.telegramleetcoderpg.infrastructure.configuration.."
            })

            .allowEmptyShould(true);
}