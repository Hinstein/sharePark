package com.hinstein.android.experiment.repository;

import com.hinstein.android.experiment.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.repository
 * @Author: Hinstein
 * @CreateTime: 2019-12-21 08:23
 * @Description:
 */
public interface SuggestionRepository extends JpaRepository<Suggestion, Integer> {


}
