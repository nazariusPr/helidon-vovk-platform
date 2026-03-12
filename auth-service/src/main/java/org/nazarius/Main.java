package org.nazarius;

import io.helidon.config.Config;
import io.helidon.openapi.OpenApiFeature;
import io.helidon.security.Security;
import io.helidon.webserver.WebServerConfig;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.security.SecurityHttpFeature;
import org.mindrot.jbcrypt.BCrypt;
import org.nazarius.VovkORM.engine.manager.JdbcEntityManager;
import org.nazarius.config.EntityManagerFactory;
import org.nazarius.config.ServerApp;
import org.nazarius.http.AuthHttpService;
import org.nazarius.model.User;
import org.nazarius.repository.UserRepository;
import org.nazarius.service.AuthService;
import org.nazarius.service.JwtService;
import org.nazarius.service.UserService;
import org.nazarius.http.UserHttpService;

import static org.nazarius.config.SecurityConfig.createSecurity;

public class Main extends ServerApp {

    public static void main(String[] args) {
        // Instantiate Main and configure hooks
        new Main()
                .beforeStart(() -> System.out.println("Initializing database and demo users..."))
                .afterStart(server -> System.out.println("Server started at http://localhost:" + server.port()))
                .start(); // this will call createRouting() internally
    }

    @Override
    protected HttpRouting.Builder createRouting() {
        // Initialize DB, repository, and services
        UserService userService = createUserService();
        JwtService jwtService = createJwtService();
        AuthService authService = new AuthService(userService, jwtService);

        // Initialize routing builder
        HttpRouting.Builder routingBuilder = HttpRouting.builder();

        // Add security feature
        Security security = createSecurity();
        routingBuilder.addFeature(SecurityHttpFeature.create(security));

        // Register HTTP services
        // UserHttpService needs UserService
        routingBuilder.register("/users", new UserHttpService(userService));
        // AuthHttpService needs AuthService
        routingBuilder.register("/auth", new AuthHttpService(authService));

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

    /**
     * Override centralized exception registration.
     * Currently, calls the parent's default handlers.
     * Can be extended to add more server-specific exception handling.
     */
    @Override
    protected void registerExceptionHandlers(HttpRouting.Builder routingBuilder) {
        super.registerExceptionHandlers(routingBuilder);
    }

    /**
     * Override default port
     */
    @Override
    protected int getPort() {
        return 9100; // custom port for User server
    }

    /**
     * --- Helper methods ---
     */

    private UserService createUserService() {
        Config config = Config.create();

        // Read DB config
        String url = config.get("db.url").asString().get();
        String username = config.get("db.username").asString().get();
        String password = config.get("db.password").asString().get();

        // Initialize DB & repository
        JdbcEntityManager entityManager = EntityManagerFactory.db(
                new EntityManagerFactory.DatabaseConfig(url, username, password)
        );
        UserRepository userRepository = new UserRepository(entityManager);

        // Populate demo users
        populateDemoUsers(userRepository);

        return new UserService(userRepository);
    }

    private JwtService createJwtService() {
        return new JwtService();
    }

    private void populateDemoUsers(UserRepository userRepository) {
        addUserIfNotExists(userRepository,
                new User(null, "alice", BCrypt.hashpw("password123", BCrypt.gensalt()), "ADMIN"));
        addUserIfNotExists(userRepository,
                new User(null, "bob", BCrypt.hashpw("secret456", BCrypt.gensalt()), "VIEWER"));
        addUserIfNotExists(userRepository,
                new User(null, "charlie", BCrypt.hashpw("mypassword", BCrypt.gensalt()), "INSPECTOR"));
    }

    private void addUserIfNotExists(UserRepository userRepository, User user) {
        if (userRepository.findById(user.getId()).isPresent()) return;
        if (userRepository.findByUsername(user.getUsername()).isPresent()) return;

        userRepository.save(user);
        System.out.println("Added demo user: " + user.getUsername());
    }
}