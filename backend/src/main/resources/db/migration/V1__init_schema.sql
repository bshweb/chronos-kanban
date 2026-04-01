create table boards (
    id uuid primary key,
    title varchar(100) not null,
    description varchar(5000)
);

create table stages (
    id uuid primary key,
    title varchar(100) not null,
    position bigint not null check (position > 0),
    board_id uuid not null references boards(id) on delete cascade,
    constraint uk_stage_board_position unique (board_id, position)
);

create table tasks (
    id uuid primary key,
    title varchar(100) not null,
    description varchar(5000),
    position bigint not null check (position > 0),
    stage_id uuid not null references stages(id) on delete cascade,
    constraint uk_task_stage_position unique (stage_id, position)
);

create index idx_stages_board_id on stages(board_id);
create index idx_tasks_stage_id on tasks(stage_id);