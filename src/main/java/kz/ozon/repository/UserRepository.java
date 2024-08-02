package kz.ozon.repository;

import kz.ozon.model.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT COUNT(u) > 0 " +
            "FROM User u " +
            "WHERE u.email = :email")
    boolean existsByEmail(String email);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.id IN :users")
    List<User> findAllByIdIn(List<Long> users, PageRequest pageable);

    @Query("SELECT u " +
            "FROM User u " +
            "WHERE u.username = :name")
    Optional<User> findByName(String name);

    @Query("SELECT COUNT(u) > 0 " +
            "FROM User u " +
            "WHERE u.username = :username")
    boolean existsByUsername(String username);

}