-- 添加默认管理员账户
-- 用户名: admin
-- 密码: admin123
INSERT INTO `ttd_user_account` (`user_account`, `password`, `crt_time`, `upt_time`, `deleted`)
VALUES ('admin', 'admin123', NOW(), NOW(), 0);
