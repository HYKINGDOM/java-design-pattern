package org.fkjava.gift.repository;

import org.fkjava.gift.domain.Present;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentRepository extends JpaRepository<Present,String>, JpaSpecificationExecutor<Present> {
}
