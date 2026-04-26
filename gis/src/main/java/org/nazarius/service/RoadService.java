package org.nazarius.service;

import org.nazarius.VovkDataCore.utils.Page;
import org.nazarius.VovkDataCore.utils.Pageable;
import org.nazarius.dto.RoadDto;
import org.nazarius.mapper.RoadMapper;
import org.nazarius.model.Road;
import org.nazarius.repository.RoadRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RoadService {

    private final RoadRepository repository;

    public RoadService(RoadRepository repository) {
        this.repository = repository;
    }

    // Fetch raw Road entity by ID
    public Road getEntityById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    // Fetch raw Road entity by name
    public Road getEntityByName(String name) {
        return repository.findByName(name).orElse(null);
    }

    // Fetch all roads as DTOs
    public List<RoadDto> getAllRoads() {
        List<Road> roads = repository.findAll();
        if (roads == null || roads.isEmpty()) {
            return Collections.emptyList();
        }
        return roads.stream()
                .map(RoadMapper::toDto)
                .collect(Collectors.toList());
    }

    // Fetch paged roads as DTOs
    public Page<RoadDto> readPage(Pageable pageable) {
        // Get Page<Road> from repository
        Page<Road> roadPage = repository.readPage(pageable);

        // Convert entities to DTOs
        List<RoadDto> dtoList = roadPage.getElems().stream()
                .map(RoadMapper::toDto)
                .collect(Collectors.toList());

        // Return a new Page<RoadDto>
        return new Page<>(
                dtoList,
                roadPage.getCurrentPage(),
                roadPage.getTotalElems(),
                roadPage.getTotalPages()
        );
    }

    // Fetch Road DTO by ID
    public RoadDto getRoadById(Integer id) {
        Road road = getEntityById(id);
        return road != null ? RoadMapper.toDto(road) : null;
    }

    // Fetch Road DTO by name
    public RoadDto getRoadByName(String name) {
        Road road = getEntityByName(name);
        return road != null ? RoadMapper.toDto(road) : null;
    }
}