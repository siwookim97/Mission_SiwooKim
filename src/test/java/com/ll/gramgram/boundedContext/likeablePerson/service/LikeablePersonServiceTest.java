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
            .id(1000L)
            .username("InstaName1")
            .gender("M")
            .build();

    private final Member member = Member
            .builder()
            .providerTypeCode("GRAMGRAM")
            .username("TestMember")
            .password("1234")
            .instaMember(instaMemberFrom)
            .build();

    private final InstaMember instaMemberTo = InstaMember.builder()
            .id(1001L)
            .username("InstaName2")
            .gender("F")
            .build();

    private final LikeablePerson likeablePerson1 = LikeablePerson.builder()
            .fromInstaMember(instaMemberFrom)
            .fromInstaMemberUsername(instaMemberFrom.getUsername())
            .toInstaMember(instaMemberTo)
            .toInstaMemberUsername(instaMemberTo.getUsername())
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
                likeablePersonService.like(member, "Insta_name2", 1);
        RsData<LikeablePerson> likeablePersonRsData2 =
                likeablePersonService.like(member, "Insta_name2", 1);
        System.out.println(likeablePersonRsData1.getMsg());
        System.out.println(likeablePersonRsData2.getMsg());

        // then
        assertThat(likeablePersonRsData2.isFail()).isTrue();
    }
}