package kz.ozon.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kz.ozon.dto.product.ClothesDto;
import kz.ozon.enums.ClothesSold;
import kz.ozon.enums.ClothingSize;
import kz.ozon.enums.Gender;
import kz.ozon.enums.SortClothes;
import kz.ozon.mapper.ClothesMapper;
import kz.ozon.model.Clothes;
import kz.ozon.repository.ClothesRepository;
import kz.ozon.service.api.ClothesPublicService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
public class ClothesPublicServiceImpl implements ClothesPublicService {
    private final ClothesRepository clothesRepository;
    private final ClothesMapper clothesMapper;
    private final EntityManager entityManager;

    @Override
    public List<ClothesDto> findAllByrequest(
            String text, List<Long> users,
            List<Long> categoryIds, SortClothes sortClothes, ClothesSold clothesSold,
            List<String> colors, List<ClothingSize> clothingSizes, String material,
            Gender gender, int from, int size) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Clothes> criteriaQuery = criteriaBuilder.createQuery(Clothes.class);
        Root<Clothes> clothes = criteriaQuery.from(Clothes.class);
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(text) && !text.trim().isEmpty()) {
            String textToLowerCase = "%" + text.toLowerCase() + "%";
            Predicate byName = criteriaBuilder.like(criteriaBuilder.lower(clothes.get("name")), textToLowerCase);
            Predicate byDescription = criteriaBuilder.like(criteriaBuilder.lower(clothes.get("description")), textToLowerCase);
            Predicate byNameOrDescription = criteriaBuilder.or(byName, byDescription);
            predicates.add(byNameOrDescription);
        }
        if (Objects.nonNull(users) && !users.isEmpty()) {
            Predicate byUsers = clothes.get("user").get("id").in(users);
            predicates.add(byUsers);
        }
        if (Objects.nonNull(categoryIds) && !categoryIds.isEmpty()) {
            Predicate byCategory = clothes.get("category").get("id").in(categoryIds);
            predicates.add(byCategory);
        }

        return null;
    }
}
