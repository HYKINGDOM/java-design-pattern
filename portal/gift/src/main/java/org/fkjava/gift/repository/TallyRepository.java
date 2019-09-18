package org.fkjava.gift.repository;

import org.fkjava.gift.domain.Tally;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

@Repository
public interface TallyRepository extends JpaRepository<Tally,String>{
    List<Tally> findByNameIn(List<String> labels);
}
