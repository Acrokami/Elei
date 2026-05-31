package com.acrobtw.elei.consumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPayload {
    private String username;
    private String message;
    private String eventType;
}
