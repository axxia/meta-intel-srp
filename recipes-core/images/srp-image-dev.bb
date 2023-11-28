DESCRIPTION = "A development image with more full-featured Linux system \
functionality installed, including LTP test suite."

GLIBC_GENERATE_LOCALES = "en_US.UTF-8"
IMAGE_LINGUAS = "en-us"

APPEND = "console=ttyS0 loglevel=8"

AUTO_SYSLINUXMENU = "0"

IMAGE_FEATURES:append = " \
dev-pkgs \
splash \
ssh-server-openssh \
"

IMAGE_INSTALL = " \
${CORE_IMAGE_BASE_INSTALL} \
packagegroup-core-full-cmdline \
packagegroup-core-buildessential \
packagegroup-core-tools-debug \
elfutils-dev \
gdbserver \
inetutils \
initscripts-readonly-rootfs-overlay \
kexec \
kexec-tools \
ltp \
srp-rc-local \
kernel-dev \
kernel-devsrc \
libasan \
libb64 \
libb64-dev \
libgcc \
libubsan \
libxml2 \
libxml2-dev \
pciutils \
python3-core \
python3-pexpect \
python3-pip \
python3-pytest \
${@bb.utils.contains('DISTRO_FEATURES', 'simics', \
		     'simicsfs-client simics-agent fuse', '', d)} \
"

MULTILIB_PACKAGES ?= " \
lib32-libasan \
lib32-libgcc \
lib32-libubsan \
lib32-libxml2 \
"

TOOLCHAIN_TARGET_TASK:append = " kernel-devsrc"

TOOLCHAIN_HOST_TASK:append = " nativesdk-elfutils-dev"

inherit core-image
