package kz.ozon.repository;

import kz.ozon.model.Clothes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ClothesRepository extends JpaRepository<Clothes, Long>
//        , QuerydslPredicateExecutor<Clothes>
{
}