package com.site.ota.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.site.ota.answer.Answer;
import com.site.ota.user.SiteUser;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    private List<Answer> answerList;
    
    @ManyToOne
    private SiteUser author;
    
    private LocalDateTime modifyDate;
    
    @ManyToMany
    Set<SiteUser> voter;
    @Column
    private String filename;//파일이름

    @Column
    private String filepath;//파일경로

    @Column
    private String rootHash; // 머클 트리의 루트 해시 값

    @Column
    private String fileHash; // 현재 올라간 파일의 해시 값

}