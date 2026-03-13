package org.nazarius.config;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;

public final class GeneralConfig {

    private static Config config;

    private GeneralConfig() {
    }

    public static Config getConfig() {
        if (config == null) {
            config = Config.builder()
                    .addSource(ConfigSources.environmentVariables())
                    .addSource(ConfigSources.systemProperties())
                    .addSource(ConfigSources.classpath("application.properties"))
                    .build();
        }
        return config;
    }
}