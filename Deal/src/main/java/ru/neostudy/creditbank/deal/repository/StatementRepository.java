package ru.neostudy.creditbank.deal.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.neostudy.creditbank.deal.model.entity.Statement;

@Repository
public interface StatementRepository extends CrudRepository<Statement, UUID> {

  Statement getByStatementId(UUID uuid);
}
