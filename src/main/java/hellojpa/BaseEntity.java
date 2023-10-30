package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass   //맵핑 정보만 받는 부모 클래스
public abstract class BaseEntity {
    private String createdBy;
    private String modifiedBy;
    private LocalDateTime createDate;
    private LocalDateTime lastModifiedDate;
}
