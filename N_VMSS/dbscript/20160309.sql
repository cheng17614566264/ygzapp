-- Add comments to the columns 
comment on column VMS_TRANS_INFO.amt_cny
  is '���_�����(��˰�ϼ�)';
comment on column VMS_TRANS_INFO.tax_amt_cny
  is '˰��_�����';

-- Add/modify columns 
alter table VMS_TRANS_INFO modify short_and_over NUMBER(1,8);
