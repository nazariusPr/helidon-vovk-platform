package org.nazarius;

import io.helidon.config.Config;
import io.helidon.openapi.OpenApiFeature;
import io.helidon.security.Security;
import io.helidon.webserver.WebServerConfig;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.security.SecurityHttpFeature;
import org.nazarius.VovkORM.engine.manager.JdbcEntityManager;
import org.nazarius.config.EntityManagerFactory;
import org.nazarius.config.ServerApp;
import org.nazarius.repository.RoadRepository;
import org.nazarius.service.RoadService;
import org.nazarius.http.RoadHttpService;

import static org.nazarius.config.GeneralConfig.getConfig;
import static org.nazarius.config.SecurityConfig.createSecurity;

public class RoadMain extends ServerApp {

    public static void main(String[] args) {
        new RoadMain()
                .beforeStart(() -> System.out.println("Initializing Road database..."))
                .afterStart(server -> System.out.println("Road server started at http://localhost:" + server.port()))
                .start();
    }

    @Override
    protected HttpRouting.Builder createRouting() {
        // Initialize service
        RoadService roadService = createRoadService();

        // Routing builder
        HttpRouting.Builder routingBuilder = HttpRouting.builder();

        // Security
        Security security = createSecurity();
        routingBuilder.addFeature(SecurityHttpFeature.create(security));

        // Register Road HTTP service
        routingBuilder.register(new RoadHttpService(roadService));

        return routingBuilder;
    }

    @Override
    protected void configureFeatures(WebServerConfig.Builder serverBuilder) {
        Config config = getConfig();
        serverBuilder.addFeature(
                OpenApiFeature.create(config.get("openapi"))
        );
    }

    @Override
    protected int getPort() {
        return 9400; // separate port for Road server
    }

    /**
     * --- Helper methods ---
     */
    private RoadService createRoadService() {
        Config config = getConfig();

        // JDBC entity manager for DB persistence
        String url = config.get("db.url").asString().get();
        String username = config.get("db.username").asString().get();
        String password = config.get("db.password").asString().get();
        JdbcEntityManager jdbcEntityManager = EntityManagerFactory.db(
                new EntityManagerFactory.DatabaseConfig(url, username, password)
        );

        // Repositories
        RoadRepository roadRepository = new RoadRepository(jdbcEntityManager);

        // Service
        return new RoadService(roadRepository);
    }
}