
--
-- Structure for table workflow_task_file_transfer_config
--

DROP TABLE IF EXISTS workflow_task_file_transfer_config;
CREATE TABLE workflow_task_file_transfer_config (
  id_task int NOT NULL,
  resource_type varchar(50) NOT NULL,
  resource_code varchar(50) NOT NULL,
  entry_code varchar(50) NOT NULL,
  target_fileserviceprovider_name varchar(50) NOT NULL,
  context varchar(50) NOT NULL,
  PRIMARY KEY (id_task)
);


