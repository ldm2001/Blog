ALTER TABLE `user`
    ADD COLUMN IF NOT EXISTS `refresh_token_expires_at` DATETIME NULL AFTER `refresh_token`;
