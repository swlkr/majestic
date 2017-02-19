# {{name}}

## Start

```bash
git clone git@github.com:your-user-name/{{name}}.git
cd {{name}}
psql -c "create database {{sanitized}}_development"
lein migrate
```

## Dev

```bash
lein repl
```

```clojure
; in the repl
(start)
```

```bash
http :1337 # hello, world!
```

## Prod (heroku)

```bash
heroku create {{name}}
git push heroku master
heroku addons:create heroku-postgresql
heroku config:add SECRET=$(openssl rand -base64 32)
heroku run lein migrate
# good to go!
```
