package org.nazarius;

import io.helidon.config.Config;
import io.helidon.openapi.OpenApiFeature;
import io.helidon.security.Security;
import io.helidon.webserver.WebServerConfig;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.security.SecurityHttpFeature;
import org.nazarius.VovkDataInMemory.api.InMemoryEntityManager;
import org.nazarius.VovkORM.engine.manager.JdbcEntityManager;
import org.nazarius.config.EntityManagerFactory;
import org.nazarius.config.ServerApp;
import org.nazarius.model.Inspection;
import org.nazarius.repository.InspectionRepositoryDb;
import org.nazarius.repository.InspectionRepositoryInMemory;
import org.nazarius.service.InspectionService;
import org.nazarius.http.InspectionHttpService;

import static org.nazarius.config.SecurityConfig.createSecurity;

public class Main extends ServerApp {

    public static void main(String[] args) {
        new Main()
                .beforeStart(() -> System.out.println("Initializing database..."))
                .afterStart(server -> System.out.println("Server started at http://localhost:" + server.port()))
                .start();
    }

    @Override
    protected HttpRouting.Builder createRouting() {
        // Initialize service
        InspectionService inspectionService = createInspectionService();

        // Initialize routing builder
        HttpRouting.Builder routingBuilder = HttpRouting.builder();

        // Add security feature
        Security security = createSecurity();
        routingBuilder.addFeature(SecurityHttpFeature.create(security));

        // Register HTTP services
        routingBuilder.register(new InspectionHttpService(inspectionService));

        return routingBuilder;
    }

    /**
     * Enable OpenAPI feature
     */
    @Override
    protected void configureFeatures(WebServerConfig.Builder serverBuilder) {
        Config config = Config.create();
        serverBuilder.addFeature(
                OpenApiFeature.create(config.get("openapi"))
        );
    }

    @Override
    protected void registerExceptionHandlers(HttpRouting.Builder routingBuilder) {
        super.registerExceptionHandlers(routingBuilder);
    }

    @Override
    protected int getPort() {
        return 9300; // custom port for Inspection server
    }

    /**
     * --- Helper methods ---
     */

    private InspectionService createInspectionService() {
        // DB entity manager
        EntityManagerFactory.DatabaseConfig dbConfig = new EntityManagerFactory.DatabaseConfig(
                "jdbc:postgresql://localhost:5432/helidon_vovk_platform",
                "postgres",
                "root"
        );
        JdbcEntityManager jdbcEntityManager = EntityManagerFactory.db(dbConfig);

        // In-memory entity manager
        InMemoryEntityManager inMemoryEntityManager = EntityManagerFactory.inMemory();
        inMemoryEntityManager.register(Inspection.class);

        // Repositories
        InspectionRepositoryDb dbRepo = new InspectionRepositoryDb(jdbcEntityManager);
        InspectionRepositoryInMemory memoryRepo = new InspectionRepositoryInMemory(inMemoryEntityManager);

        // Service
        return new InspectionService(dbRepo, memoryRepo);
    }
}