package ru.neostudy.creditbank.deal.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neostudy.creditbank.deal.model.entity.Credit;

@Repository
public interface CreditRepository extends JpaRepository<Credit, UUID> {
}
