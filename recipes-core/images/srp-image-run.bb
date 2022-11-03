DESCRIPTION = "A console-only image with full-featured Linux system \
functionality installed."

GLIBC_GENERATE_LOCALES = "en_GB.UTF-8 en_US.UTF-8"
IMAGE_LINGUAS ?= "en-gb"

IMAGE_FEATURES:append = " \
splash \
ssh-server-openssh \
"

IMAGE_INSTALL = "\
${CORE_IMAGE_BASE_INSTALL} \
packagegroup-core-full-cmdline \
initscripts-readonly-rootfs-overlay \
kexec \
kexec-tools \
srp-rc-local \
libxml2 \
"

inherit core-image
