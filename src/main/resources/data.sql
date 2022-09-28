-- noinspection SqlNoDataSourceInspectionForFile
-- noinspection SqlResolveForFile

insert into event (`location_id`, `event_name`, `event_status`, `event_start_date_time`, `event_end_date_time`, `current_number_of_people`, `capacity`)
values
    (1, '운동1', 'OPENED', '2021-01-01 09:00:00', '2021-01-01 12:00:00', 0, 20),
    (1, '운동2', 'OPENED', '2021-01-01 13:00:00', '2021-01-01 12:00:00', 0, 20),
    (2, '행사1', 'OPENED', '2021-01-02 09:00:00', '2021-01-02 12:00:00', 0, 30),
    (2, '행사2', 'OPENED', '2021-01-03 09:00:00', '2021-01-03 12:00:00', 0, 30),
    (2, '행사3', 'CLOSED', '2021-01-04 09:00:00', '2021-01-04 12:00:00', 0, 30),
    (3, '오전 스키', 'OPENED', '2021-02-01 08:00:00', '2021-02-01 12:30:00', 12, 50)
;