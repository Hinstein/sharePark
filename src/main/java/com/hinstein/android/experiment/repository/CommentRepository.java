package com.hinstein.android.experiment.repository;

import com.hinstein.android.experiment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.repository
 * @Author: Hinstein
 * @CreateTime: 2019-12-20 10:11
 * @Description:
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    @Query(value = "select p from Comment p where p.parkId =?1")
    Page<Comment> findByParkId(int parkId, PageRequest pageable);
}
