### DDD 시작하기 중요내용 요약

#### 1장 도메인 모델 시작하기
* 도메인이란 소프트웨어로 해결하고자 하는 문제 영역을 말한다.
    * 도메인은 여러 하위 도메인으로 구성 된다.
* 모델을 구성하는 핵심 구성요소, 규칙, 기능을 찾는 것이 도메인 모델링의 기본작업
* 모델은 엔티티와 밸류로 구분할 수 있다.
    * 엔티티
        * 식별자를 가진다
        * 엔티티를 삭제할 때까지 식별자는 유지
        * 엔티티 식별자의 실제 데이터는 String인 경우가 많은데, 이때도 밸류타입을 이용해서 해당 필드의 의미를 드러낼 수 있다
    * 밸류
        * 배송 서비스에서 받는 사람의 정보(여러 인스턴스변수)는 "받는 사람" 이라는 개념 하나를 표현하고 있음
        * 배송 서비스에서 주소의 정보(여러 인스턴스변수)는 "주소" 라는 개념 하나를 표현하고 있음
        * -> 따로 클래스 만들어서 관리
        * 밸류 타입의 장점은 개념을 확실하게 표현하여 코드의 의미를 더 잘 이해할 수 있게 하고, 밸류 타입 만을 위한 기능을 추가할 수 있는 것
        * 밸류 객체의 데이터를 변경할 때는 기존 데이터를 변경하기보다 변경한 데이터를 갖는 새로운 밸류 객체를 생성
            * 이러한 경우처럼 데이터 변경 기능을 제공하지 않는 타입을 불변 타입이라고 함.
            * 가장 중요한 이유는 안전한 코드를 작성할 수 있음
* 도메인 모델에 set 메서드를 사용하지 말라
    * set 메서드는 도메인의 핵심 개념이나 의도를 코드에서 사라지게 한다.
    * changeShippingInfo / setShippingInfo 무엇이 의도가 드러나는가?
    * set을 쓸 때는 private 으로 클래스 내부에서 데이터를 변경할 목적으로 써라

#### 2장 아키텍처 개요
* 네 개의 영역
    * 표현
        * 컨트롤러(객체 변환, JSON 변환)
    * 응용
        * 서비스
    * 도메인
        * 핵심 로직은 도메인 모델에서 구현
    * 인프라스트럭처
        * 구현 기술을 다룸
        * RDB 연동, 메시지 큐, 몽고 DB나 레디스
        * 논리적 개념보다는 실제 구현
* 상위 계층에서 하위 계층으로는 의존하지만, 하위 계층은 상위 계층으로 의존하지 않는다.
    * 표현 > 응용 > 도메인 > 인프라스트럭처
    * 구현저장하고 조회하는 기능을 정의
        * 리포지터리 인터페이스는 도메인 모델 영역에 속하고, 실제 구현 클래스는 인프라스트럭처 영역에 속한다
        * 기본적으로 repositry를 사용하는 주체가 응용 서비스이기 때문에 애그리거트를 저상위 개념으로 표현
        * 애그리거트는 군집에 속한 객체를 관리하는 루트 엔티티를 갖는데, 애그리거트를 사용하는 코드는 루트가 제공하는 기능을 제공하고 루트를 통해서 간접적으로 애그리거트 내의 다른 엔티티나 밸류에 접근
    * Repository
        * 도메인 모델의 영속성을 처리
        * repository는 애그리거트 단위로 도메인 객체를 거트 루트 식별자로 조회하는 메서드를 가져야 한다장하는 메소드와 애그리의 편리함을 위해 계층 구조를 유연하게 적용하기 도 한다(응용 > 인프라스트럭처)
* 도메인 영역의 주요 구성요소
    * Entity
        * 고유의 식별자를 갖는 객체로 자신의 라이프 사이클을 가짐. 도메인 모델의 데이터를 포함하며 해당 데이터와 관련된 기능을 함께 제공(주문, 회원)
        * 도메인 모델의 엔티티와 RDB의 엔티티는 다르다. 특히 도메인 모델의 엔티티는 데이터와 함께 도메인 기능(배송지 주소 변경 등)을 함께 제공
    * Value
        * 고유의 식별자를 가지지 않는 객체로 개념적으로 하나의 값을 표현할 때 사용한다. 엔티티의 속성이면서 다른 밸류 타입의 속성으로도 사용.(주소, 금액)
    * Aggregate
        * 연관된 엔티티와 밸류 객체를 개념적으로 하나로 묶은 것(주문과 관련된 엔티티들을 주문 애그리거트로 묶을 수 있음)
        * 상위 수준에서 모델을 관리하지 않고 개별 요소에만 초점을 맞추다 보면 큰 수준에서 모델을 이해하지 못하기 때문에 군집을 통해
    * Domain service
        * 특정 엔티티에 속하지 않은 도메인 로직을 제공(할인 금액 계산 등 다양한 조건을 이용해서 구현하는 것처럼 여러 엔티티와 밸류가 필요할 때 사용)
* 요청 처리 흐름
    * Controller > Service > Domain > Repository
* 인프라스트럭처는 구현의 편리함을 위해 적절히 의존할 수 있따. @Transactional 이나 #ntity 등을 서비스나 도메인 모델에 사용하는 것이 편리하다. 구현의 편리함은 설계의 완벽함보다 중요할 수도 있다!
* **패키지 구성 규칙에 정답이 존재하는 것은 아니다!**
    * 예시로 ui > application > domain < infrastructure 로 사용할 수 있다.
    * 도메인이 크면 각 도메인으로 directory(모듈)를 나누고 order > ui > application > domain < infra 형태로 구성할 수 있다.
    * 도메인 모듈은 애그리거트를 기준으로 다시 패키지를 구성할 수 있다.
    * catalog
        * ui
        * applicaton
        * domain
            * product
            * category
        * infra
    * 애그리거트, 도메인 모델, 리포지터리는 같은 패키지에 의치 시킨다.
    * 응용 서비스도 도메인 별로 패키지를 구분할 수 있다.
    * 정해진 규칙은 없지만 한 패키지에 너무 많은 타입이 몰려서 코드를 찾을 때 불편한 정도만 아니면 되고, 필자의 의견으로는 10~15개 미만으로 타입 개수를 유지하려고 노력함

#### 3장 애그리거트
* 애그리거트는 관련된 모델을 하나로 모았기 때문에 한 애그리거트에 속한 객체는 유사하거나 동일한 라이프 사이클을 가진다
* 애그리거트는 have a 관계가 아니다(상품과 리뷰는 함께 생성되거나 변경되지 않고 변경 주체도 다르다)
* 애그리거트에 속한 모든 객체가 일관된 상태를 유지하려면 애그리거트 전체를 관리할 주체가 필요한데, 이 책임을 지는 것이 루트 엔티티
* 일관성이 깨지지 않는 것이 중요하다 !
* 애그리거트 외부에서 애그리거트에 속한 객체를 직접 변경하면 안 된다. 이는 애그리거트가 강제하는 규칙을 적용할 수 없어 모델의 일관성을 깬다!
* 루트를 통해서만 도메인 로직을 구현하게 하려면,
    * 단순히 필드를 변경하는 set 메서드를 public 선언하지 않는다
        * set 메서드를 넣지 않으면 자연스럽게 cancel이나 change처럼 의미가 잘 드러나는 이름을 사용하는 빈도가 높아진다
    * 밸류 타입은 불변으로 구현한다
    * 불변으로 구현할 수 없다면 protected 선언하여 애그리거트 외부 상태 변경을 방지할 수 있다
* 트랜잭션의 범위
    * 트랜잭션의 범위는 작을 수록 좋다
    * 한 트랜잭션이 한 개 테이블을 수정하는 것과 세 개의 테이블을 수정하는 것을 비교하면 성능 차이가 발생한다
        * 한 개 테이블을 수정하면 트랜잭션 충돌을 막기 위해 잠그는 대상이 한 개 테이블의 한 행으로 한정되지만, 세 개의 테이블을 수정하면 잠금 대상이 많아지고 그만큼 동시에 처리할 수 있는 트랜잭션 개수가 줄어들어 처리량을 떨어뜨린다
        * 동일하게 한 트랜잭션에서는 한 개의 애그리거트만 수정해야 한다
        * 만약 수정해야 한다면, 애그리거트에서 수정하지 말고 응용 서비스 계층에서 두 애그리거트를 수정하도록 구현한다
* 애그리거트는 개념상 완전한 한 개의 도메인 모델을 표현하므로 객체의 영속성을 처리하는 리포지토리는 애그리거트 단위로 존재
* 한 객체가 다른 객체를 참조하는 것처럼 애그리거트도 다른 애그리거트를 참조한다. 애그리거트의 관리 주체는 루트이므로 애그리거트 > 애그리거트 참조는 다른 루트를 참조한다는 것과 같다
    * 여러 문제가 발생(의존 결합도 높임, 수정의 유혹, 성능 고민, 확장성)하는데 이럴 때는 RDB에서 외래키를 통해 참조하듯이 다른 애그리거트를 참조할 때는 루트 ID를 이용한다.
    * id 참조 방식에서 N+1 문제가 발생할 수도 있는데, 이때는 JPQL 등을 이용해서 그냥 JOIN 걸자
    * 애그리거트마다 다른 저장소를 사용하면 한 번의 쿼리로 관련 애그리거트를 조회할 수 없는데, 이때는 조회 성능을 높이기 위해 캐시를 적용하거나 조회 전용 저장소를 따로 구성
    * **애그리거트를 팩토리로 이용할 수도 있다!!**

#### 4장 JPA를 이용한 리포지터리 구현
* 리포지터리 인터페이스는 애그리거트와 같이 도메인 영역에 속하고, 리포지터리를 구현한 클래스는 인프라 스트럭처 영역에 속함
* 애그리거트와 JPA 매핑을 위한 기본 규칙
    * 애그리거트 루트는 엔티티이므로 @Entity 설정
    * 밸류는 @Embeddable 설정
    * 밸류 타입 프로퍼티는 @Embedded 설정
    * @Embeddable 설정의 컬럼 이름과 실제 컬럼 이름이 다를 때는 @AttributeOverrides 애너테이션으로 프로퍼티와 매핑할 칼럼 이름을 변경
    * JPA에서 @Entity와 @Embeddable 클래스를 매핑하려면 기본 생성자를 제공해야 함. 불변 타입이라 기본 생성자는 필요 없지만 JPA 매핑을 위해 추가해야 함
    * 하이버네이트는 @Access 설정으로 명시적으로 접근 방식을 지정하지 않으면 @Id || @EmbeddedId 위치에 따라 접근 방식 결정. 필드에 위치하면 필드 접근, get 메서드에 접근하면 메서드 접근
    * 식별자 칼럼을 만들 때 GeneratedValue를 사용하면, 도메인 객체를 리포지터리에 저장할 때 식별자가 생성된다. 즉, 도메인 객체를 생성하는 시점에는 식별자를 알 수 없음.
        * UUID로 식별자 칼럼을 만드는 건 어떨까.
    * DIP에 따르면 @Entity @Table은 구현 기술에 속하므로 도메인 모델은 구현 기술인 JPA에 의존하지 말아야 하는데, 의존하고 있따. 리포지터리 인터페이스도 마찬가지로
      도메인 패키지에 있지만 구현 기술인 스프링 데이터 JPA를 의존하므로, 도메인이 인프라에 의존중이다. 하지만 DIP를 적용하는 주된 이유는 저수준 구현이 바뀌더라도 고수준이
      영향을 받지 않도록 하기 위함이다. 리포지터리와 도메인 모델의 구현 기술은 거의 바뀌지 않는다. 이렇게 바뀌지 않을 변경을 대비하는 것은 과하다. 타협을 하자.
      DIP를 완벽하지 지키면 좋겠지만, 개발 편의성과 실용성을 가져가면서 구조적인 유연함을 유지하려면 JPA에 의존하는 것이 합리적인 선택이다.

#### 5장 스프링 데이터 JPA를 이용한 조회 기능
* CQRS(Command and Query Responsibility Segregation)
    * 명령 모델과 조회 모델을 분리하는 패턴
    * 명령 모델은 상태를 변경하는 기능, 조회 모델은 데이터를 조회하는 기능
    * 회원 가입, 암호 변경, 주문 취소 처럼 상태를 변경하는 기능을 명령 모델
    * 주문 목록, 주문 상세처럼 데이터를 보여주는 기능은 조회 모델
    * 즉, 명령 모델은 주로 도메인 모델이 사용된다.

#### 6장 응용 서비스와 표현 영역
* 표현 영역은 사용자의 요청을 해석한다.
    * HTTP 요청이 표현 영역에 전달된다. 표현 영역은 URL, 요청 파라미터, 쿠키, 헤더 등을 이용해서 사용자가 실행하고 싶은 기능을 판별하고 그 기능을 제공하는 응용 서비스를 실행
    * 표현 영역은 응용 서비스의 실행 결과를 사용자에게 알맞은 형태(HTML, JSON)로 응답한다.
* 응용 서비스 영역은 요청을 위한 기능을 제공하는 주체다
    * 기능을 실행하는 데 필요한 입력값을 메서드 인자로 받고 실행 결과를 리턴한다
    * 응용 서비스의 메서드가 요구하는 파라미터와 표현 영역이 사용자로부터 전달받은 데이터 영역이 일치하지 않기 때문에 표현 영역은 응용 서비스가 요구하는 형식으로
      사용자 요청을 변환한다.
    * 응용 서비스의 역할은 도매인 객체를 사용해서 사용자의 요청을 처리한다.
    * 리포지터리에서 애그리거트를 구하고, 애그리거트의 도메인 기능을 실행하고, 결과를 리턴한다.
    * 만약 응용 서비스가 복잡하다면 응용 서비스에서 도메인 로직의 일부를 구현하고 있을 가능성이 높다.
    * 응용 서비스는 트랜잭션 처리도 담당한다.만약 Member 객체의 상태를 변경했는데 DB에 반영하는 도중 문제가 발생하면 일부 Member 만 차단 상태가 되어 데이터 일관성이 깨진다.
      이런 상황이 발생하지 않으려면 트랜잭션 범위에서 응용 서비스를 실행해야 한다.
    * 접근 제어와 이벤트 처리도 담당한다.
    * 응용 서비스 영역에 도메인 로직을 넣으면 안 된다. 애그리거트에서 구현해야 한다.
* 응용 서비스의 크기
    * 회원 도메인의 모든 기능을 한 응용 서비스 클래스에서 구현한다면코드 중복을 제거하기 쉽지만 한 서비스 클래스의 크기가 커진다.
        * 연관 관계가 적은 도메인에 대해 의존하게 될 수도 있다.
    * 구분되는 기능 별로 서비스 클래스를 구현하는 방식은 한 응용 서비스 클래스에서 한 개 내지 2~3개의 기능을 구현한다.
        * 클래스 개수는 많아지지만 코드 품질을 일정 수준으로 유지한다. 필요한 의존 객체만 포함하므로 다른 기능을 구현한 코드에 영향 받지 않는다.
* 응용 서비스를 구현할 때 논쟁이 되는 것이 인터페이스가 필요한 지다.
    * 인터페이스가 필요한 몇 가지 상황이 있는데 그중 하나는 구현 클래스가 여러개인 경우다. 구현 클래스가 다수 존재하거나 런타임에 구현 객체를 교체해야 할 때 인터페이스를 유용하게 사용한다.
    * 하지만 응용 서비스는 런타임에 교체하는 경우가 거의 없고 한 응용 서비스의 구현 클래스가 두 개인 경우도 드물다.
    * 이런 이유로 인터페이스와 클래스를 따로 구현하면 소스 파일만 많아지고 구현 클래스에 대한 간접 참조가 증가해서 전체 구조가 복잡해진다.
    * 따라서 인터페이스가 명확하게 필요하기 전까지는 응용 서비스에 대한 인터페이스를 작성하는 것이 좋은 선택이라고 볼 수는 없다.
    * TDD나 컨트롤러부터 구현한다면, 인터페이스를 이용할 수 있긴 하다.
* **응용 서비스의 파라미터 타입을 결정할 때 주의할 점은 표현 영역과 관련된 타입을 사용하면 안 된다는 것이다....**
    * 테스트가 어려워진다 ㅠㅠ
    * 또 응용 서비스가 표현 영역의 역할까지 대신한다 ㅠㅠ
    * Session 이나 쿠키는 표현 영역의 상태인데, 이 상태를 응용 서비스에서 변경해버리면 표현 영역의 코드만으로 표현 영역의 상태가 어떻게 변경되는지 추적하기 어렵다.
* 표현 영역의 책임
    * 사용자가 시스템을 사용할 수 있는 흐름(화면)을 제공하고 관리
    * 사용자의 요청을 알맞은 응용 서비스에 전달하고 결과를 사용자에게 제공
    * 사용자의 세션을 관리
* 값 검증은 표현 영역과 응용 서비스 영역 두 곳에서 모두 수행할 수 있다. 원칙적으로는 응용 서비스에서 한다.
* 권한 검사 같은 경우 프레임워크(스프링 시큐리티)의 확장을 원하는 수준으로 할 수 없다면 도메인에 맞는 권한 검사 기능을 직접 구현하는 것이 코드 유지 보수에 유리하다.
* 응용 서비스가 사용자 요청 기능을 실행하는 데 별다른 기여를 하지 못하면(조회 전용 기능) 굳이 서비스 영역을 거치지 않아도 된다.

#### 7장 도메인 서비스
* 도메인 영역의 코드를 작성하다 보면 한 애그리거트로 기능을 구현할 수 없을 때가 있다.
* 이런 문제를 해소하는 가장 쉬운 방법은 도메인 기능을 별도 서비스로 구현하는 것.
* 도메인 서비스
    * 계산 로직 : 여러 애그리거트가 필요하거나 복잡한 계산 로직
    * 외부 시스템 연동이 필요한 도메인 로직
    * 도메인 서비스 객체를 애그리거트에 주입하면 안 됨
        * 메서드에서 필요한 경우 파라미터로 사용하는 방식으로 쓰기. 애그리거트 루트에서 제공하는 모든 기능에서 도메인 서비스 객체를 사용하지 않기 때문에.

#### 9장 도메인 모델과 바운디드 컨텍스트
* 도메인을 완벽하게 표현하는 단일 모델을 만드는 것은 불가능
    * 재고 관리에서 상품과 주문에서 상품, 배송에서 상품은 다 다르다.
    * 논리적으로 같지만 하위 도메인에 따라 다른 용어를 사용할 수도 있다
    * 모델은 컨텍스트 아래에서 완전한 의미를 가진다.
    * 이러한 경계를 바운디드 컨텍스트라고 한다.
* 바운디드 컨텍스트
    * 바운디드 컨텍스트는 기업의 팀 조직 구조에 따라 결정되기도 한다.
    * 규모가 작은 기업에서는 전체 스티템을 한 개 팀에서 구현하기도 하며, 여러 하위 도메인을 한 개의 바운드디 컨텍스트에서 구현하기도 함
    * 한 개의 바운디드 컨텍스트가 여러 하위 도메인을 포함하더라도 하위 도메인마다 구분되는 패키지를 갖도록 구현해야 한다
* 바운디드 컨텍스트는 도메인은 기능을 제공하는 데 필요한 모든 요소를 포함한다.
    * 표현 응용 도메인 인프라스트럭처 DBMS 모두가 포함됨
    * 모든 바운디트 컨텍스트를 반드시 도메인 주도로 개발할 필요는 없으며, 복잡하지 않은 도메인 로직은 CRUD 방식의 DAO와 데이터 중심으로 구현해도 된다.
#### 10장 이벤트
* 바운디드 컨텍스트 간의 강결합을 해결하는 방법이 이벤트
* 이벤트라는 용어는 '과거에 벌어진 어떤 일'을 의미
* 이벤트 생성 주체 -> 이벤트 디스패처 -> 이벤트 핸들러
    * 도메인 모델에서 이벤트 생성 주체는 엔티티, 밸류, 도메인 서비스와 같은 도메인 객체임. 도메인 객체는 이벤트가 발생하면 도메인 로직을 실행해서 이벤트를 발생
    * 이벤트 핸들러는 이벤트 생성 주체가 발생한 이벤트에 반응.
    * 이벤트 디스패처는 핸들러와 생성 주체를 연결. 동기나 비동기로 실행
* 이벤트 클래스는 과거 시제를 사용
* @EventListener 어노테이션
* 이벤트 클래스는 딱 필요한 속성만을 가짐
* Push API를 이벤트로 사용할 수 있지 않을까?
* 모든 이벤트가 공통으로 갖는 프로퍼티가 존재하면 상위 클래스로 관리할 수 있다
* 이벤트 클래스는 Events 클래스를 상속 받아서 구현
* 이벤트 발생과 출판을 위해서 ApplicationEventPublisher를 사용한다. 스프링에서.
* 이벤트 클래스 설정 관련해서는 책 p314를 참고해서 다시 보자.
* 핸들러는 @EventListener 어노테이션.
* 이벤트를 사용하기 위한 프로세스
    * 이벤트 클래스 생성(Events 상속) -> EventsConfig -> ApplicationEventPublisher 설정 -> 이벤트 핸들러에 @EventListener 설정
    * 도메인 기능을 실행
    * 도메인 기능은 Events.raise()를 이용해서 이벤트 발생
    * EVents.raise() 는 스프링이 제공하는 ApplicationEventPublisher를 이용해서 이벤트를 출판
    * ApplicationEventPublisher는 @EventListener(이벤트타입.class) 애너테이션을 찾아 실행