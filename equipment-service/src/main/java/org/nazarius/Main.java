package org.nazarius;

import io.helidon.config.Config;
import io.helidon.openapi.OpenApiFeature;
import io.helidon.security.Security;
import io.helidon.webserver.WebServerConfig;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.security.SecurityHttpFeature;
import org.nazarius.VovkDataFiles.csv.CsvEntityManager;
import org.nazarius.VovkORM.engine.manager.JdbcEntityManager;
import org.nazarius.config.EntityManagerFactory;
import org.nazarius.config.ServerApp;
import org.nazarius.http.EquipmentHttpService;
import org.nazarius.repository.EquipmentRepositoryCsv;
import org.nazarius.repository.EquipmentRepositoryDb;
import org.nazarius.service.EquipmentService;

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
        // Initialize services
        EquipmentService equipmentService = createEquipmentService();

        // Initialize routing builder
        HttpRouting.Builder routingBuilder = HttpRouting.builder();

        // Add security feature
        Security security = createSecurity();
        routingBuilder.addFeature(SecurityHttpFeature.create(security));

        // Register HTTP services
        routingBuilder.register(new EquipmentHttpService(equipmentService));

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
        return 9200; // custom port for Equipment server
    }

    /**
     * --- Helper methods ---
     */

    private EquipmentService createEquipmentService() {
        Config config = Config.create();

        // Read DB config
        String url = config.get("db.url").asString().get();
        String username = config.get("db.username").asString().get();
        String password = config.get("db.password").asString().get();

        // Initialize entity managers
        JdbcEntityManager jdbcEntityManager = EntityManagerFactory.db(
                new EntityManagerFactory.DatabaseConfig(url, username, password)
        );
        CsvEntityManager csvEntityManager = EntityManagerFactory.csv();

        // Initialize repositories
        EquipmentRepositoryDb dbRepo = new EquipmentRepositoryDb(jdbcEntityManager);
        EquipmentRepositoryCsv csvRepo = new EquipmentRepositoryCsv(csvEntityManager);

        return new EquipmentService(dbRepo, csvRepo);
    }
}