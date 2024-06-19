package ru.neostudy.creditbank.deal.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neostudy.creditbank.deal.model.entity.Statement;

@Repository
public interface StatementRepository extends JpaRepository<Statement, UUID> {

  Statement getByStatementId(UUID uuid);
}
