package study.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {

    /*
    //PK값을 @GeneratedValue로 안하고 직접 설정 하는 경우에 save() 메서드 에서 문제 발생
    Persistable 인터페이스 를 상속 받아서 isNew 메서드 를 구현 해야함
    
        @Transactional
        @Override
        public <S extends T> S save(S entity) {

            Assert.notNull(entity, "Entity must not be null");

            if (entityInformation.isNew(entity)) {
                entityManager.persist(entity);
                return entity;
            } else {
                return entityManager.merge(entity);
            }
        }
     */
    @Id //@GeneratedValue
    private String id;

    //isNew 메서드에서 비교로 사용
    @CreatedDate
    private LocalDateTime createdDate;

    public Item(String id){
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
