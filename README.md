# Fish It

## Introduction

오픈마켓 가격 변동 확인 사이트, Fish It 입니다.
- 사용자가 원하는 물품을 검색했을 시 해당 제품 관련 정보와 최저가 마켓 정보를 제공합니다.
- 가격 알림을 받기 희망하는 물품과 희망 가격을 등록하면 주기적으로 모니터링하여 사용자가 원하는 최저가에 도달했을 시 메일 알림을 발송합니다.
- 원하는 제품 등록 시 일정 시간 간격으로 해당 제품의 가격 변동 추이를 그래프로 시각화하여 제공합니다.

<br>



## System Architecture
![image](https://user-images.githubusercontent.com/57824857/182030922-79baac24-3ba5-4efc-b386-1d11c9df0336.png)



## Tech Stack

|                           Frontend                           |                        Backend (API)                         |
| :----------------------------------------------------------: | :----------------------------------------------------------: | 
| ![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=flat-square&logo=typescript&logoColor=white) ![React](https://img.shields.io/badge/React-61DAFB?style=flat-square&logo=react&logoColor=white) ![HighCharts](https://img.shields.io/badge/HighCharts-13324B?style=flat-square&logo=Chart.js&logoColor=white) ![Heroku](https://img.shields.io/badge/Heroku-430098?style=flat-square&logo=heroku&logoColor=white) | ![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=flat-square&logo=springboot&color=#6DB33F) ![AWS](https://img.shields.io/badge/AWS-232F3E?style=flat&logo=amazon-aws&logoColor=white)  ![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=Docker&logoColor=white) ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white) ![Jsoup](https://img.shields.io/badge/JSoup-007396?style=flat-square&logo=OpenJDK&logoColor=white)  



## 주요기능

<table>
  <tr>
    <td><img width="600" src="https://user-images.githubusercontent.com/94429560/183659009-a0938dcf-a5aa-40af-9e65-59f6a8be8aa3.png"></td>
    <td><img width="600" src="https://user-images.githubusercontent.com/94429560/183658385-1e38457d-a2f2-4d9a-a18a-4fbe84ebba11.png"></td>
    <td><img width="600" src="https://user-images.githubusercontent.com/94429560/183659288-c8eeb39d-8c42-4d80-9c80-93e9a9e034de.png"></td>
  </tr>
  <tr>
    <td align="center"><b>메인페이지</b></td>
    <td align="center"><b>로그인 및 회원가입</b></td>
    <td align="center"><b>제품 검색 페이지</b></td>
  </tr>
</table>
<table>
  <tr>
    <td><img width="600" src="https://user-images.githubusercontent.com/57824857/182031108-9d37ec57-0a4e-47c5-a5ca-3ceef33684a0.png"></td>
    <td><img width="600" src="https://user-images.githubusercontent.com/57824857/182031117-53e67dc4-fbba-4c9d-9ab1-45390e6728a4.png"><img width="600" src=""> </td>
  </tr>
  <tr>
    <td align="center"><b>제품 상세 페이지</b></td>
    <td align="center"><b>마이페이지</b></td>
  </tr>
</table>

<table>
  <tr>
    <td><img width="600" src="https://user-images.githubusercontent.com/94429560/183660765-694b988e-5025-4552-ad29-df8d268ac763.png"></td>
    <td><img width="600" src="https://user-images.githubusercontent.com/57824857/182030983-494b7b0e-cc05-40f9-a4e1-32aa4c4a5091.png"></td>
  </tr>
  <tr>
    <td align="center"><b>가격 변동 추이 그래프</b></td>
    <td align="center"><b>알림 메일</b></td>
  </tr>
</table>



  



# 사용한 기술


- Google OAuth2.0

- Kakao OAuth2.0 

- JSoup
- HighCharts


<br>

## 배포 서버
http://fish-it-c.herokuapp.com

테스트용 계정
- email: test@test.com
- password : test
  

## Dev Server

http://3.39.75.19:8080/api/v1



## API Documentation

http://3.39.75.19:8080/swagger-ui.html



## Getting Started

`spring: 2.6.6`  
`react: 18.1.0`

### 1. Cloning

```
$ git https://github.com/Techeer-TeamC/Fish_it.git
```

- Provide necessary information in application.yml template


### 2. Frontend
- Install packages

```
$ cd frontend
$ npm install
```


### 3. Docker
**Dev**
```
$ git submodule update --init --recursive
$ docker-compose-local up --build                             
```

**Production Enviornment**
```
$ git submodule update --init --recursive
$ docker-compose up --build                             
```



## Architecture

### CICD Architecture

![image](https://user-images.githubusercontent.com/66551410/152016992-cff6b052-35d7-416e-868c-b2702a3ef692.png)



### MySQL ERD

![image](https://user-images.githubusercontent.com/57824857/182031224-e58a7c3b-3388-47b4-9eb9-bc1d7296af03.png)


### Medium.com Docs
👉 https://medium.com/@techeerteamc/fish-it-get-notification-when-the-product-reaches-the-price-you-want-17b9239282b7



## Contributors

|                 김소미                 |                  손정민                  |                    정태원                    |               오홍기                |               장현우                |
| :------------------------------------: | :--------------------------------------: | :------------------------------------------: | :------------------------------------: | :------------------------------------: |
| [@somii009](https://github.com/somii009) | [@jungmaan](https://github.com/jungmaan) | [@teawon](https://github.com/teawon) | [@ohhondgi](https://github.com/ohhondgi) | [@aswooo](https://github.com/aswooo) |
|               Frontend, Backend                |               Frontend                |                 Frontend, Backend                   |              Backend, DevOps               |              Backend, DevOps               |



