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
libb64 \
libxml2 \
python3-core \
python3-pexpect \
python3-pip \
python3-pytest \
screen \
tmux \
${@bb.utils.contains('DISTRO_FEATURES', 'simics', \
		     'simicsfs-client simics-agent fuse', '', d)} \
"

inherit core-image
