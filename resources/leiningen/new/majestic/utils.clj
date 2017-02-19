(ns {{name}}.utils
  (:require [cheshire.core :as json]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as string])
  (:import (org.postgresql.util PGobject)
           (java.util UUID)
           (java.util Date)))

(defn uuid [] (str (UUID/randomUUID)))

(defn now []
  (new Date))

(defn throw+
  ([err]
   (if (instance? Exception err)
     (throw err)
     (throw (ex-info err {}))))
  ([err data] (throw (ex-info err data))))

(defn flip [f]
  (fn [x y] (f y x)))

(defn str->int [s]
  (try
    (some-> s bigint int)
    (catch NumberFormatException e
      nil)))

(defn add-json-to-yesql []
  (extend-type PGobject
    jdbc/IResultSetReadColumn
    (result-set-read-column [val rsmeta idx]
      (let [colType (.getColumnTypeName rsmeta idx)]
        (if (or (= colType "json")
                (= colType "jsonb"))
          (json/parse-string (.getValue val) true) val)))))
