package com.bank.history.endpoints.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import java.time.LocalTime;

@Component
public class TotalHistoryInfoContributor implements InfoContributor {

    private final Environment environment;

    @Autowired
    public TotalHistoryInfoContributor(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("Название", environment.getProperty("spring.application.name"));
        builder.withDetail("ArtifactId", environment.getProperty("spring.application.artifactId"));
        builder.withDetail("Время запуска", LocalTime.now());
        builder.withDetail("Версия приложения", environment.getProperty("spring.application.version"));
        builder.withDetail("Context-path", environment.getProperty("server.servlet.context-path"));
    }
}