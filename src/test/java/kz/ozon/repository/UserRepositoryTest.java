package kz.ozon.repository;

import kz.ozon.model.User;
import lombok.RequiredArgsConstructor;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    private final TestEntityManager testEntityManager;
    private final UserRepository userRepository;

    private final PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));

    private User user;
    private List<User> users;

    @BeforeEach
    void setUp() {
        user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .create();
        users = Stream.generate(() -> Instancio.of(User.class)
                        .ignore(Select.field(User::getId))
                        .create())
                .limit(10)
                .toList();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        testEntityManager.clear();
    }

    @Test
    void existsByEmail() {
        testEntityManager.persist(user);
        boolean existsByEmail = userRepository.existsByEmail(user.getEmail());
        assertTrue(existsByEmail);
    }

    @Test
    void findAllByIdIn() {
        List<User> userList = userRepository.saveAll(users);

        List<Long> longs = userList.stream()
                .map(User::getId)
                .toList();

        List<User> allByIdIn = userRepository.findAllByIdIn(longs, pageable);
        userList.sort(Comparator.comparing(User::getId).reversed());
        assertEquals(allByIdIn, userList);
    }
}