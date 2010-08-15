(ns lazytest.runtests)

(doseq [sym '[lazytest.find-asserts
	      lazytest.runnable-asserts
	      lazytest.expect-asserts
	      lazytest.fixture-asserts
	      lazytest.fixture.stub-asserts
	      lazytest.describe-asserts
	      lazytest.readme-examples]]
  (println "Running assertions in" sym)
  (require sym))

(println "All assertions passed.")