CREATE TABLE `storehouse_style_cost_price_history` (
  `id` varchar(32) NOT NULL COMMENT '记录ID',
  `storehouse_id` varchar(32) NOT NULL COMMENT '仓库ID',
  `style_id` varchar(32) NOT NULL COMMENT '款号ID',
  `the_quantity` decimal(16,2) NOT NULL COMMENT '本次数量',
  `the_price` decimal(16,2) NOT NULL COMMENT '本次价格',
  `avg_price` decimal(10,2) NOT NULL COMMENT '截至该记录生成时间的平均成本价',
  `sum_quantity` decimal(16,2) NOT NULL COMMENT '截至该记录生成时间的总数量',
  `sum_cost` decimal(16,2) NOT NULL COMMENT '截至该记录生成时间的总成本',
  `date_computed` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '计算日期',
  `source_type` varchar(24) NOT NULL COMMENT '变动源类型：采购/出货',
  `source_id` varchar(32) NOT NULL COMMENT '变动源id',
  `date_created` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录保存时间',
  `last_updated` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '记录版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库款号成本价历史表';

ALTER TABLE storehouse_style_cost_price_history ADD KEY `IDX_sscph_storehouseId` (`storehouse_id`) USING HASH;

ALTER TABLE storehouse_style_cost_price_history ADD KEY `IDX_sscph_styleId` (`style_id`) USING HASH;

ALTER TABLE storehouse_style_cost_price_history ADD KEY `IDX_sscph_storehouseId_styleId` (`storehouse_id`, `style_id`) USING HASH;

INSERT INTO storehouse_style_cost_price_history (storehouse_id, style_id) VALUES ('','');

INSERT INTO storehouse_style_cost_price_history (storehouse_id, style_id) VALUES ('','');

INSERT INTO storehouse_style_cost_price_history (storehouse_id, style_id) VALUES ('','');