-- added these all as separate commands, in order
-- item 1- add spring columns to existing user pricipal table- pg 806
ALTER TABLE UserPrincipal
  ADD COLUMN AccountNonExpired BOOLEAN NOT NULL DEFAULT TRUE,
  ADD COLUMN AccountNonLocked BOOLEAN NOT NULL DEFAULT TRUE,
  ADD COLUMN CredentialsNonExpired BOOLEAN NOT NULL DEFAULT TRUE,
  ADD COLUMN Enabled BOOLEAN NOT NULL DEFAULT TRUE;

-- item 2- create a new table for listing authorities
CREATE TABLE UserPrincipal_Authority (
  UserId BIGINT UNSIGNED NOT NULL,
  Authority VARCHAR(100) NOT NULL,
  UNIQUE KEY UserPrincipal_Authority_User_Authority (UserId, Authority),
  CONSTRAINT UserPrincipal_Authority_UserId FOREIGN KEY (UserId)
    REFERENCES UserPrincipal (UserId) ON DELETE CASCADE
) ENGINE = InnoDB;


-- item 3 add some authorities
-- Nicholas, a user
INSERT INTO UserPrincipal_Authority (UserId, Authority)
  VALUES (1, 'VIEW_TICKETS'), (1, 'VIEW_TICKET'), (1, 'CREATE_TICKET'),
    (1, 'EDIT_OWN_TICKET'), (1, 'VIEW_COMMENTS'), (1, 'CREATE_COMMENT'),
    (1, 'EDIT_OWN_COMMENT'), (1, 'VIEW_ATTACHMENT'), (1, 'CREATE_CHAT_REQUEST'),
    (1, 'CHAT');

-- sarah, a user
INSERT INTO UserPrincipal_Authority (UserId, Authority)
  VALUES (2, 'VIEW_TICKETS'), (2, 'VIEW_TICKET'), (2, 'CREATE_TICKET'),
    (2, 'EDIT_OWN_TICKET'), (2, 'VIEW_COMMENTS'), (2, 'CREATE_COMMENT'),
    (2, 'EDIT_OWN_COMMENT'), (2, 'VIEW_ATTACHMENT'), (2, 'CREATE_CHAT_REQUEST'),
    (2, 'CHAT');

-- mike, a user
INSERT INTO UserPrincipal_Authority (UserId, Authority)
  VALUES (3, 'VIEW_TICKETS'), (3, 'VIEW_TICKET'), (3, 'CREATE_TICKET'),
    (3, 'EDIT_OWN_TICKET'), (3, 'VIEW_COMMENTS'), (3, 'CREATE_COMMENT'),
    (3, 'EDIT_OWN_COMMENT'), (3, 'VIEW_ATTACHMENT'), (3, 'CREATE_CHAT_REQUEST'),
    (3, 'CHAT');

-- john, an admin
INSERT INTO UserPrincipal_Authority (UserId, Authority)
  VALUES (4, 'VIEW_TICKETS'), (4, 'VIEW_TICKET'), (4, 'CREATE_TICKET'),
    (4, 'EDIT_OWN_TICKET'), (4, 'VIEW_COMMENTS'), (4, 'CREATE_COMMENT'),
    (4, 'EDIT_OWN_COMMENT'), (4, 'VIEW_ATTACHMENT'), (4, 'CREATE_CHAT_REQUEST'),
    (4, 'CHAT'), (4, 'EDIT_ANY_TICKET'), (4, 'DELETE_TICKET'),
    (4, 'EDIT_ANY_COMMENT'), (4, 'DELETE_COMMENT'), (4, 'VIEW_USER_SESSIONS'),
    (4, 'VIEW_CHAT_REQUESTS'), (4, 'START_CHAT');