<br />
<br />
<p align="center">
  <img src="src/assets/roommate.svg" height="48">
</p>
<br />
<br />
<h1>룸메이트 프로젝트</h1>
<blockquote>
  <p>성향 테스트를 통해 비슷한 생활습관을 가진 룸메이트를 찾아주는 서비스</p>
</blockquote>

<기획의도>

❓ 서울 거주 청년층의 주거 부담을 경감할 수 있는 방법이 없을까?<br />
❗️ 주거비용을 분담할 룸메이트를 찾아줄 수 있으면 좋겠다! <br />
💡 '룸메이트'는 생활반경과 생활패턴이 비슷한 룸메이트를 찾아주는 매칭서비스입니다.

<br />

<구현목표>

<ul>
  <li>생활 습관 테스트를 통해 나와 매칭률이 높은 사람을 필터링하여 확인할 수 있다.</li>
  <li>상세페이지에서 상대의 프로필과 집정보를 확인할 수 있다.</li>
  <li>원하는 상대방과 실시간 1:1채팅이 가능하다.</li>
</ul>

<br />
<br />
<h2>⏯ 프로젝트 실행</h2>
<ul>
  <li>
  1. 원격 저장소를 클론합니다.
    
    git clone https://github.com/roommate-project/back-end.git
  </li>   
  <li>
  2. 생성된 로컬 저장소로 이동 후 빌드
  
    /* windows */
    $ gradlew build

    /* linux */
    $ ./gradlew build
  </li>   
  <li>
  3. ./build/libs 에서 .jar파일 실행
  
    java -jar roommate-backend-0.0.1-SNAPSHOT.jar

  </li>
  <br />
  <li><a href="https://github.com/roommate-project/front-end.git">프론트 Git Hub으로 이동하기</a></li>
</ul>

<br />
<br />
<h2>👩🏻‍💻🧑🏻‍💻 룸메이트 팀원</h2>
<table>
  <tr>
    <td colspan='2'>팀원</td>
    <td>깃허브 주소</td>
  </tr>
  <tr>
    <td rowspan='2'>FE Developer</td>
    <td>김원희</td>
    <td><a href="https://github.com/wooneeS2">https://github.com/wooneeS2</a></td>
  </tr>
  <tr>
    <td>박수진</td>
    <td><a href="https://github.com/s0ojin">https://github.com/s0ojin</a></td>
  </tr>
  <tr>
    <td rowspan='2'>BE Developer</td>
    <td>임서영</td>
    <td><a href="https://github.com/im-shung2">https://github.com/im-shung</a></td>
  </tr>
  <tr>
    <td>최재성</td>
    <td><a href="https://github.com/JessJess-Choi">https://github.com/JessJess-Choi</a></td>
  </tr>
</table>

<br />
<br />
<h2>🛠 백엔드 기술 스택</h2>
    <table>
      <tr>
          <td>기술</td>
          <td>선택한 이유</td>
      </tr>
      <tr>
          <td><img src="https://img.shields.io/badge/Web Socket and Stomp-white?&logoColor=61DAFB"/></td>
          <td>
          - 채팅 구현을 위함<br />
          - pub/sub구조로 웹소켓 세션 관리가 편함
          </td>
      </tr>
    </table>

<br />
<br />
<h2>🙋🏻‍♀️ 백엔드 역할 분담</h2>
<h3>최재성</h3>
  <ol>
    <li>로그인 구현</li>
    <li>매칭 상세페이지 구현</li>
    <li>마이페이지 구현</li>
    <li>매칭 테스트페이지 구현</li>
  </ol>

<br />
<h3>임서영</h3>
  <ol>
    <li>채팅 구현</li>
  </ol>

<br />
<br />
<h2>🤫 룸메이트 미리보기</h2>
<br />
1. 회원가입
<img src="https://user-images.githubusercontent.com/100757599/198816169-7451cd75-1c00-491b-b7e3-790a1f5f9a0b.gif" />
<br />
2. 로그인
<img src="https://user-images.githubusercontent.com/100757599/198816244-feeef8f5-7ed8-4c55-8aae-0a38d86bc29a.gif" />
<br />
3. 매칭성향테스트
<img src="https://user-images.githubusercontent.com/100757599/198816274-d681d8a8-af9e-4a3c-a2a4-d3148b84f5f4.gif" />
<br />
4. 매칭페이지와 상세페이지
<img src="https://user-images.githubusercontent.com/100757599/198816311-7660d2eb-56fe-45c0-8877-552808a901aa.gif" />
<br />
5. 채팅
<img src="https://user-images.githubusercontent.com/100757599/198816318-4e942e83-f9f2-4c84-b63a-a168a915f3b0.gif" />
<br />
6. 마이페이지
<img src="https://user-images.githubusercontent.com/100757599/198816341-042db0c4-613f-4b64-85da-e20cb00a7a6b.gif" />


<br />
<br />
<h2>💡 버전</h2>
1.0 : 룸메이트 서비스 배포(22.10.23)
<br />
