-- chap 24 changes- add ticket comment attachments and retrofit tables.  add 2 join tablesCREATE TABLE `userprincipal` (
-- pg 721
-- copy of attachment table prior to changes
CREATE TABLE `attachment` (
  `AttachmentId` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `TicketId` bigint(20) unsigned NOT NULL,
  `AttachmentName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `MimeContentType` varchar(255) COLLATE utf8_unicode_ci NOT NULL,
  `Contents` blob NOT NULL,
  PRIMARY KEY (`AttachmentId`),
  KEY `Attachment_TicketId` (`TicketId`),
  CONSTRAINT `Attachment_TicketId` FOREIGN KEY (`TicketId`) REFERENCES `ticket` (`TicketId`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

use customersupport;

-- new join table 1 for attachments on tickets
CREATE TABLE Ticket_Attachment (
  SortKey SMALLINT NOT NULL,
  TicketId BIGINT UNSIGNED NOT NULL,
  AttachmentId BIGINT UNSIGNED NOT NULL,
  CONSTRAINT Ticket_Attachment_Ticket FOREIGN KEY (TicketId)
    REFERENCES Ticket (TicketId) ON DELETE CASCADE,
  CONSTRAINT Ticket_Attachment_Attachment FOREIGN KEY (AttachmentId)
    REFERENCES Attachment (AttachmentId) ON DELETE CASCADE,
  INDEX Ticket_OrderedAttachments (TicketId, SortKey, AttachmentId)
) ENGINE = InnoDB;

-- new join table 2 for attachments on ticketComments
use customersupport; 
CREATE TABLE TicketComment_Attachment (
  SortKey SMALLINT NOT NULL,
  CommentId BIGINT UNSIGNED NOT NULL,
  AttachmentId BIGINT UNSIGNED NOT NULL,
  CONSTRAINT TicketComment_Attachment_Comment FOREIGN KEY (CommentId)
    REFERENCES TicketComment (CommentId) ON DELETE CASCADE,
  CONSTRAINT TicketComment_Attachment_Attachment FOREIGN KEY (AttachmentId)
    REFERENCES Attachment (AttachmentId) ON DELETE CASCADE,
  INDEX TicketComment_OrderedAttachments (CommentId, SortKey, AttachmentId)
) ENGINE = InnoDB;

-- and lastly transfer data from attachments and get rid of ticketId ref that is now in part of join tables above
-- Run these statements if the Attachment table already exists with a TicketId column
-- note you have to execute these one at a tim in order.  highlight each block
-- execute it, then move on to the next- 6 total
INSERT INTO Ticket_Attachment (SortKey, TicketId, AttachmentId)
    SELECT @rn := @rn + 1, TicketId, AttachmentId
        FROM Attachment, (SELECT @rn:=0) x
        ORDER BY TicketId, AttachmentName;
CREATE TEMPORARY TABLE $minSortKeys ENGINE = Memory (
  SELECT min(SortKey) as SortKey, TicketId FROM Ticket_Attachment GROUP BY TicketId
);
UPDATE Ticket_Attachment a SET a.SortKey = a.SortKey - (
  SELECT x.SortKey FROM $minSortKeys x WHERE x.TicketId = a.TicketId
) WHERE TicketId > 0;
DROP TABLE $minSortKeys;
ALTER TABLE Attachment DROP FOREIGN KEY Attachment_TicketId;
ALTER TABLE Attachment DROP COLUMN TicketId;
