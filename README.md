# Chicken Kiosk Java

Java Swing을 활용하여 구현한 **치킨 주문 키오스크 데스크톱 애플리케이션**입니다.

학교 Java 프로그래밍 과목에서 진행한 프로젝트로, 메뉴 이미지 표시, 메뉴별 수량 조절, 주문 내역 계산, 주문·초기화·종료 기능을 구현했습니다.

---

## 주요 기능

- 치킨 메뉴 8종 이미지 표시
- 메뉴별 수량 증가 / 감소
- 메뉴별 단가 및 소계 계산
- 전체 주문 금액 자동 계산
- 주문 내역 확인
- 주문 완료 메시지 출력
- 주문 초기화
- 프로그램 종료 확인

---

## 사용 기술

- Java
- Java Swing
- AWT Event Handling
- Maven
- Git / GitHub

---

## 프로젝트 구조

```text
Chicken_Kiosk_Java/
├─ README.md
├─ .gitignore
├─ pom.xml
├─ screenshots/
│  └─ demo.png
└─ src/
   └─ main/
      ├─ java/
      │  └─ com/porlaumenart/chickenkiosk/
      │     └─ ChickenKiosk.java
      └─ resources/
         └─ images/
            ├─ fried.jpg
            ├─ hot-fried.jpg
            ├─ bburinkle.jpg
            ├─ hot-bburinkle.jpg
            ├─ goldking.jpg
            ├─ poteking.jpg
            ├─ yangnyeom.jpg
            └─ matchoking.jpg
```

---

## 실행 환경

```text
JDK 11 이상
Maven 3.x
```

---

## 실행 방법

### 1. 저장소 Clone

```bash
git clone https://github.com/PorlauMenart/Chicken_Kiosk_Java.git
cd Chicken_Kiosk_Java
```

### 2. 프로젝트 빌드

```bash
mvn clean package
```

### 3. 실행

```bash
java -jar target/chicken-kiosk-1.0.0.jar
```

또는 Eclipse / IntelliJ에서 `ChickenKiosk.java`의 `main()` 메서드를 직접 실행할 수 있습니다.

---

## 이미지 리소스

메뉴 이미지는 `src/main/resources/images/`에 포함되어 있으며, 실행 시 JAR 내부의 리소스를 자동으로 불러옵니다.

따라서 별도의 이미지 경로 설정 없이 GitHub에서 프로젝트를 내려받아 빌드해도 메뉴 이미지가 함께 표시됩니다.

---

## 동작 구조

```text
메뉴 선택
   ↓
수량 변경
   ↓
메뉴별 소계 계산
   ↓
주문 내역 및 총 금액 표시
   ↓
주문 / 초기화 / 종료
```

---

## 프로젝트를 통해 학습한 내용

- Java Swing 기반 GUI 구성
- 이미지 리소스 로딩
- 버튼 및 메뉴 이벤트 처리
- 배열을 이용한 메뉴별 상태 관리
- 주문 금액 계산 및 화면 갱신

---

## 참고사항

본 프로젝트는 Java GUI 프로그래밍과 이벤트 처리 방식을 학습하기 위해 제작한 학교 프로젝트입니다.

이미지 파일을 JAR 리소스로 포함하도록 구성해 다른 환경에서도 상대경로 문제 없이 실행할 수 있도록 했습니다.

---

## Author

**PorlauMenart**
