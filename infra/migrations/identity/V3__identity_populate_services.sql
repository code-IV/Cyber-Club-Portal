SET search_path TO identity;

INSERT INTO services (id, service_name) VALUES 
    (gen_random_uuid(), 'portal'),
    (gen_random_uuid(), 'community'),
    (gen_random_uuid(), 'challenge'),
    (gen_random_uuid(), 'learn');