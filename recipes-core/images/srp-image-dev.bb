DESCRIPTION = "A development image with more full-featured Linux system \
functionality installed, including LTP test suite."

GLIBC_GENERATE_LOCALES = "en_GB.UTF-8 en_US.UTF-8"
IMAGE_LINGUAS ?= "en-gb"

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
libgcc \
libubsan \
libxml2 \
libxml2-dev \
pciutils \
${@oe.utils.conditional('ALTERNATIVE_KERNELS', '', '', ' \
			${ALTERNATIVE_KERNELS_INSTALL} \
			${ALTERNATIVE_KERNELS_MODULES_INSTALL}', d)} \
${@bb.utils.contains('DISTRO_FEATURES', 'simics', \
		     'simicsfs-client simics-agent fuse', '', d)} \
${@bb.utils.contains('DISTRO_FEATURES', 'multilib', \
		     '${MULTILIB_PACKAGES}', '', d)}  \
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
