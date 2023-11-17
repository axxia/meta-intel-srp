require recipes-kernel/linux/linux-yocto_${PV}.bb
require linux-yocto-srp.inc

FILESEXTRAPATHS:prepend := "${COREBASE}/meta/recipes-kernel/linux/files:"

LINUX_VERSION_EXTENSION = "-yocto-srp-debug-${LINUX_KERNEL_TYPE}"
SRC_URI:append = " file://debug.scc"
