###Title: [1Week] 김시우

---

###미션 요구사항 분석 & 체크리스트
- [x] 호감목록 삭제하는 기능 추가
  - [x] 호감목록 삭제 기능 테스트 코드 추가
  - [x] LikeablePersonController 에서 LikeablePerson Entity 의 Id를 받아옴
  - [x] Rq 로부터 가져온 Member Entity 의 instaMemberId 컬럼과 LikeablePerson 의 fromInstaMemberId 컬럼의 값이 같은지 확인 
  - [x] LikeablePersonService 에서 Controller 부터 전달받은 entityId 값을 통해 해당 Row 삭제
- [x] Oauth2.0을 통해 Google 로그인 기능 추가
  - [x] Google key, 프로젝트 설정 파일에 추가

###1주차 미션 요약
**[접근 방법]**

**호감 목록 삭제 기능**
1. list.html 파일에서 어떤 URI 를 원하는지 확인 -> /likeablePerson/delete/{id} -> id 값을 PathVariable 형식으로 GET 방식 
2. Controller 에서 @GetMapping("/delete/{id}") 방식으로 접근
3. Service 에서 삭제 기능을 구현 -> 현재 로그인 되어있는 정보와 PathVariable 방식으로 보낸 삭제하고자 하는 id 값을 파라미터로 Service로 전달
4. Service 에서 구현중 예외 처리할 경우 고려
   1. LikeablePersonRepository 에서 id를 통해 Optional< LikeablePerson > 객체를 가져오므로 비어있을 경우 고려 -> .isEmpty()
   2. 로그인 되어있는 상태로 호감목록을 볼 수 있어 발생경우는 매우 적지만 로그인 되어있는 사용자의 인스타 아이디와  호감목록의 정보가 일치하지 않는 경우 고려
5. 삭제 완료 메시지와 삭제된 객체를 RsData 객체에 담아 반환
6. Controller 에서 list 목록을 다시 띄우기 위해 Redirect

**Google 로그인**
1. Google 로그인 API key를 발급
2. application 설정파일에 Google 로그인 API 설정 코드 추가

**[특이사항]**
1. Google 로그인 API를 통해 로그인은 되지만 인증 정보를 받을 수 없어 환경설정을 만져 보았지만 실패
   - Google 로그인 API를 재발급해 해결

**[Refactoring]**
1. Entity 클래스와 Repository의 기본키 값일치 -> long