# majestic

## Usage

```bash
lein new majestic your-project-name
```

## What Just Happened?

Files were created! Files!

```bash
your-project-name
├── README.md
├── profiles.clj
├── project.clj
├── resources
│   ├── migrations
│   │   ├── 20170218150305_add_hstore_ext.edn
│   │   └── 20170218150305_create_users_table.edn
│   └── sql
│       └── users.sql
├── src
│   └── your_project_name
│       ├── core.clj
│       ├── db.clj
│       ├── env.clj
│       ├── http.clj
│       ├── logic
│       │   ├── tokens.clj
│       │   └── users.clj
│       ├── routes.clj
│       ├── server.clj
│       └── utils.clj
├── target
└── test
    └── your_project_name
        ├── db_test.clj
        ├── logic
        │   ├── tokens_test.clj
        │   └── users_test.clj
        ├── routes_test.clj
        └── server_test.clj
```

You can do a few different things, you can run all the tests with `lein test` or you can `lein repl` in and type `(start)` or you can deploy straight to heroku and run `heroku run lein migrate` to get the db up and running. It gives you what I think is the smallest set of working code to start a monolithic api.

## License

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
