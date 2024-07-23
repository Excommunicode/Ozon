package kz.ozon.repository;

import kz.ozon.model.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.id IN :users")
    List<User> findAllByIdIn(List<Long> users, PageRequest pageable);
}