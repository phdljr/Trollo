# Trollo
[S.A 계획서](https://www.notion.so/S-A-a856bb52f3ea4821b8c2a3b51533c805)

## 개요
> trollo : 프로젝트를 보드로 정리하여 프로젝트 관리와 작업흐름 관리를 하는 trello를 클론코딩

## 팀 구성원 및 역할 분담
> 통장을 구해조 : 팀장 이종렬, 팀원 김민선, 팀원 김용운, 팀원 신승호, 팀원 박창선

* 이종렬
  * Card 도메인
* 김민선
  * User, Invitation 도메인
* 김용운
  * Board, Section 도메인
* 신승호
  * Checklist, Event 도메인
* 박창선
  * Comment, File 도메인

(각 도메인별 예외처리는 각자의 도메인에서 각자 처리)

## 설계 전략
<details>
    <summary>시스템 상황분석 및 시나리오</summary>

* 사용자
  * 회원가입을 할 수 있다.
  * 로그인을 할 수 있다.
  * 로그아웃을 할 수 있다.
  * 회원 탈퇴를 할 수 있다.
* 초대
  * 다른 사용자를 초대할 수 있다.
  * 초대를 받은 사용자는 초대를 거절/수락할 수 있다.
  * 초대를 거절하면 초대 테이블에서 해당 Entity가 삭제된다.
  * 초대를 수락하면 초대 테이블에서 해당 Entity가 삭제되고 보드_사용자_테이블에 참여자로 등록된다.
* 보드
  * 칼럼들의 집합체
  * 보드는 다음과 같은 속성을 갖고 있다.
    * Id, 이름, 생성자, 배경 색상, 설명
  * 노션 페이지 같은 느낌
  * 사용자가 생성할 수 있다.
  * 생성한 사용자(주인)가 다른 사용자(초대받은 사람)를 초대할 수 있다.
  * 하나의 보드에 여러 명의 사용자가 존재한다.
  * 보드에 속한 사용자들은 보드를 수정/초대할 수 있다.
  * 보드에 속한 사용자들은 나갈 수 있다.
  * 생성자만 보드를 삭제할 수 있다.
* 컬럼
  * 카드들의 집합체
  * 특정 보드에 속한 사용자만 생성할 수 있다.
  * 컬럼 속성
    * Id, 이름, 순서
  * 컬럼은 보드에 속한 누구나 생성/수정/삭제가 가능하다.
* 카드
  * 카드 속성
    * Id, 제목, 내용, 칼럼, 색상, 마감일, (체크리스트, 작업자 리스트)
  * 카드는 보드에 속한 누구나 조회/생성/수정/삭제가 가능하다.
    * 조회
      * 해당 보드에 속한 사용자는 해당 보드의 모든 카드를 볼 수 있다.
      * 보드 기준으로 카드를 조회할 수 있다.
      * 칼럼 기준으로 카드를 조회할 수 있다.
        * 칼럼에 속한 카드들은 순서가 정해져 있다.
      * 하나의 카드는 제목과 내용, 댓글, 체크리스트, 작업자 목록이 존재한다.
    * 생성
      * 제목과 함께 카드를 생성할 수 있다.
      * 나머지는 빈 내용이다.
      * 위치는 항상 해당 칼럼의 맨 마지막에 생성된다.
    * 수정
      * 내용을 수정할 수 있다.
      * 댓글을 달 수 있다.
        * 하나의 카드에는 여러 개의 댓글이 존재한다.
      * 작업자를 설정해줄 수 있다.
        * 하나의 카드에는 여러 명의 작업자를 정할 수 있다.
      * 보드 내에서 카드를 옮길 수 있다.
        * 특정 카드를 다른 카드의 위 또는 아래로 옮길 수 있다.
          * `/api/v1/cards/{cardId}/to/{toCardId}/[above|below]`
        * 카드가 없는 빈 칼럼으로 옮길 수 있다.
          * `/api/v1/cards/{cardId}/to/columns/{toColumnId}`
        * 서로 다른 칼럼으로도 위와 같은 api로 옮길 수 있다.
          * 단, 다른 보드의 칼럼으로는 옮길 수 없다.
    * 삭제
      * 해당 보드에 속한 사용자는 해당 보드의 카드를 삭제할 수 있다.
      * 작성자가 아니여도 삭제할 수 있다.
  * 고급기능
    * 카드 내에 체크리스트가 존재한다.
    * 체크리스트 생성 URL을 통해 할일 입력
    * 체크리스트 수정을 통해 입력한 할일의 완료/취소 체크 가능
    * 체크리스트의 진행도가 존재한다.
    * 첨부 파일을 업/다운로드를 할 수 있다.
* 알림 기능
  * 카드에 대한 상태변경에 대해 해당 보드의 참여자한테 알림을 보낸다.
    * 카드가 생성/수정/삭제 되었을 때 알림을 보낸다.
    * 카드가 다른 칼럼으로 이동됐을 때도 알람을 보낸다.
    * 카드에 댓글이 달렸을 때 알람을 보낸다.
  * 각 상태변경에 해당하는 메서드가 실행될 때 스프링 이벤트 퍼블리셔는 이벤트를 발행한다.
  * 이벤트 리스너는 이벤트를 받아 이벤트의 정보를 통해 notification과 userNotification을 저장한다.
    * notification은 카드에 변경을 일으킨 당사자의 아이디, 해당카드가 속한 보드아이디, 변경타입, 변경메세지, 알림이 저장되는 시간(카드가 변경된 시간)으로 구성
    * userNotification은 알림이 생성된 시점의 보드 참여자와 알림의 아이디, 알림의 상태(UNREAD)로 구성(저장될 때, 한번에 보드 참여자의 수만큼 동일한 아이디의 알림으로 필드가 생성)
  * 보드의 참여자는 알림을 조회할 수 있다.
  * 조회하면 해당 보드 참여자의 아이디를 지닌 필드의 UNREAD가 READ로 변경
  * 카드가 생성/수정/삭제 되었을 때 알림을 보낸다.

</details>
<details>
    <summary>ERD 설계</summary>

<img width="918" alt="스크린샷 2024-01-02 오후 10 58 57" src="https://github.com/yoooooungwoon/plusProject/assets/94377282/af6b7f1d-e298-4f30-b2aa-587d98dcc09e">

[ErdLink](https://www.erdcloud.com/d/hRwLsb57PKqKgXL4v)

</details>

## 협업 전략

### 커밋 컨벤션

`ex) [#issue-number]Feat: 로그인 기능 추가`

![Untitled](https://github.com/yoooooungwoon/plusProject/assets/94377282/ddb98625-d6f6-4fff-b6f7-6f6965771ee8)

* 브랜치 전략(깃 플로우)
  * main : 4명 리뷰
  * develop : 2명 리뷰
  * feature/(기능명)

### 성과

![image](https://github.com/yoooooungwoon/plusProject/assets/94377282/94deec79-3621-4828-8f3a-c1b33967daaf)

![image](https://github.com/yoooooungwoon/plusProject/assets/94377282/e29cdd7a-e88b-4c16-a9b3-175c1098be1b)

![image](https://github.com/yoooooungwoon/plusProject/assets/94377282/7341413b-e44a-4f67-9750-e4f823ad5590)

## 구현 사항

### 필수 구현 기능

* 사용자 관리 기능
  * 로그인/회원가입
  * 사용자 정보 수정 및 삭제
* 보드 관리 기능
  * 보드 생성
  * 보드 수정
    * 보드 이름
    * 배경 색상
  * 보드 삭제
    * 생성한 사용자만 삭제
  * 보드 초대
    * 특정 사용자들을 해당 보드에 초대시켜 협업
* 컬럼 관리 기능
  * 컬럼 생성
    * 보드 내부에 컬럼을 생성할 수 있다
  * 컬럼 이름 수정
  * 컬럼 삭제
  * 컬럼 순서 이동
* 카드 관리 기능
  * 카드 생성
    * 컬럼 내부에 카드를 생성
  * 카드 수정
    * 카드 이름
    * 카드 설명
    * 카드 색상
    * 작업자 할당
    * 작업자 변경
  * 카드 삭제
  * 카드 이동
    * 같은 컬럼 내에서 카드의 위치를 변경
    * 카드를 다른 컬럼으로 이동
* 카드 상세 기능
  * 댓글 달기
  * 마감일 지정

### 스페셜 구현 기능

* 카드에 고급 기능 구현
  * 카드 파일 첨부/다운로드
  * 카드 내에 체크리스트 추가
* 알림 기능 구현
  * 카드 상태가 변경되면 알림
  * 카드에 댓글이 달리면 알림

## 디렉토리 구조

<details>
    <summary>자세히 보기</summary>
 
    ├─main
    │  ├─java
    │  │  └─org
    │  │      └─nbc
    │  │          └─account
    │  │              └─trollo
    │  │                  ├─domain
    │  │                  │  ├─board
    │  │                  │  │  ├─combine
    │  │                  │  │  │  └─impl
    │  │                  │  │  ├─controller
    │  │                  │  │  ├─dto
    │  │                  │  │  ├─entity
    │  │                  │  │  ├─exception
    │  │                  │  │  ├─repository
    │  │                  │  │  └─service
    │  │                  │  │      └─impl
    │  │                  │  ├─card
    │  │                  │  │  ├─controller
    │  │                  │  │  ├─converter
    │  │                  │  │  ├─dto
    │  │                  │  │  │  ├─request
    │  │                  │  │  │  └─response
    │  │                  │  │  ├─entity
    │  │                  │  │  ├─exception
    │  │                  │  │  ├─mapper
    │  │                  │  │  ├─repository
    │  │                  │  │  └─service
    │  │                  │  │      └─impl
    │  │                  │  ├─checklist
    │  │                  │  │  ├─controller
    │  │                  │  │  ├─dto
    │  │                  │  │  │  ├─request
    │  │                  │  │  │  └─response
    │  │                  │  │  ├─entity
    │  │                  │  │  ├─exception
    │  │                  │  │  ├─repository
    │  │                  │  │  └─service
    │  │                  │  │      └─impl
    │  │                  │  ├─comment
    │  │                  │  │  ├─controller
    │  │                  │  │  ├─dto
    │  │                  │  │  │  ├─req
    │  │                  │  │  │  └─res
    │  │                  │  │  ├─entity
    │  │                  │  │  ├─exception
    │  │                  │  │  ├─mapper
    │  │                  │  │  ├─repository
    │  │                  │  │  └─service
    │  │                  │  │      └─impl
    │  │                  │  ├─invitation
    │  │                  │  │  ├─controller
    │  │                  │  │  ├─dto
    │  │                  │  │  │  └─response
    │  │                  │  │  ├─entity
    │  │                  │  │  ├─exception
    │  │                  │  │  ├─repository
    │  │                  │  │  └─service
    │  │                  │  │      └─impl
    │  │                  │  ├─notification
    │  │                  │  │  ├─controller
    │  │                  │  │  ├─dto
    │  │                  │  │  │  └─response
    │  │                  │  │  ├─entity
    │  │                  │  │  ├─event
    │  │                  │  │  ├─exception
    │  │                  │  │  ├─repository
    │  │                  │  │  └─service
    │  │                  │  │      └─impl
    │  │                  │  ├─S3File
    │  │                  │  │  ├─controller
    │  │                  │  │  ├─entity
    │  │                  │  │  ├─repository
    │  │                  │  │  └─service
    │  │                  │  │      └─impl
    │  │                  │  ├─section
    │  │                  │  │  ├─controller
    │  │                  │  │  ├─dto
    │  │                  │  │  │  └─response
    │  │                  │  │  ├─entity
    │  │                  │  │  ├─exception
    │  │                  │  │  ├─repository
    │  │                  │  │  └─service
    │  │                  │  │      └─impl
    │  │                  │  ├─user
    │  │                  │  │  ├─controller
    │  │                  │  │  ├─dto
    │  │                  │  │  │  ├─request
    │  │                  │  │  │  └─response
    │  │                  │  │  ├─entity
    │  │                  │  │  ├─exception
    │  │                  │  │  ├─repository
    │  │                  │  │  └─service
    │  │                  │  │      └─impl
    │  │                  │  ├─userboard
    │  │                  │  │  ├─entity
    │  │                  │  │  ├─exception
    │  │                  │  │  └─repository
    │  │                  │  ├─usernotification
    │  │                  │  │  ├─entity
    │  │                  │  │  └─repository
    │  │                  │  └─worker
    │  │                  │      ├─dto
    │  │                  │      │  └─response
    │  │                  │      ├─entity
    │  │                  │      ├─exception
    │  │                  │      └─repository
    │  │                  └─global
    │  │                      ├─config
    │  │                      ├─dto
    │  │                      ├─entity
    │  │                      ├─exception
    │  │                      ├─jwt
    │  │                      ├─security
    │  │                      └─util
    │  └─resources
    └─test
        └─java
            └─org
                └─nbc
                    └─account
                        └─trollo

</details>

