insert into boards (id, title, description)
values (
           '11111111-1111-1111-1111-111111111111',
           'Demo Board',
           'Initial seeded kanban board'
       );

insert into stages (id, title, position, board_id)
values
    ('22222222-2222-2222-2222-222222222221', 'Backlog', 1, '11111111-1111-1111-1111-111111111111'),
    ('22222222-2222-2222-2222-222222222222', 'ToDo', 2, '11111111-1111-1111-1111-111111111111'),
    ('22222222-2222-2222-2222-222222222223', 'In Progress', 3, '11111111-1111-1111-1111-111111111111'),
    ('22222222-2222-2222-2222-222222222224', 'Needs Review', 4, '11111111-1111-1111-1111-111111111111'),
    ('22222222-2222-2222-2222-222222222225', 'Reviewed', 5, '11111111-1111-1111-1111-111111111111'),
    ('22222222-2222-2222-2222-222222222226', 'Done', 6, '11111111-1111-1111-1111-111111111111');

insert into tasks (id, title, description, position, stage_id)
values
    ('33333333-3333-3333-3333-333333333331', 'Collect product requirements', 'Discuss scope and expected features', 1, '22222222-2222-2222-2222-222222222221'),
    ('33333333-3333-3333-3333-333333333332', 'Research competitors', 'Review similar kanban tools', 2, '22222222-2222-2222-2222-222222222221'),

    ('33333333-3333-3333-3333-333333333333', 'Create board endpoint', 'Implement create board API', 1, '22222222-2222-2222-2222-222222222222'),
    ('33333333-3333-3333-3333-333333333334', 'Add validation', 'Validate incoming DTOs', 2, '22222222-2222-2222-2222-222222222222'),

    ('33333333-3333-3333-3333-333333333335', 'Implement stage reordering', 'Support drag and drop reorder logic', 1, '22222222-2222-2222-2222-222222222223'),
    ('33333333-3333-3333-3333-333333333336', 'Write integration tests', 'Cover board and stage endpoints', 2, '22222222-2222-2222-2222-222222222223'),

    ('33333333-3333-3333-3333-333333333337', 'Review service layer', 'Check transaction boundaries and exceptions', 1, '22222222-2222-2222-2222-222222222224'),

    ('33333333-3333-3333-3333-333333333338', 'Approve API contract', 'Confirm response DTO structure', 1, '22222222-2222-2222-2222-222222222225'),

    ('33333333-3333-3333-3333-333333333339', 'Deploy demo version', 'Initial deployment to free hosting', 1, '22222222-2222-2222-2222-222222222226');