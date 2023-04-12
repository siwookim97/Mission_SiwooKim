package com.ll.gramgram.boundedContext.likeablePerson.service;

import static org.assertj.core.api.Assertions.*;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.boundedContext.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LikeablePersonServiceTest {

    @Autowired
    private LikeablePersonService likeablePersonService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("중복 호감대상 실패")
    @Order(1)
    void insertFailDuplicatedInstaMember() {
        // when
        Member member = memberRepository.findByUsername("user3").get();
        RsData<LikeablePerson> likeablePersonRsData =
                likeablePersonService.like(member, "insta_user100", 2);

        // then
        System.out.println(likeablePersonRsData.getMsg());
        assertThat(likeablePersonRsData.getResultCode()).startsWith("F-3");
        assertThat(likeablePersonRsData.isFail()).isTrue();
    }

    @Test
    @DisplayName("호감목록 10명 초과 실패")
    @Order(2)
    void insertFailLimitLikeablePerson() {
        // when
        Member member = memberRepository.findByUsername("user3").get();
        RsData<LikeablePerson> likeablePersonRsData =
                likeablePersonService.like(member, "insta_user200", 1);

        // then
        System.out.println(likeablePersonRsData.getMsg());
        assertThat(likeablePersonRsData.getResultCode()).startsWith("F-4");
        assertThat(likeablePersonRsData.isFail()).isTrue();
    }

    @Test
    @DisplayName("호감코드 변경 성공")
    @Order(3)
    void updateSuccess() {
        // when
        Member member = memberRepository.findByUsername("user3").get();
        RsData<LikeablePerson> likeablePersonRsData =
                likeablePersonService.like(member, "insta_user4", 2);

        // then
        System.out.println(likeablePersonRsData.getMsg());
        assertThat(likeablePersonRsData.getResultCode()).startsWith("S-2");
        assertThat(likeablePersonRsData.isSuccess()).isTrue();
    }
}