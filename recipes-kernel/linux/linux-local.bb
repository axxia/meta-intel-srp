########################## linux-local.bb ############################
# Simple recipe to build kernel from local repository.               #
# Set linux-local as PREFERRED_PROVIDER for virtual/kernel component #
#     PREFERRED_PROVIDER_virtual/kernel = "linux-local"              #
# To build only the kernel run:                                      #
#      $ bitbake linux-local                                         #
# All changes should be committed in the local kernel clone.         #
# Full defconfig should be copied in this directory (besides recipe) #
# Adjust the following lines with the kernel path and branch.        #
######################################################################

LOCAL_KERNEL_PATH ?= "path-to-local-kernel-repository"
LOCAL_KERNEL_BRANCH ?= "standard/base"

FILESEXTRAPATHS:prepend := "${THISDIR}:"

require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION_EXTENSION = "-srp-local-dev"

LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c"

PV = "srp-dev"

DEPENDS:append = " elfutils-native openssl-native util-linux-native"

SRCREV_machine = "${AUTOREV}"

SRC_URI = " git://${LOCAL_KERNEL_PATH};name=machine;branch=${LOCAL_KERNEL_BRANCH} \
	    file://defconfig \
	    "

do_kernel_configme[depends] += "${PN}:do_prepare_recipe_sysroot"

COMPATIBLE_MACHINE:srpeh = "${MACHINE}"
KERNEL_VERSION_SANITY_SKIP = "1"
