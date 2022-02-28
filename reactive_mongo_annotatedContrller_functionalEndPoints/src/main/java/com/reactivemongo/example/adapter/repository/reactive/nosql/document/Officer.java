package com.reactivemongo.example.adapter.repository.reactive.nosql.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Setter
@Getter
@Document(value = "Officer")
public class Officer {

    @Id
    private String id;
    private RANK rank;
    private String first;
    private String last;

    public Officer(RANK rank,String first,String last){

        this.rank = rank;
        this.first = first;
        this.last = last;
    }

    @Override
    public String toString() {
        return "Officer{" +
                "id='" + id + '\'' +
                ", rank=" + rank +
                ", first='" + first + '\'' +
                ", last='" + last + '\'' +
                '}';
    }
}
