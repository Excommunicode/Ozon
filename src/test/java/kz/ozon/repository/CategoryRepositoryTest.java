package kz.ozon.repository;

import kz.ozon.model.Category;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {
    private final CategoryRepository categoryRepository;
    private final TestEntityManager testEntityManager;

    private Category category;

    @BeforeEach
    void setUp() {
        category = Instancio.of(Category.class)
                .ignore(Select.field(Category::getId))
                .create();
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
        testEntityManager.clear();
    }

    @Test
    void existsByName() {

        categoryRepository.save(category);

        boolean existsByName = categoryRepository.existsByName(category.getName());
        assertTrue(existsByName);
    }
}