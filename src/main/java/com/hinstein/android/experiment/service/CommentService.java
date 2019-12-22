package com.hinstein.android.experiment.service;

import com.hinstein.android.experiment.entity.Comment;
import com.hinstein.android.experiment.entity.Order;
import com.hinstein.android.experiment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @BelongsProject: androidexperiment
 * @BelongsPackage: com.hinstein.android.experiment.service
 * @Author: Hinstein
 * @CreateTime: 2019-12-20 10:11
 * @Description:
 */
@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    public void save(String username, String content, int parkId, String userHeadPhoto) {
        Comment comment = new Comment();
        comment.setUsername(username);
        comment.setContent(content);
        comment.setParkId(parkId);
        comment.setUserHeadPhoto(userHeadPhoto);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        comment.setTime(df.format(new Date()));
        commentRepository.save(comment);
    }


    @Transactional(rollbackOn = Exception.class)
    public Page<Comment> findByParkId(int userId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        PageRequest pageable = PageRequest.of(0, 100000, sort);
        return commentRepository.findByParkId(userId, pageable);
    }
}
