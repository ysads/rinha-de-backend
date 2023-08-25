(ns rinha.models.person
  (:gen-class))

(defn valid-string? [val]
  (and (string? val)
       (<= 1 (count val) 32)))

(defn valid-date? [val]
  (and (string? val)
       (re-matches #"\d{4}\-\d{2}\-\d{2}" val)))

(defn valid-stack? [val]
  (or (nil? val)
      (and (coll? val)
           (every? valid-string? val))))

(defn valid? [payload]
  (and (valid-string? (:name payload))
       (valid-string? (:nickname payload))
       (valid-date? (:birthdate payload))
       (valid-stack? (:stack payload))))