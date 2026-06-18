package org.aulaawspleno.rds.repository;

import org.aulaawspleno.sqs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CepRepository extends JpaRepository<User, Long> {
}
