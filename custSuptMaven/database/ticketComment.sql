-- chap 22- Add a new ticket comment table to existing db

USE customersupport;

CREATE TABLE TicketComment (
  CommentId BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  TicketId BIGINT UNSIGNED NOT NULL,
  UserId BIGINT UNSIGNED NOT NULL,
  Body TEXT,
  DateCreated TIMESTAMP(6) NULL,
  CONSTRAINT TicketComment_UserId FOREIGN KEY (UserId)
    REFERENCES UserPrincipal (UserId) ON DELETE CASCADE ,
  CONSTRAINT TicketComment_TicketId FOREIGN KEY (TicketId)
    REFERENCES Ticket (TicketId) ON DELETE CASCADE
) ENGINE = InnoDB;
