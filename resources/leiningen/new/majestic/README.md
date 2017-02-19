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
(in-ns '{{name}}.core)
(start)
```

```bash
http :1337 # hello, world!
```

## Prod

```bash
lein uberjar
java -jar billing-service.jar
```
