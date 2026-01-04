create table if not exists recipes(
    id uuid primary key,
    name varchar not null,
    description text not null,
    servings int not null check ( servings >= 1 ),
    type varchar not null,
    created_at timestamp not null,
    updated_at timestamp not null,
    created_by uuid not null,
    constraint fk_recipes_created_by foreign key (created_by) references users(id)
);

create table if not exists ingredients(
    id uuid primary key,
    name varchar not null,
    amount double precision not null check ( amount > 0 ),
    unit varchar not null,
    recipe uuid not null,
    constraint fk_ingredients_recipe foreign key (recipe) references recipes(id) on delete cascade
);

create table if not exists instructions(
    id uuid primary key ,
    step integer not null check ( step >= 1 ),
    description text not null,
    recipe uuid not null,
    constraint fk_instructions_recipe foreign key (recipe) references recipes(id) on delete cascade
);

create index idx_ingredients_recipe on ingredients(recipe);
create index idx_instructions_recipe on instructions(recipe);
create index idx_recipes_created_by on recipes(created_by);
