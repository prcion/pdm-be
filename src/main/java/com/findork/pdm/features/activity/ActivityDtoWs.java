package com.findork.pdm.features.activity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDtoWs {
    private String event;
    private ActivityDto payload;
}
