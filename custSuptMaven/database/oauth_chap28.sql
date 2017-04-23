CREATE TABLE WebServiceClient (
  WebServiceClientId BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  ClientId VARCHAR(50) NOT NULL,
  ClientSecret VARCHAR(60) NOT NULL,
  UNIQUE KEY WebServiceClient_ClientId (ClientId)
);

CREATE TABLE WebServiceClient_Scope (
  WebServiceClientId BIGINT UNSIGNED NOT NULL,
  Scope VARCHAR(100) NOT NULL,
  UNIQUE KEY WebServiceClient_Scopes_Client_Scope (WebServiceClientId, Scope),
  CONSTRAINT WebServiceClient_Scopes_ClientId FOREIGN KEY (WebServiceClientId)
    REFERENCES WebServiceClient (WebServiceClientId) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE WebServiceClient_Grant (
  WebServiceClientId BIGINT UNSIGNED NOT NULL,
  GrantName VARCHAR(100) NOT NULL,
  UNIQUE KEY WebServiceClient_Grants_Client_Grant (WebServiceClientId, GrantName),
  CONSTRAINT WebServiceClient_Grants_ClientId FOREIGN KEY (WebServiceClientId)
    REFERENCES WebServiceClient (WebServiceClientId) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE WebServiceClient_RedirectUri (
  WebServiceClientId BIGINT UNSIGNED NOT NULL,
  Uri VARCHAR(1024) NOT NULL,
  CONSTRAINT WebServiceClient_Uris_ClientId FOREIGN KEY (WebServiceClientId)
      REFERENCES WebServiceClient (WebServiceClientId) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE OAuthAccessToken (
  OAuthAccessTokenId BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  TokenKey VARCHAR(50) NOT NULL,
  Value VARCHAR(50) NOT NULL,
  Expiration TIMESTAMP NULL,
  Authentication BLOB NOT NULL,
  UNIQUE KEY OAuthAccessToken_TokenKey (TokenKey),
  UNIQUE KEY OAuthAccessToken_Value (Value)
) ENGINE = InnoDB;

CREATE TABLE OAuthAccessToken_Scope (
  OAuthAccessTokenId BIGINT UNSIGNED NOT NULL,
  Scope VARCHAR(100) NOT NULL,
  UNIQUE KEY OAuthAccessToken_Scopes_Token_Scope (OAuthAccessTokenId, Scope),
  CONSTRAINT OAuthAccessToken_Scopes_TokenId FOREIGN KEY (OAuthAccessTokenId)
    REFERENCES OAuthAccessToken (OAuthAccessTokenId) ON DELETE CASCADE
) ENGINE = InnoDB;

CREATE TABLE OAuthNonce (
  OAuthNonceId BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Value VARCHAR(50),
  NonceTimestamp BIGINT NOT NULL,
  UNIQUE KEY OAuthNonce_Value_Timestamp (Value, NonceTimestamp)
) ENGINE = InnoDB;


-- Run these statements to add the default OAuth data to the database
INSERT INTO UserPrincipal_Authority (UserId, Authority)
  VALUES (1, 'USE_WEB_SERVICES'), (4, 'USE_WEB_SERVICES');

INSERT INTO WebServiceClient (ClientId, ClientSecret) VALUES ( -- y471l12D2y55U5558rd2
    'TestClient', '$2a$10$elDBcfb/ZKyuNgOPK5.70Oi4gN2EuhU2yONPsoF3avx9.Hd/b8BTa'
);

INSERT INTO WebServiceClient_Scope (WebServiceClientId, Scope)
  VALUES (1, 'READ'), (1, 'WRITE'), (1, 'TRUST');

INSERT INTO WebServiceClient_Grant (WebServiceClientId, GrantName)
  VALUES (1, 'authorization_code');

INSERT INTO WebServiceClient_RedirectUri (WebServiceClientId, Uri)
  VALUES (1, 'http://localhost:8080/client/support');