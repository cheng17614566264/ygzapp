-- ������ռ�
-- ������ռ� VMSS_IST
CREATE TABLESPACE VMSS_IST 
DATAFILE 'D:/Soft/Oracle/oradata/vms/VMSS_IST_0001.ORA' SIZE 256M 
AUTOEXTEND ON NEXT 64M MAXSIZE 1024M 
EXTENT MANAGEMENT LOCAL SEGMENT SPACE MANAGEMENT AUTO;

-- �����û�
-- ���� fmss_ist �û�
create user vmss_ist identified by VMSS_IST 
default tablespace VMSS_IST 
temporary tablespace TEMP;
-- Ϊ fmss_ist �û���Ȩ
GRANT 
	create session, create trigger, create procedure, 
	create sequence, create table, create view, 
	create public synonym, drop public synonym 
TO vmss_ist;
GRANT dba TO vmss_ist;
-- ���� fmss ��������ʹ�ñ�ռ�
ALTER USER vmss_ist QUOTA UNLIMITED ON VMSS_IST;