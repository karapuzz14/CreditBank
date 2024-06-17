package ru.neostudy.creditbank.deal.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.neostudy.creditbank.deal.model.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
}
