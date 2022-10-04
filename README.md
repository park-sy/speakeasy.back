# Speakeasy Server 구성

## speakeasy.backType Commit Rule

feat : 새로운 기능 추가, 기존의 기능을 요구 사항에 맞추어 수정
<br>
fix : 기능에 대한 버그 수정
<br>
build : 빌드 관련 수정
<br>
chore : 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore
<br>
ci : CI 관련 설정 수정
<br>
docs : 문서(주석) 수정
<br>
style : 코드 스타일, 포맷팅에 대한 수정
<br>
refactor : 기능의 변화가 아닌 코드 리팩터링 ex) 변수 이름 변경
<br>
test : 테스트 코드 추가/수정
<br>
release : 버전 릴리즈


## DB 구성

![erd](https://user-images.githubusercontent.com/53611554/193597816-83ea76a7-8f08-41df-b3bb-2682dcb308b3.png)


## 주요 쿼리 요구 사항  

유저 투표 기반의 필터 적용

투표된 노트의 종류 N개 중 비율이 1/N이 넘는 항목이 필터링 되도록 함

정렬 시 해당 오름 차순으로 해당 값으로 내림차순 정렬



