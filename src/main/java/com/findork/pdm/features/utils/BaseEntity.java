package com.findork.pdm.features.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@MappedSuperclass
@EqualsAndHashCode
public class BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

}
