package com.ll.gramgram.boundedContext.likeablePerson.service;

import com.ll.gramgram.base.rsData.RsData;
import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.instaMember.service.InstaMemberService;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import com.ll.gramgram.boundedContext.likeablePerson.repository.LikeablePersonRepository;
import com.ll.gramgram.boundedContext.member.entity.Member;
import com.ll.gramgram.standard.util.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeablePersonService {
    private final LikeablePersonRepository likeablePersonRepository;
    private final InstaMemberService instaMemberService;

    @Transactional
    public RsData<LikeablePerson> like(Member member, String username, int attractiveTypeCode) {
        if (member.hasConnectedInstaMember() == false) {
            return RsData.of("F-2", "먼저 본인의 인스타그램 아이디를 입력해야 합니다.");
        }

        if (member.getInstaMember().getUsername().equals(username)) {
            return RsData.of("F-1", "본인을 호감상대로 등록할 수 없습니다.");
        }

        InstaMember toInstaMember = instaMemberService.findByUsernameOrCreate(username).getData();

        if (isDuplicatedLikeablePerson(member.getInstaMember().getId(), toInstaMember.getId())) {
            if (!isLikeablePersonAttractiveTypeDiffer(member.getInstaMember().getId(), toInstaMember.getId(),
                    attractiveTypeCode)) {
                return RsData.of("F-3", "이미 중복된 호감사유의 대상이 있습니다.");
            }
            String attractiveTypeFrom = findByFromInstaMemberIdAndToInstaMemberId(member.getInstaMember().getId(),
                    toInstaMember.getId()).getAttractiveTypeDisplayName();
            LikeablePerson likeablePerson = updateLikeablePersonByAttractiveTypeCode(
                    member.getInstaMember().getId(),
                    toInstaMember.getId(),
                    attractiveTypeCode);
            likeablePersonRepository.save(likeablePerson);
            String attractiveTypeTo = likeablePerson.getAttractiveTypeDisplayName();
            return RsData.of("S-2", "(%s)에 대한 호감사유를 (%s)에서 (%s)으로 변경합니다."
                    .formatted(username, attractiveTypeFrom, attractiveTypeTo), likeablePerson);
        }

        if (isLikeablePersonSizeOver(member.getInstaMember().getId())) {
            return RsData.of("F-4", "10명 이상의 호감상대 등록은 불가합니다.");
        }

        LikeablePerson likeablePerson = LikeablePerson
                .builder()
                .fromInstaMember(member.getInstaMember()) // 호감을 표시하는 사람의 인스타 멤버
                .fromInstaMemberUsername(member.getInstaMember().getUsername()) // 중요하지 않음
                .toInstaMember(toInstaMember) // 호감을 받는 사람의 인스타 멤버
                .toInstaMemberUsername(toInstaMember.getUsername()) // 중요하지 않음
                .attractiveTypeCode(attractiveTypeCode) // 1=외모, 2=능력, 3=성격
                .build();

        likeablePersonRepository.save(likeablePerson); // 저장

        return RsData.of("S-1", "입력하신 인스타유저(%s)를 호감상대로 등록되었습니다."
                .formatted(username), likeablePerson);
    }

    @Transactional
    public RsData<LikeablePerson> deleteLike(Member member, long likeableId) {
        Optional<LikeablePerson> optionalLikeablePerson = likeablePersonRepository.findById(likeableId);
        if (optionalLikeablePerson.isEmpty()) {
            return RsData.of("F-1", "호감목록 삭제를 실패했습니다. (해당 ID를 조회할 수 없음)");
        }

        LikeablePerson likeablePerson = optionalLikeablePerson.get();
        long verifiedInstaMemberId = member.getInstaMember().getId();
        long likeablePersonFromInstaMemberId = likeablePerson.getFromInstaMember().getId();
        if (verifiedInstaMemberId != likeablePersonFromInstaMemberId) {
            return RsData.of("F-2",
                    "호감목록 삭제를 실패했습니다. (호감 표시한 목록과 로그인한 인스타 ID가 일치하지 않음");
        }

        likeablePersonRepository.deleteById(likeableId);

        return RsData.of("S-1", "호감목록 삭제완료(%d)".formatted(likeableId), likeablePerson);
    }

    public List<LikeablePerson> findByFromInstaMemberId(long fromInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);
    }

    // Optional<LikeablePerson> select
    private Optional<LikeablePerson> findOptionalLikeablePerson(long fromInstaMemberId, long toInstaMemberId) {
        return likeablePersonRepository.findByFromInstaMemberIdAndToInstaMemberId(fromInstaMemberId, toInstaMemberId);
    }

    // Optional에서 LikeablePerson 객체 반환
    private LikeablePerson findByFromInstaMemberIdAndToInstaMemberId(long fromInstaMemberId, long toInstaMemberId) {
        return findOptionalLikeablePerson(fromInstaMemberId, toInstaMemberId).get();
    }

    // Optional에서 LikeablePerson 객체 존재하는지 확인
    private boolean isDuplicatedLikeablePerson(long fromInstaMemberId, long toInstaMemberId) {
        return findOptionalLikeablePerson(fromInstaMemberId, toInstaMemberId).isPresent();
    }

    // Insta_Member의 호감목록 한계치 확인
    private boolean isLikeablePersonSizeOver(long fromInstaMemberId) {
        List<LikeablePerson> likeablePeople = likeablePersonRepository.findByFromInstaMemberId(fromInstaMemberId);

        return likeablePeople.size() >= Constants.MAXIMUN_LIKEABLEPERSON_COUNT;
    }

    // AttractiveTypeCode가 같은지 확인
    private boolean isLikeablePersonAttractiveTypeDiffer(long fromInstaMemberId, long toInstaMemberId,
                                                         int attractiveTypeCode) {
        LikeablePerson findLikeablePerson =
                findByFromInstaMemberIdAndToInstaMemberId(fromInstaMemberId, toInstaMemberId);
        int likeableAttractiveType = findLikeablePerson.getAttractiveTypeCode();

        return likeableAttractiveType != attractiveTypeCode;
    }

    // AttractiveTypeCode update
    private LikeablePerson updateLikeablePersonByAttractiveTypeCode(long fromInstaMemberId, long toInstaMemberId,
                                                                    int attractiveTypeCode) {
        LikeablePerson findLikeablePerson =
                findByFromInstaMemberIdAndToInstaMemberId(fromInstaMemberId, toInstaMemberId);
        findLikeablePerson.setAttractiveTypeCode(attractiveTypeCode);

        return findLikeablePerson;
    }
}