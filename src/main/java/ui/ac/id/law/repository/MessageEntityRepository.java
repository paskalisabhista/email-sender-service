package ui.ac.id.law.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ui.ac.id.law.model.MessageEntity;

@Repository
public interface MessageEntityRepository extends JpaRepository<MessageEntity, String> {
}
