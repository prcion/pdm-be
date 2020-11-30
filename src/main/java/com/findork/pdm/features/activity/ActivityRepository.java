package com.findork.pdm.features.activity;

import org.hibernate.sql.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    @Query("SELECT a from Activity a where a.user.id = :userId and a.name LIKE CONCAT('%', :name, '%') OR :name IS NULL")
    Page<Activity> findAllByUserId(@Param("userId") Long userId, Pageable pageable,@Param("name") String name);
}
