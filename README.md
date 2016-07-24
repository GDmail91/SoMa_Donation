# 과제 1.  SoMa_Donation (Android)

## 백업포유 (Back Up For You: BUFY)
백업포유(BUFY)는 그동안 주변에 눈치채지 못했지만, 도움이 필요한 이웃을 위한 후원 장려 커뮤니티 입니다.
BUFY는 매달 순위를 지정하여 가장 추천과 조회수가 높은 추천 대상에 대해 후원금 및 후원 물품을 전달해 줍니다.
BUFY에서는 도움이 필요한 어린이집, 요양원, 독거노인 뿐만 아니라 더 나아가서 주위 가까운 친구 끼리도 도움이 필요하다면 얼마든지 도와줄 수 있습니다.

- 매달 순위를 매겨 가장 도움이 필요한 곳을 뽑습니다.
- 페이스북에 공유를 하여 사람들에게 알립니다.
- 공유만으로도 후원에 참여할 수 있습니다.
- 공유로 얻은 수익은 대상 단체/개인 에게로 돌아갑니다.
- 도움이 필요한 단체/개인을 홍보하여 그동안 소홀했던 이웃에대한 관심을 늘릴 수 있습니다.

페이스북 공유만으로도 후원이 가능한 BUFY에서 당신의 이웃을 후원을 해주세요.

---
## 클라이언트 기술소개
BUFY에 적용된 기술

### 라이브러리
- **facebook-android-sdk :** 페이스북 로그인 연동, 공유하기
- **okhttp3 :** http 통신용
- **gson :** 전달 받은 JSON 데이터 파싱용
- **retrofit 2.0 :** 서버와 API 통신을 하기위한 라이브러리
- **glide :** 비동기 방식 이미지 처리 위한 라이브러리
- **play-services :** FMS(Firebase Massaging Service) 연결용
- **firebase-messaging :** FMS 송수신용
- **SharedPreferences :** 액세스 토큰 및 사용자 정보를 저장할 키값 저장소로 사용

### UI 소개
#### 로그인 화면 
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=login_example.jpg)
> .facebook.FacebookLogin

페이스북을 이용하여 로그인 할 수 있도록 버튼을 제공한다.
버튼 클릭시 페이스북 로그인 페이지로 이동하며 응답받은 메세지에서 **access_token** 및 **사용자 이름** 을 획득한다.
*(전화번호도 입력 받고자 하였으나 페이스북의 경우 전화번호가 없는 사용자도 있으며, *
*개인정보 수집 및 로그인 페이지에 Depth 추가 등의 문제를 고려하여 현재 제외된 상태이다.)*

#### 메인 화면 
>.MainActivity
>.MainFragment
>.ViewPagerAdapter
>.rank.RankItem
>.rank.RankListAdapter
>.review.ReviewItem
>.review.ReviewListAdapter

메인화면은 **Fragment**를 이용하여 사이드 메뉴와 **ViewPager**를 이용한 가로 스와이프 화면을 제공한다.

##### 이달의 후원
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=monthly_example.png)

메인 페이지로서 이달의 후원을 전면에 두어 후원을 도모한다.
**후원하기**와 **공유하기** 버튼이 제공된다
 - **후원하기 :** 직접 돈을 넣어 후원할 수 있도록 **PG사(이니시스)** 연동을 하였으며 납입된 금액을 확인하여 관리자가 해당 금액만큼을 ""후원금""에 추가하도록 한다.
 - **공유하기 :** 페이스북 포스팅을 이용하여  **공유**를 한다. 관심있는 사용자는 해당 페이지로 접속을 하게되어 주위에 **홍보**를 할 수 있도록 제공한다.
 - **후원금 :** 메인페이지에는 이달의 후원 대상에 대한 후원 금액이 표시되는데 이는 **직접 후원**된 후원금과 **광고수익**으로 얻은 수익의 80%를 기부하는 것으로 한다.

##### 랭킹
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=rank_list_example.png)

이번달의 랭킹 리스트를 순위순서대로 보여주며 사용자별 좋아요 상태와 조회수를 표시하여 보여준다.
 - **좋아요 버튼 :** 사용자별 좋아요 상태에 따라 **붉게** 표시를 해준다.
 - **조회수 버튼 :** 조회시 마다 **view count**가 1씩 증가한다.
 - **랭킹 순위 :** 랭킹목록에서 해당 게시물의 랭킹 순위를 보여준다. 랭킹 순위는 좋아요 + 조회수 + 게시일 순으로 이루어지며 좋아요가 같다면 조회수, 조회수마저 같다면 게시일 순으로 보여준다.

##### 후기
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=review_list_example.png)

지난 후원대상 단체/개인 에 대한 후원금과 작성된 후기가 올라온다.

#### 게시물 화면
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=detail_example.png)

>.rank.DetailDonation

사진, 제목, 설명의 글이 적혀있다.
좋아요 버튼이 표시되며 **클릭시** 좋아요 상태에 따라 좋아요를  했다면 **채운손**, 안했다면 **빈손**으로 표시
된다.
**공유버튼**을 누르면 해당유저의 페이스북 담벼락에 해당 **게시물**과 **링크**가 제공된다.
본인계정의 글이라면 **수정하기** 버튼이 생성되며 게시물을 수정하는 페이지로 이동한다.

#### 게시물 수정/등록 화면
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=edit_example.png)

>.rankEditDonation
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=search_example.png)

게시물을 수정할 수 있도록 **제목** 및 **설명**이 **EditText** View로 바뀌며 사진 클릭시 **갤러리**로 이동하여 사진을 가져올 수 있도록 하였다.

####알림
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=alarm_example.png) 

>.alarm.AlarmListAdapter
>.alarm.AlarmActivity

**FCM**을 통해 전송되어진 알림 리스트가 나열된다.
클릭시 연결된 **카테고리(ranking, monthly)**와 **ID**를 이용하여 해당 게시물로 이동하게된다.

#### 검색
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=search_example.png)

>.search.SearchActivity
>.rank.RankListAdapter

**타이틀**을 이용한 검색이 가능하며 검색결과를 리스트로 보여준다.

#### 내글 보기
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=my_list_example.png)

>.post.MyPostActivity
>.post.PostListActivity

현재까지 작성한 **게시물**과 각 게시물별 **좋아요**, **조회수**를 확인 할 수 있다.

#### 후원 하기
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=donate_example.png)

>.donate.DonateActivity
>.donate.InicisWebViewClient
>.donate.PaymentScheme

**이달의 후원**에서 후원하기 클릭시 연결되는 **WebView** 액티비티이다.
**PG사 결제 모듈**과 연결하여 원하는 금액을 결제할 수 있는 **결제페이지**로 연결된다.

#### 후원 문의
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=contact_example.png)

>.ContactActivity

**BUFY**에서 제공되는 후원 방식 외에 물건을 통한 기증 등 **기타 후원 문의**가 필요할 수 있기 때문에 후원 문의를 제공한다.
여기서 작성되어지는 메시지는 **읽기 권한** 이상을 가진 **관리자**에게 보여지며 **담당자**가 **Email**을 통해 답장 할 수 있도록 **Email** 또한 표시하도록 하였다.

#### 설정
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=setting_example.png)

>.SettingActivity

설정페이지에는 푸시수신을 설정할 수 있는 **푸시 수신여부**, **현재 버전 정보**, **로그인 사용자 이름** 을 보여준다.

### 기타 기능

#### 푸시 수신
![Alt text](http://bufy.mooo.com/ranking/get/image?content_img=push_noti_example.png)

>.LinkActivity

푸시 수신시 **Notification Bar**에 알림이 뜨게 되는데 클릭시 **인텐트**가 전달된다.
이러한 수신후 인텐트를 받아 **동작**을 **처리**하기위한 Activity 이다.

#### 도구
- AndroidStudio
- Git/Github
- SourceTree
