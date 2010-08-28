(ns lazytest.runner.console
  (:use lazytest.find
	lazytest.suite
	lazytest.test-case
	lazytest.color
	lazytest.focus
	lazytest.wrap
	[clojure.stacktrace :only (print-cause-trace)])
  (:import lazytest.ExpectationFailed))

(defn run-test-case [tc]
  (do-before tc)
  (let [result (try-test-case tc)]
    (if (:pass? result)
      (print (colorize "." :green))
      (print (colorize "F" :red)))
    (flush)
    (do-after tc)
    result))

(defn run-suite [ste]
  (let [ste-seq (ste)]
    (do-before ste-seq)
    (let [results (doall (map (fn [x]
				(cond (suite? x) (run-suite x)
				      (test-case? x) (run-test-case x)
				      :else (throw (IllegalArgumentException.
						    "Non-test given to run-suite."))))
			      ste-seq))]
      (do-after ste-seq)
      (suite-result ste-seq results))))

(defn run-tests
  "Runs tests defined in the given namespaces, with colored green dots
  indicating passing tests and red 'F's indicating falied tests."
  [& namespaces]
  (let [nns (if (seq namespaces) namespaces (all-ns))
	stes (remove nil? (map find-tests namespaces))]
    (doall (map run-suite stes))))
