package org.nazarius.repository;

import org.nazarius.VovkDataCore.utils.Page;
import org.nazarius.VovkDataCore.utils.Pageable;
import org.nazarius.VovkORM.engine.manager.JdbcEntityManager;
import org.nazarius.VovkORM.sql.builder.Select;
import org.nazarius.model.Road;

import java.util.List;
import java.util.Optional;

import static org.nazarius.VovkORM.sql.builder.Select.select;
import static org.nazarius.VovkORM.sql.common.Where.column;

public class RoadRepository {

    private final JdbcEntityManager manager;

    public RoadRepository(JdbcEntityManager manager) {
        this.manager = manager;
    }

    public Optional<Road> findById(Integer id) {
        return Optional.ofNullable(manager.findById(id, Road.class));
    }

    public Optional<Road> findByName(String name) {
        Select select = select()
                .from("roads")
                .where(column("name").eq(name));

        List<Road> roads = manager.find(select, Road.class);

        if (roads.isEmpty()) return Optional.empty();
        else if (roads.size() > 1) throw new IllegalStateException("Multiple roads found with name: " + name);
        else return Optional.of(roads.get(0));
    }

    public List<Road> findAll() {
        return manager.findAll(Road.class);
    }

    public Page<Road> readPage(Pageable pageable) {
        return manager.readPage(pageable, Road.class);
    }
}