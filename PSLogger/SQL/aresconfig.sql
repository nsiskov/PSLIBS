-- LOG Base location needs to be updated for logger -- 
set define off
Insert into ARESCONFIG (PARAMNAME,PARAMVALUE,PARAMDESC,PARAMTYPE) values ('DEFAULT_LOG_LEVEL','INFO','Default log level',1);
Insert into ARESCONFIG (PARAMNAME,PARAMVALUE,PARAMDESC,PARAMTYPE) values ('LOG_BASE_LOCATION','/opt/arcot/logs/callout','Default log file location',1);
Insert into ARESCONFIG (PARAMNAME,PARAMVALUE,PARAMDESC,PARAMTYPE) values ('DEFAULT_LOG_FILE','transfort_default_log','Default log file',1);
Insert into ARESCONFIG (PARAMNAME,PARAMVALUE,PARAMDESC,PARAMTYPE) values ('LOG_FORMAT','%d{MM-dd-yyyy HH:mm:ss z} %X{hostname} [%t] %p %c - %m%n','Log format',1);
Insert into ARESCONFIG (PARAMNAME,PARAMVALUE,PARAMDESC,PARAMTYPE) values ('LOG_EMAIL_FROM','','Log - From Email',1);
Insert into ARESCONFIG (PARAMNAME,PARAMVALUE,PARAMDESC,PARAMTYPE) values ('LOG_EMAIL_TO','','Log - to DL or email',1);
Insert into ARESCONFIG (PARAMNAME,PARAMVALUE,PARAMDESC,PARAMTYPE) values ('LOG_EMAIL_HOST','','Log - Email host',1);
Insert into ARESCONFIG (PARAMNAME,PARAMVALUE,PARAMDESC,PARAMTYPE) values ('LOG_MASK_PATTERN','','Regex pattern to filter mask data',1);
Insert into ARESCONFIG (PARAMNAME,PARAMVALUE,PARAMDESC,PARAMTYPE) values ('LOG_MASK_DATA','N','Flag indicating whether to mask sensitive data',1);
Insert into ARESCONFIG (PARAMNAME,PARAMVALUE,PARAMDESC,PARAMTYPE) values ('LOG_MASK_REPLACE_TEXT','','Masking text of the senstive data(e.g. credit card number be masked as XXXX-XXXX-XXXX-1234)',1);
commit;