package org.klodnicki.repository;

import org.klodnicki.model.entity.Badge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BadgeRepository extends CrudRepository<Badge, Long> {

    Optional<Badge> findByBadgeNumber(String badgeNumber);
}
