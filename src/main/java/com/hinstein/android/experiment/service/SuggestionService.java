package com.hinstein.android.experiment.service;

import com.hinstein.android.experiment.entity.Suggestion;
import com.hinstein.android.experiment.entity.Test;
import com.hinstein.android.experiment.repository.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.service
 * @Author: Hinstein
 * @CreateTime: 2019-12-21 08:24
 * @Description:
 */
@Service
public class SuggestionService {

    @Autowired
    private SuggestionRepository suggestionRepository;
    public void save(int userId ,String suggestion) {
        Suggestion suggestion1 = new Suggestion();
        suggestion1.setUserId(userId);
        suggestion1.setContent(suggestion);
        suggestionRepository.save(suggestion1);
    }
}
