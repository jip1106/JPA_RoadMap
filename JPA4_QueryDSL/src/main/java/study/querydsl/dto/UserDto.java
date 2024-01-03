package study.querydsl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {

    private String name;
    private int age;
    private int realAge;

    public UserDto(String name, int age){
        this.name = name;
        this.age = age;
    }

    public UserDto(String name, int age, int realAge){
        this.name = name;
        this.age = age;
        this.realAge = realAge;
    }
}
