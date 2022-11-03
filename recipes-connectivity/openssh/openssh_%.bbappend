# Disable sshd sysvinit service to have simulator booting
INITSCRIPT_PARAMS:${PN}-sshd = "stop 09 0 1 6 ."
