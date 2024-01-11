package com.dontbe.www.DontBeServer.api.notification.repository;

import com.dontbe.www.DontBeServer.api.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
