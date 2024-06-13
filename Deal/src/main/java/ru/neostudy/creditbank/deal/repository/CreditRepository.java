package ru.neostudy.creditbank.deal.repository;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.neostudy.creditbank.deal.model.entity.Client;
import ru.neostudy.creditbank.deal.model.entity.Credit;

@Repository
public interface CreditRepository extends CrudRepository<Credit, UUID> {
}
