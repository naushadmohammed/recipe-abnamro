create index idx_recipes_servings on recipes(servings);
create index idx_recipes_type on recipes(type);

create index idx_ingredients_name on ingredients(name);

create extension if not exists pg_trgm;
create index idx_instructions_description_gin on instructions using gin (lower(description) gin_trgm_ops);