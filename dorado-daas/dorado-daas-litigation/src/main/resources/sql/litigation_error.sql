-- Table: public.litigation_error_crawled_data

-- DROP TABLE public.litigation_error_crawled_data;

CREATE TABLE public.litigation_error_crawled_data
(
  id character varying(65) NOT NULL, -- 主键
  source_url_id character varying(65), -- 来源网址id
  content_url text, -- 抓取数据内容页url
  data text, -- 抓取的诉讼数据
  title text, -- 标题
  publish_date timestamp with time zone, -- 发布日期
  created_date timestamp with time zone, -- 创建时间
  insert_date timestamp with time zone, -- 数据入库时间
  CONSTRAINT litigation_error_crawled_data_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.litigation_error_crawled_data
  OWNER TO postgres;
COMMENT ON TABLE public.litigation_error_crawled_data
  IS '诉讼抓取数据表';
COMMENT ON COLUMN public.litigation_error_crawled_data.id IS '主键';
COMMENT ON COLUMN public.litigation_error_crawled_data.source_url_id IS '来源网址id';
COMMENT ON COLUMN public.litigation_error_crawled_data.content_url IS '抓取数据内容页url';
COMMENT ON COLUMN public.litigation_error_crawled_data.data IS '抓取的诉讼数据';
COMMENT ON COLUMN public.litigation_error_crawled_data.title IS '标题';
COMMENT ON COLUMN public.litigation_error_crawled_data.publish_date IS '发布日期';
COMMENT ON COLUMN public.litigation_error_crawled_data.created_date IS '创建时间';
COMMENT ON COLUMN public.litigation_error_crawled_data.insert_date IS '数据入库时间';

-----------------------------------------------------------
-- Table: public.litigation_error_parsed_data_detail

-- DROP TABLE public.litigation_error_parsed_data_detail;

CREATE TABLE public.litigation_error_parsed_data_detail
(
  id character varying(65) NOT NULL, -- 主键
  supplier_id character varying(65) NOT NULL, -- 供应商主键
  parsed_data_id character varying(65), -- 案件编号
  crawled_data_id character varying(65), -- 抓取数据表主键id
  num_non_empty integer, -- optix用的，暂留
  case_number character varying(255), -- 案号
  simple_case_number character varying(255), -- 简单案号
  previous_case_number character varying(255), -- 上期案号
  simple_previous_case_number character varying(255), -- 简单上期案号
  if_be_court character varying(1), -- 是否勾选（作报告用
  case_category_id integer, -- 案件类型
  accuse_date timestamp with time zone, -- 案件审理日期
  court_id integer, -- 受理法院 / 执行法院
  case_title text, -- 案件标题
  accuse_status text, -- 案件状态
  accuser_list_no_anonymous text,
  appuser_list_no_anonymous text,
  party_list_no_anonymous text,
  simple_detail text, -- 简单案由
  details text, -- 详细案由
  total_amount_involved numeric(20,2), -- 涉案金额
  trial_procedure text,
  win character varying(255), -- 胜/败诉
  deduplicated boolean,
  created_date timestamp with time zone, -- 创建日期
  status integer, -- （“0”表示该条数据解析成功；“1”，表示该条数据有用但未解析成功；“3”表示无用数据）
  reason text,
  updated_date timestamp with time zone, -- 更新时间
  expiry_date timestamp with time zone, -- 判断是否有效用，若无效，则又有值，重复的和被替代的 数据 则该字段有值
  insert_date timestamp with time zone, -- 数据入库时间
  pk_id bigint, -- 与诉讼平台数据关联用
  data_from integer, -- 数据来源,0-optix,1-db2主表,2-db2进程表,3-sqlserver诉讼平台
  fetch_flag integer, -- 数据提取标示，0-未被提取过，1-已被提取
  error_type integer, -- 数据异常标示，0-判重被物理删除，1-数据不合格发生异常
  CONSTRAINT litigation_error_parsed_data_detail_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.litigation_error_parsed_data_detail
  OWNER TO postgres;
COMMENT ON TABLE public.litigation_error_parsed_data_detail
  IS '诉讼案件详情';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.id IS '主键';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.supplier_id IS '供应商主键';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.parsed_data_id IS '案件编号';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.crawled_data_id IS '抓取数据表主键id';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.num_non_empty IS 'optix用的，暂留';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.case_number IS '案号';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.previous_case_number IS '上期案号';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.if_be_court IS '是否勾选（作报告用';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.case_category_id IS '案件类型';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.accuse_date IS '案件审理日期 ';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.court_id IS '受理法院 / 执行法院';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.case_title IS '案件标题';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.accuse_status IS '案件状态';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.simple_detail IS '简单案由';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.details IS '详细案由';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.total_amount_involved IS '涉案金额';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.win IS '胜/败诉';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.created_date IS '创建日期';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.status IS '（“0”表示该条数据解析成功；“1”，表示该条数据有用但未解析成功；“3”表示无用数据）';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.updated_date IS '更新时间';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.expiry_date IS '判断是否有效用，若无效，则又有值，重复的和被替代的 数据 则该字段有值';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.insert_date IS '数据入库时间';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.pk_id IS '与诉讼平台数据关联用';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.data_from IS '数据来源,0-optix,1-db2主表,2-db2进程表,3-sqlserver诉讼平台';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.fetch_flag IS '数据提取标示，0-未被提取过，1-已被提取';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.error_type IS '数据异常标示，0-判重被物理删除，1-数据不合格发生异常';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.simple_case_number IS '简单案号';
COMMENT ON COLUMN public.litigation_error_parsed_data_detail.simple_previous_case_number IS '简单上期案号';


-----------------------------------------------------------

-- Table: public.litigation_error_party

-- DROP TABLE public.litigation_error_party;

CREATE TABLE public.litigation_error_party
(
  id character varying(65) NOT NULL, -- 主键
  parsed_data_detail_id character varying(65), -- litigation_parsed_data_detail 诉讼案件详情表主键
  name character varying(255),
  sbdnum character varying(255),
  party_category_id integer, -- 若企业/自然人为原告，则置值“1”；若为被告，则置值“2”，若为当事人，则置值“3”
  legal_representative character varying(255), -- 法定代表人
  organization_code character varying(255), -- 组织机构代码
  identification_number character varying(255), -- 身份证号码
  date_of_birth date, -- 出生日期
  address character varying(1000), -- 地址
  organization_name character varying(255), -- 单位名称
  amount_involved numeric(20,2), -- 履行金额
  amount_to_be_executed numeric(20,2), -- 未执行金额
  updated_date timestamp with time zone, -- 更新时间
  accuse_date timestamp with time zone, -- 案件审理日期
  insert_date timestamp with time zone, -- 数据入库时间
  CONSTRAINT litigation_error_party_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public.litigation_error_party
  OWNER TO postgres;
COMMENT ON TABLE public.litigation_error_party
  IS '企业/自然人';
COMMENT ON COLUMN public.litigation_error_party.id IS '主键';
COMMENT ON COLUMN public.litigation_error_party.parsed_data_detail_id IS 'litigation_parsed_data_detail 诉讼案件详情表主键';
COMMENT ON COLUMN public.litigation_error_party.party_category_id IS '若企业/自然人为原告，则置值“1”；若为被告，则置值“2”，若为当事人，则置值“3”';
COMMENT ON COLUMN public.litigation_error_party.legal_representative IS '法定代表人';
COMMENT ON COLUMN public.litigation_error_party.organization_code IS '组织机构代码';
COMMENT ON COLUMN public.litigation_error_party.identification_number IS '身份证号码';
COMMENT ON COLUMN public.litigation_error_party.date_of_birth IS '出生日期';
COMMENT ON COLUMN public.litigation_error_party.address IS '地址';
COMMENT ON COLUMN public.litigation_error_party.organization_name IS '单位名称';
COMMENT ON COLUMN public.litigation_error_party.amount_involved IS '履行金额';
COMMENT ON COLUMN public.litigation_error_party.amount_to_be_executed IS '未执行金额';
COMMENT ON COLUMN public.litigation_error_party.updated_date IS '更新时间';
COMMENT ON COLUMN public.litigation_error_party.accuse_date IS '案件审理日期';
COMMENT ON COLUMN public.litigation_error_party.insert_date IS '数据入库时间';


-----------------------------------------------------------
-----------------------------------------------------------