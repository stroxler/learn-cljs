# Dev is our own task defined in boot.repl, which does:
#   wait serve watch reload cljs-repl cljs
dev:
	boot dev

# Start an nrepl client session.
#   some clarification here: the boot repl is a clj repl. Inside that clj
#   repl (which drops you in the `boot.user` namespace) there's a function
#   `start-repl` to start the clojurescript repl we want
# It seems like the server may be a bit picky about when you connect /
# disconnect, but running `make boot` in one terminal and waiting a while
# (if you don't wait the repl client often errs out on the first try, but if
# so just try again), then starting the repl, then hitting the webpage
# seemed to work.
repl:
	boot repl -c -e '(start-repl)'

clean:
	rm -rf build out
