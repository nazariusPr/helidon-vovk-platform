package org.nazarius.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.nazarius.VovkDataFiles.csv.CsvEntityManager;
import org.nazarius.VovkDataInMemory.api.InMemoryEntityManager;
import org.nazarius.VovkDataInMemory.api.Storage;
import org.nazarius.VovkDataInMemory.engine.manager.DefaultInMemoryEntityManager;
import org.nazarius.VovkDataInMemory.internal.InMemoryStorage;
import org.nazarius.VovkORM.engine.manager.JdbcEntityManager;

import javax.sql.DataSource;

public class EntityManagerFactory {

    /**
     * Initialize the database and return a ready-to-use JdbcEntityManager.
     */
    public static JdbcEntityManager db(DatabaseConfig dbConfig) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbConfig.url());
        config.setUsername(dbConfig.username());
        config.setPassword(dbConfig.password());
        config.setMaximumPoolSize(10);
        config.setPoolName("VovkORMPool");

        DataSource dataSource = new HikariDataSource(config);
        return new JdbcEntityManager(dataSource);
    }

    /**
     * Initialize CsvEntityManager for CSV files
     */
    public static CsvEntityManager csv() {
        return new CsvEntityManager();
    }

    /**
     * Initialize InMemoryEntityManager for working with in-memory storage.
     */
    public static InMemoryEntityManager inMemory() {
        Storage storage = new InMemoryStorage();
        return new DefaultInMemoryEntityManager(storage);
    }

    /**
     * Simple configuration holder for database connection
     */
    public record DatabaseConfig(String url, String username, String password) {
    }
}