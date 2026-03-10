UPDATE `user`
SET
    `refresh_token` = NULL,
    `refresh_token_expires_at` = NULL
WHERE (`refresh_token` IS NULL AND `refresh_token_expires_at` IS NOT NULL)
   OR (`refresh_token` IS NOT NULL AND `refresh_token_expires_at` IS NULL);

ALTER TABLE `user`
    ADD CONSTRAINT `uk_user_nickname` UNIQUE (`nickname`),
    ADD CONSTRAINT `uk_user_tel_number` UNIQUE (`tel_number`),
    ADD CONSTRAINT `uk_user_refresh_token` UNIQUE (`refresh_token`),
    ADD CONSTRAINT `ck_user_refresh_token_pair`
        CHECK (
            (`refresh_token` IS NULL AND `refresh_token_expires_at` IS NULL)
            OR
            (`refresh_token` IS NOT NULL AND `refresh_token_expires_at` IS NOT NULL)
        );
