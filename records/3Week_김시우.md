###Title: [3Week] 김시우

---

###미션 요구사항 분석 & 체크리스트
- [x] 호감 표시/호감 사유 변경 후, 개별 호감표시건에서 3시간 동안은 호감취소와 호감사유를 변경 불가
  - [x] modifyUnlockDate 필드를 통해 남은 변경 가능 시간을 계산
- [x] NCP를 통한 배포

###3주차 미션 요약
**[접근 방법]**
1. list.html 에서 button을 어떻게 활성화 하는지 확인
  -> likeablePerson.modifyUnlockDateRemainStrHuman으로 남은 시간을 출력
2. LikeablePerson 클래스의 getModifyUnlockDateRemainStrHuman 메서드를 통해 남은 시간 출력을 확인
3. Util 클래스를 만들어 남은 시간을 계산해 `~시 ~분`형태의 String 변수를 반환하는 메서드 작성
4. application에서 정상적으로 작동하는지 확인

**[특이사항]**
1. 배포를 하는데 있어서 처음 하는 방법이라 어려움이 있었다.(해결)
2. 남은 시간을 계산하는 로직을 LikeablePerson 엔티티에 두기에는 메서드의 크기가 크고 다른 엔티티에서 사용 할 수 있다고 생각해 Util 클래스로 두어 작성을 했는데 더 나은 방법이 있는지 궁금하다.

**[Refactoring]**
