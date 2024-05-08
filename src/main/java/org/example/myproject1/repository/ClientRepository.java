package org.example.myproject1.repository;

import org.example.myproject1.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findClientById(Long id);
}
