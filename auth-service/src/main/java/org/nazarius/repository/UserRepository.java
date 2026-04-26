package org.nazarius.repository;

import org.nazarius.VovkDataCore.utils.Page;
import org.nazarius.VovkDataCore.utils.Pageable;
import org.nazarius.VovkORM.engine.manager.JdbcEntityManager;
import org.nazarius.VovkORM.sql.builder.Select;
import org.nazarius.model.User;

import java.util.List;
import java.util.Optional;

import static org.nazarius.VovkDataCore.dsl.Conditions.eq;
import static org.nazarius.VovkORM.sql.builder.Select.select;
import static org.nazarius.VovkORM.sql.common.Where.column;

public class UserRepository {
    private final JdbcEntityManager manager;

    public UserRepository(JdbcEntityManager manager) {
        this.manager = manager;
    }

    public Optional<User> findById(Integer id) {
        return Optional.ofNullable(manager.findById(id, User.class));
    }

    public Optional<User> findByUsername(String username) {
        final String USERNAME = User.USERNAME();
        List<User> users = manager.query(User.class)
                .where(eq(USERNAME, username))
                .list();

        if (users.isEmpty()) {
            return Optional.empty();
        } else if (users.size() > 1) {
            throw new IllegalStateException("Multiple users found with username: " + username);
        } else {
            return Optional.of(users.getFirst());
        }
    }

    public List<User> findAll() {
        return manager.findAll(User.class);
    }

    public Page<User> readPage(Pageable pageable) {
        return manager.readPage(pageable, User.class);
    }

    public int save(User user) {
        return manager.save(user, User.class);
    }

    public int update(User user) {
        return manager.update(user, User.class);
    }

    public int deleteById(Integer id) {
        return manager.deleteById(id, User.class);
    }
}