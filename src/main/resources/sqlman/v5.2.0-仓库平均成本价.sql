-- 实时平均成本价表
CREATE TABLE `storehouse_style_cost_price` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `storehouse_id` varchar(32) NOT NULL COMMENT '仓库ID',
  `style_id` varchar(32) NOT NULL COMMENT '款号ID',
  `avg_price` decimal(16,2) NOT NULL COMMENT '平均成本价',
  `sum_quantity` decimal(16,2) NOT NULL COMMENT '总数量',
  `sum_cost` decimal(16,2) NOT NULL COMMENT '总成本',
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '更新版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=772 DEFAULT CHARSET=utf8mb4;

ALTER TABLE `storehouse_style_cost_price` ADD UNIQUE KEY `UK_sscp_storehouseId_styleId` (`storehouse_id`,`style_id`) USING HASH;
ALTER TABLE `storehouse_style_cost_price` ADD KEY `IDX_sscp_storehouseId` (`storehouse_id`) USING HASH;
ALTER TABLE `storehouse_style_cost_price` ADD KEY `IDX_sscp_styleId` (`style_id`) USING HASH;

-- 历史平均成本价表
CREATE TABLE `storehouse_style_cost_price_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `storehouse_id` varchar(32) NOT NULL COMMENT '仓库ID',
  `style_id` varchar(32) NOT NULL COMMENT '款号ID',
  `avg_price` decimal(16,2) NOT NULL COMMENT '截至该记录生成时间的平均成本价',
  `sum_quantity` decimal(16,2) NOT NULL COMMENT '截至该记录生成时间的总数量',
  `sum_cost` decimal(16,2) NOT NULL COMMENT '截至该记录生成时间的总成本',
  `date_computed` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '计算日期',
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录保存时间',
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '记录版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=772 DEFAULT CHARSET=utf8mb4;

ALTER TABLE `storehouse_style_cost_price_history` ADD KEY `IDX_sscph_storehouseId` (`storehouse_id`) USING HASH;
ALTER TABLE `storehouse_style_cost_price_history` ADD KEY `IDX_sscph_styleId` (`style_id`) USING HASH;
ALTER TABLE `storehouse_style_cost_price_history` ADD KEY `IDX_sscph_storehouseId_styleId` (`storehouse_id`,`style_id`) USING HASH;

