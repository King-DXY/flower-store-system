-- ============================================================
-- 鲜花商城 — 数据库升级脚本（适配真实库结构）
-- 安全：可重复执行，已存在的列/表会报错 → 忽略即可
-- ============================================================

-- ========== 1. 新建 order_item 表 ==========
-- 作用：一个订单可以买多种花，每种花一条记录
CREATE TABLE IF NOT EXISTS order_item (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  order_id INT NOT NULL COMMENT '关联 orders.order_id',
  flower_id INT NOT NULL COMMENT '关联 flower.flower_id',
  flower_name VARCHAR(100) NOT NULL COMMENT '鲜花名称（方便展示）',
  unit_price DECIMAL(10,2) NOT NULL COMMENT '购买时的单价',
  quantity INT NOT NULL COMMENT '购买数量',
  subtotal DECIMAL(10,2) NOT NULL COMMENT '小计',
  INDEX idx_order_id (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细';

-- ========== 2. 新建 cart 表 ==========
-- 作用：购物车持久化（Phase 2 迁到 Redis）
CREATE TABLE IF NOT EXISTS cart (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT NOT NULL COMMENT '顾客ID',
  flower_id INT NOT NULL COMMENT '鲜花ID',
  store_id INT NOT NULL COMMENT '店铺ID',
  quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  INDEX idx_customer (customer_id),
  UNIQUE KEY uk_customer_flower (customer_id, flower_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车';

-- ========== 3. 给 customer 加列 ==========
-- 新密码列（后续改为 BCrypt 加密）
ALTER TABLE customer ADD COLUMN password VARCHAR(255) DEFAULT NULL COMMENT 'BCrypt密码(后续启用)';
-- 逻辑删除标记
ALTER TABLE customer ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除';

-- ========== 4. 给 flowerstore 加列 ==========
ALTER TABLE flowerstore ADD COLUMN password VARCHAR(255) DEFAULT NULL COMMENT 'BCrypt密码(后续启用)';
ALTER TABLE flowerstore ADD COLUMN image_url VARCHAR(512) DEFAULT NULL COMMENT '店铺图片URL';
ALTER TABLE flowerstore ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除';

-- ========== 5. 给 flower 加列 ==========
ALTER TABLE flower ADD COLUMN image_url VARCHAR(512) DEFAULT NULL COMMENT '鲜花图片URL';
ALTER TABLE flower ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除';

-- ========== 6. 给 orders 加列 ==========
ALTER TABLE orders ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除';

-- ============================================================
-- 完成！如果有 "Duplicate column" 报错，说明列已存在，忽略即可
-- ============================================================
