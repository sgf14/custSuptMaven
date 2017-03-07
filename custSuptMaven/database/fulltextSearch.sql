-- Chapter 23, pg 678, or eclise customer-support-v17/database folder
-- add fulltext/ search engine style search function to cust-supt db

USE CustomerSupport;

ALTER TABLE Ticket ADD FULLTEXT INDEX Ticket_Search (Subject, Body);
ALTER TABLE TicketComment ADD FULLTEXT INDEX TicketComment_Search (Body);
