package net.nighthawk.seekersconnect_backend.repo;


import net.nighthawk.seekersconnect_backend.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DocumentRepo extends JpaRepository<Document, String> {
    List<Document> findByUserId(String userId);
}
