package com.example.demo.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ELocation;
import com.example.demo.model.Location;

import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
    Optional<Location> findByCode(String code);

    boolean existsByCode(String code);

    List<Location> findByParentCode(String parentCode);

    List<Location> findByType(ELocation type);

    Optional<Location> findByName(String name);
}