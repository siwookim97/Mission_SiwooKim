## Title: [4Week] 김시우

### 미션 요구사항 분석 & 체크리스트

#### 필수 미션

- [x] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현
  - [x] Controller를 통해 파라미터 3개 받기
  - [x] InstaMember Entity에 getToLikeablePeople 메서드에 검색 조건 작성

- [ ] 네이버클라우드플랫폼을 통한 배포, 도메인, HTTPS 까지 적용

#### 선택 미션

- [ ] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 호감사유 필터링기능 구현

- [ ] 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 정렬기능


---

### 4주차 미션 요약

---

**[접근 방법]**

필수미션 1) - 내가 받은 호감리스트(/usr/likeablePerson/toList)에서 성별 필터링기능 구현<br><br>
성별, 호감사유, 정렬, 3개의 정렬 요건으로 모두 확장하기 위해서 동적쿼리로 작성하고자 했지만 InstaMember Entity에 toLikeablePeople 
필드가 있어 활용하기로 함, toList 페이지는 3개의 파라미터 모두 null값을 허용해야한다.
null값을 체크하기 위해서 Request를 사용하고자 하였지만 이미 Parameter 방식으로 받기에 3개의 파라미터 받아옴

InstaMember Entity 클래스 에서 나를 호감하고 있는 사람을 List 필드로 다루고 있기에 Entity 클래스에서 get 메서드를 통해서 가져오도록 구현

**[특이사항]**

**[Refactoring]**
