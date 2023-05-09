package com.ll.gramgram.boundedContext.instaMember.entity;

import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class InstaMember extends InstaMemberBase {
    @Setter
    @Column(unique = true)
    private String username;

    @Setter
    @Column(unique = true)
    private String oauthId;

    @Setter
    private String accessToken;

    @OneToMany(mappedBy = "fromInstaMember", cascade = {CascadeType.ALL})
    @OrderBy("id desc") // 정렬
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default // @Builder 가 있으면 ` = new ArrayList<>();` 가 작동하지 않는다. 그래서 이걸 붙여야 한다.
    private List<LikeablePerson> fromLikeablePeople = new ArrayList<>();

    @OneToMany(mappedBy = "toInstaMember", cascade = {CascadeType.ALL})
    @OrderBy("id desc") // 정렬
    @LazyCollection(LazyCollectionOption.EXTRA)
    @Builder.Default // @Builder 가 있으면 ` = new ArrayList<>();` 가 작동하지 않는다. 그래서 이걸 붙여야 한다.
    private List<LikeablePerson> toLikeablePeople = new ArrayList<>();

    public void addFromLikeablePerson(LikeablePerson likeablePerson) {
        fromLikeablePeople.add(0, likeablePerson);
    }

    public void addToLikeablePerson(LikeablePerson likeablePerson) {
        toLikeablePeople.add(0, likeablePerson);
    }

    public void removeFromLikeablePerson(LikeablePerson likeablePerson) {
        fromLikeablePeople.removeIf(e -> e.equals(likeablePerson));
    }

    public void removeToLikeablePerson(LikeablePerson likeablePerson) {
        toLikeablePeople.removeIf(e -> e.equals(likeablePerson));
    }

    public String getGenderDisplayName() {
        return switch (gender) {
            case "W" -> "여성";
            default -> "남성";
        };
    }

    public String getGenderDisplayNameWithIcon() {
        return switch (gender) {
            case "W" -> "<i class=\"fa-solid fa-person-dress\"></i>";
            default -> "<i class=\"fa-solid fa-person\"></i>";
        } + "&nbsp;" + getGenderDisplayName();
    }

    public void increaseLikesCount(String gender, int attractiveTypeCode) {
        if (gender.equals("W") && attractiveTypeCode == 1) likesCountByGenderWomanAndAttractiveTypeCode1++;
        if (gender.equals("W") && attractiveTypeCode == 2) likesCountByGenderWomanAndAttractiveTypeCode2++;
        if (gender.equals("W") && attractiveTypeCode == 3) likesCountByGenderWomanAndAttractiveTypeCode3++;
        if (gender.equals("M") && attractiveTypeCode == 1) likesCountByGenderManAndAttractiveTypeCode1++;
        if (gender.equals("M") && attractiveTypeCode == 2) likesCountByGenderManAndAttractiveTypeCode2++;
        if (gender.equals("M") && attractiveTypeCode == 3) likesCountByGenderManAndAttractiveTypeCode3++;
    }

    public void decreaseLikesCount(String gender, int attractiveTypeCode) {
        if (gender.equals("W") && attractiveTypeCode == 1) likesCountByGenderWomanAndAttractiveTypeCode1--;
        if (gender.equals("W") && attractiveTypeCode == 2) likesCountByGenderWomanAndAttractiveTypeCode2--;
        if (gender.equals("W") && attractiveTypeCode == 3) likesCountByGenderWomanAndAttractiveTypeCode3--;
        if (gender.equals("M") && attractiveTypeCode == 1) likesCountByGenderManAndAttractiveTypeCode1--;
        if (gender.equals("M") && attractiveTypeCode == 2) likesCountByGenderManAndAttractiveTypeCode2--;
        if (gender.equals("M") && attractiveTypeCode == 3) likesCountByGenderManAndAttractiveTypeCode3--;
    }

    public void updateGender(String gender) {
        this.gender = gender;
    }

    public InstaMemberSnapshot snapshot(String eventTypeCode) {
        return InstaMemberSnapshot
                .builder()
                .eventTypeCode(eventTypeCode)
                .username(username)
                .instaMember(this)
                .gender(gender)
                .likesCountByGenderManAndAttractiveTypeCode1(likesCountByGenderManAndAttractiveTypeCode1)
                .likesCountByGenderManAndAttractiveTypeCode2(likesCountByGenderManAndAttractiveTypeCode2)
                .likesCountByGenderManAndAttractiveTypeCode3(likesCountByGenderManAndAttractiveTypeCode3)
                .likesCountByGenderWomanAndAttractiveTypeCode1(likesCountByGenderWomanAndAttractiveTypeCode1)
                .likesCountByGenderWomanAndAttractiveTypeCode2(likesCountByGenderWomanAndAttractiveTypeCode2)
                .likesCountByGenderWomanAndAttractiveTypeCode3(likesCountByGenderWomanAndAttractiveTypeCode3)
                .build();
    }
//
//    public List<LikeablePerson> getToLikeablePeople() {
//        return this.toLikeablePeople;
//    }

    public List<LikeablePerson> getToLikeablePeople(String gender, Integer attractiveTypeCode, Integer sortCode) {
        List<LikeablePerson> resultToLikeablePeople = this.toLikeablePeople;

        resultToLikeablePeople = filterGender(gender, resultToLikeablePeople);
        resultToLikeablePeople = filterAttractiveTypeCode(attractiveTypeCode, resultToLikeablePeople);
        if (sortCode != null) {
            if (sortCode.equals(1)) {
                resultToLikeablePeople = resultToLikeablePeople
                        .stream()
                        .sorted(Comparator.comparing(LikeablePerson::getModifyDate).reversed())
                        .collect(Collectors.toList());
            }
            else if (sortCode.equals(2)) {
                resultToLikeablePeople = resultToLikeablePeople
                        .stream()
                        .sorted(Comparator.comparing(LikeablePerson::getModifyDate))
                        .collect(Collectors.toList());
            }
            else if (sortCode.equals(3)) {
//                resultToLikeablePeople = resultToLikeablePeople
//                        .stream()
//                        .sorted(Comparator.comparing())
//                        .collect(Collectors.toList());
            }
            else if (sortCode.equals(4)) {

            }
            else if (sortCode.equals(5)) {

            }
            else if (sortCode.equals(6)) {

            }
        }

        return resultToLikeablePeople;
    }

    private List<LikeablePerson> filterGender(String gender, List<LikeablePerson> resultToLikeablePeople) {
        if (gender != null) {
            if (gender.equals("M")) {
                resultToLikeablePeople = resultToLikeablePeople
                        .stream()
                        .filter(person -> person.getFromInstaMember().gender.equals("M"))
                        .collect(Collectors.toList());
            } else if (gender.equals("W")) {
                resultToLikeablePeople = resultToLikeablePeople
                        .stream()
                        .filter(person -> person.getFromInstaMember().gender.equals("W"))
                        .collect(Collectors.toList());
            }
        }

        return resultToLikeablePeople;
    }

    private List<LikeablePerson> filterAttractiveTypeCode(Integer attractiveTypeCode, List<LikeablePerson> resultToLikeablePeople) {
        if (attractiveTypeCode != null) {
            if (attractiveTypeCode.equals(1)) {
                resultToLikeablePeople = resultToLikeablePeople
                        .stream()
                        .filter(person -> person.getAttractiveTypeCode() == 1)
                        .collect(Collectors.toList());
            } else if (attractiveTypeCode.equals(2)) {
                resultToLikeablePeople = resultToLikeablePeople
                        .stream()
                        .filter(person -> person.getAttractiveTypeCode() == 2)
                        .collect(Collectors.toList());
            } else if (attractiveTypeCode.equals(3)) {
                resultToLikeablePeople = resultToLikeablePeople
                        .stream()
                        .filter(person -> person.getAttractiveTypeCode() == 3)
                        .collect(Collectors.toList());
            }
        }

        return resultToLikeablePeople;
    }
}
