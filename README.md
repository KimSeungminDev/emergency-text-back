CREATE TABLE emergency (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- id는 자동 증가
    crt_dt VARCHAR(50) NOT NULL,
    dst_se_nm VARCHAR(100),
    emrg_step_nm VARCHAR(100),
    mdfcn_ymd VARCHAR(50),
    msg_cn TEXT NOT NULL,
    msg_send VARCHAR(4000),
    rcptn_rgn_nm TEXT,
    reg_ymd VARCHAR(50),
    search_term VARCHAR(255),
    search_time VARCHAR(50),
    session_name VARCHAR(255), 
    sn BIGINT  -- sn은 중복 가능
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;


위는 sql이에요
