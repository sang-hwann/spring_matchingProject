package com.project.matchingsystem.repository;

import com.project.matchingsystem.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long id);

    Optional<Category> findByName(String categoryName);

    Page<Category> findByParentIdIsNull(Pageable pageable);

    List<Category> findByParentIdIsNotNull();

    void deleteByParentId(Long parentId);

}
