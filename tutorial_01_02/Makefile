# waiting for an interrupt before we exit
#  - start a boot server for the`build` directory (defaults to port 3000)
#  - watch the src directories (which are set in `build.boot`)
#    and immediately, then on every change:
#      - with browser reloading when we are finished compiling...
#      - compile clojurescript
#      - start a clojurescript nrepl server (the repl itself won't pop
#        up here, you need a separate terminal window) connected to all
#        listening browser windows
#      - the target directory for the compile is build
# The semantics are a little weird here, I still don't entirely understand
# them. For example, we don't start a new repl every time we recompile,
# do we?
boot:
	boot wait serve -d build watch reload cljs cljs-repl target -d build

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
