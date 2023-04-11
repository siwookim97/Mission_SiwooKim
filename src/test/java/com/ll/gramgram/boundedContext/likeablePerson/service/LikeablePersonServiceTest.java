package com.ll.gramgram.boundedContext.likeablePerson.service;

import static org.assertj.core.api.Assertions.*;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;


@SpringBootTest
class LikeablePersonServiceTest {

    @Autowired
    private LikeablePersonService likeablePersonService;

    @Autowired
    private LikeablePersonRepository likeablePersonRepository;

    private LikeablePerson likeablePerson;

    private final InstaMember instaMemberFrom = InstaMember.builder()
            .createDate(LocalDateTime.now())
            .modifyDate(LocalDateTime.now())
            .username("Insta_name1")
            .gender("M")
            .build();
    private final InstaMember instaMemberTo = InstaMember.builder()
            .createDate(LocalDateTime.now())
            .modifyDate(LocalDateTime.now())
            .username("Insta_name2")
            .gender("F")
            .build();
    private final LikeablePerson likeablePerson1 = LikeablePerson.builder()
            .createDate(LocalDateTime.now())
            .modifyDate(LocalDateTime.now())
            .fromInstaMember(instaMemberFrom)
            .toInstaMember(instaMemberTo)
            .attractiveTypeCode(1)
            .build();

//    @BeforeEach
//    void setUp() {
//        likeablePersonRepository.save(likeablePerson1);
//    }

    @Test
    @DisplayName("중복 호감표시 불가")
    void failToAddLikeablePerson() {
        // when
        RsData<LikeablePerson> likeablePersonRsData1 =
                likeablePersonService.like(new Member(), "Insta_name2", 1);
        RsData<LikeablePerson> likeablePersonRsData2 =
                likeablePersonService.like(new Member(), "Insta_name2", 1);

        // then
        assertThat(likeablePersonRsData2.isFail()).isFalse();
    }
}