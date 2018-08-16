  CREATE EVENT delete_notifications_after_30days
  ON SCHEDULE EVERY 1 DAY
  ON COMPLETION PRESERVE

DO
  BEGIN
    SET FOREIGN_KEY_CHECKS = 0;
    delete from jbugger.notification
    where notification_date > DATE_SUB(NOW(), INTERVAL 30 DAY);
    SET FOREIGN_KEY_CHECKS = 1;
  END;