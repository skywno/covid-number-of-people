package com.example.covid.domain;

import com.example.covid.constant.LocationType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@ToString
@EqualsAndHashCode
@Table(indexes = {
        @Index(columnList = "locationName"),
        @Index(columnList = "address"),
        @Index(columnList = "phoneNumber"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "modifiedAt")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Setter
    @Column(nullable = false, columnDefinition = "varchar default 'COMMON'")
    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    @Setter
    @Column(nullable = false)
    private String locationName;

    @Setter
    @Column(nullable = false)
    private String address;

    @Setter
    @Column(nullable = false)
    private String phoneNumber;

    @Setter
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer capacity;


    @Column(nullable = false, insertable = false, updatable = false,
            columnDefinition = "datetime default CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false, insertable = false, updatable = false,
            columnDefinition = "datetime default CURRENT_TIMESTAMP on update " +
                    "CURRENT_TIMESTAMP")
    @LastModifiedDate
    private LocalDateTime modifiedAt;


    protected Location() {
    }

    protected Location(
            LocationType locationType,
            String locationName,
            String address,
            String phoneNumber,
            Integer capacity
    ) {
        this.locationType = locationType;
        this.locationName = locationName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.capacity = capacity;
    }

    public static Location of(
            LocationType locationType,
            String locationName,
            String address,
            String phoneNumber,
            Integer capacity
    ) {
        return new Location(locationType, locationName, address, phoneNumber,
                capacity);
    }
}