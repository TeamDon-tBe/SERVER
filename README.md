# Don't be - Server
비난·차별·혐오 표현의 글을 투명하게, 우리가 함께 만들어가는 온화한 익명 커뮤니티 Don’t be

## 💻 팀원 소개
💙 서버 : 김보람

🐸 서버 : 정홍준
| 김보람 | 정홍준 |
| :---------:|:----------:|
|<img width="330" height="350" alt="image" src="https://avatars.githubusercontent.com/u/128011308?s=400&u=633d530e6863dea1ad9f409fd2f979b85c018b6f&v=4"> | <img width="330" height="350" alt="image" src="https://github.com/Team-Puzzling/Puzzling_Server/assets/68415644/51bde41d-f223-44bc-b84b-fe47bb09fdde"> | 
| [bo-ram-bo-ram](https://github.com/bo-ram-bo-ram) | [Hong0329](https://github.com/Hong0329) |
<br>

## 역할 분담


## Git Convention

### 🔹Commit Convention
 - ✅ `[CHORE]` : 동작에 영향 없는 코드 or 변경 없는 변경사항(주석 추가 등)
- ✨ `[FEAT]` : 새로운 기능 구현
- ➕ `[ADD]` : Feat 이외의 부수적인 코드 추가, 라이브러리 추가, 새로운 파일 생성
- 🔨 `[FIX]` : 버그, 오류 해결
- ⚰️ `[DEL]` : 쓸모없는 코드 삭제
- 📝 `[DOCS]` : README나 WIKI 등의 문서 수정
- ✏️ `[CORRECT]` : 주로 문법의 오류나 타입의 변경, 이름 변경시
- ⏪️ `[RENAME]` : 파일 이름 변경시
- ♻️ `[REFACTOR]` : 전면 수정
- 🔀 `[MERGE]`: 다른 브랜치와 병합

### 커밋 예시

`ex ) git commit -m "#1 [FEAT] 회원가입 기능 완료"`

<br>

### 🔹Branch Convention

- [develop] : 최종 배포
- [feature] : 기능 추가
- [fix] : 에러 수정, 버그 수정
- [docs] : README, 문서
- [refactor] : 코드 리펙토링 (기능 변경 없이 코드만 수정할 때)
- [modify] : 코드 수정 (기능의 변화가 있을 때)
- [chore] : gradle 세팅, 위의 것 이외에 거의 모든 것

### 브랜치 명 예시

`ex) feature/#이슈번호`

<br>

### 🔹Branch Strategy
### Git Flow

기본적으로 Git Flow 전략을 이용한다. Fork한 후 나의 repository에서 작업하고 구현 후 원본 repository에 pr을 날린다. 작업 시작 시 선행되어야 할 작업은 다음과 같다.

```java
1. Issue를 생성한다.
2. feature Branch를 생성한다.
3. Add - Commit - Push - Pull Request 의 과정을 거친다.
4. Pull Request가 작성되면 작성자 이외의 다른 팀원이 Code Review를 한다.
5. Code Review가 완료되면 Pull Request 작성자가 develop Branch로 merge 한다.
6. merge된 작업이 있을 경우, 다른 브랜치에서 작업을 진행 중이던 개발자는 본인의 브랜치로 merge된 작업을 Pull 받아온다.
7. 종료된 Issue와 Pull Request의 Label과 Project를 관리한다.
```

- 기본적으로 git flow 전략을 사용합니다.
- main, develop, feature 3가지 branch 를 기본으로 합니다.
- main → develop → feature. feature 브랜치는 feat/기능명으로 사용합니다.
- 이슈를 사용하는 경우 브랜치명을 feature/[issue num]-[feature name]로 합니다.

<br>


### 🔹Issue Convention
- [feat] : 기능 추가
- [fix] : 에러 수정, 버그 수정
- [docs] : README, 문서
- [refactor] : 코드 리펙토링 (기능 변경 없이 코드만 수정할 때)
- [modify] : 코드 수정 (기능의 변화가 있을 때)
- [chore] : gradle 세팅, 위의 것 이외에 거의 모든 것

`ex) [feat] user api 구현`

<br>

## Coding Convention


 <details>  <summary>1. 변수</summary>  
 <div markdown="1"> 
 <br>
     1-1. camelCase 형식을 사용합니다.<br><br>
     1-2. 이름은 짧지만 의미 있어야 합니다.(사용 의도를 누구나 알아낼 수 있도록!)<br><br>
     1-3. ENUM이나 상수는 대문자로 표기합니다.<br><br>
 </div>  </details>

 <details>  <summary>2. 함수</summary>  
 <div markdown="1"> 
 <br>
 2-1. 함수의 이름은 동사여야 하며, camelCase 형식을 사용합니다. <br><br>
     2-2. 객체 이름을 함수 이름에 중복적으로 사용하지 않습니다.<br><br>
     </div>  </details>

 <details>  <summary>3. 클래스 </summary>  
 <div markdown="1"> 
 <br>
 클래스 이름은 명사이어야 하며 Pascal Case를 사용합니다.
     </div>  </details>

 <details>  <summary>4. 인터페이스 </summary>  
 <div markdown="1"> 
 <br>
 클래스와 같은 규칙을 사용합니다.
     </div>  </details>


## 개발환경
- Java 17
- Spring
- JPA

## 프로젝트 구조


## 아키텍트 구조


## ERD



