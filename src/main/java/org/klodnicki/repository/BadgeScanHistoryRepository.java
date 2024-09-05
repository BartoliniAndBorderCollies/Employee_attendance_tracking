package org.klodnicki.repository;

import org.klodnicki.model.entity.Badge;
import org.klodnicki.model.entity.BadgeScanHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BadgeScanHistoryRepository extends CrudRepository<BadgeScanHistory, Long> {

    Optional<BadgeScanHistory> findFirstByBadgeOrderByTimeStampDesc(Badge badge);

    Optional<BadgeScanHistory> findByBadge(Badge badge);
}
