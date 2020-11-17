package com.findork.pdm.features.activity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ActivityDto implements Serializable {
    private Long id;
    private String name;
    private Long userId;
    private Long createdDate;
    private Long updatedDate;
}
