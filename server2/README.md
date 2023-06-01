# Hacker IO

## Deploy
ssh root@134.122.93.102
rm -r hackerio
exit
scp -r ~/proj/hackerio root@134.122.93.102:/root
ssh root@134.122.93.102
apt install sbcl
curl -O https://beta.quicklisp.org/quicklisp.lisp
sbcl --load quicklisp.lisp

Create .sbclrc:
(let ((quicklisp-init (merge-pathnames "quicklisp/setup.lisp"
                                       (user-homedir-pathname))))
  (when (probe-file quicklisp-init)
    (load quicklisp-init)))

(push #p"/root/hackerio/server2/" asdf:*central-registry*)

Edit: /root/hackerio/server2/src/config.lisp
screen -S hackerio
sbcl
(ql:quickload :hackerio)
(hackerio:start-server)
(hackerio:stop-server)

CTRL+A D

screen -r hackerio

http://134.122.93.102/mission1
