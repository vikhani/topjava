package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional()
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    List<Meal> findAllByUserId(int userId, Sort sort);

    @Transactional
    @Modifying
    @Query(name = Meal.GET_BETWEEN)
    List<Meal> getBetweenInclusive(@Param("startDateTime") LocalDateTime startDateTime,
                            @Param("endDateTime") LocalDateTime endDateTime,
                            @Param("userId") int userId);
}
