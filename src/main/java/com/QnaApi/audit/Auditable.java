package com.QnaApi.audit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// Auditable 클래스는 엔티티 클래스마다 공통으로 존재하는 엔티티 생성일, 수정일, 작성자 등의
// 필드를 공통화 한 뒤, 엔티티에 대한 이벤트 발생 시 해당 필드의 값을 자동으로 채워주는 기능을 합니다.
// AuditingEntityListener: 영속성 컨텍스트에 저장된 엔티티의 변경 사항이 flush되기 전에(데이터베이스에 저장되기 전에)
// 엔티티의 생성일시, 수정일시, 작성자 등 필드에 자동으로 값을 추가해준다.
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) //엔티티 클래스에 리스너를 추가


public abstract class Auditable {
    @CreatedDate// 엔티티가 생성된 날짜를 필드값에 추가
    @Column(name ="created_at",updatable = false)// 컬럼명은 created_at, 업데이트는 안되게
    private LocalDateTime createdAt;

    @LastModifiedDate //엔티티가 마지막으로 생성된 날짜를 필드값으로 추가
    @Column(name="LAST_MODIFIED_AT")
    private LocalDateTime modifiedAt;

    @CreatedBy // 엔티티를 생성한 주체를 필드에 추가해줌
    @Column(updatable = false) //업데이트는 할수없음
    private String createdBy;


}
