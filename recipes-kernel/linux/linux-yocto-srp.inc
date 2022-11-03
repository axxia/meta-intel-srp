KBRANCH:srpeh  = "v5.15/standard/base"
KMACHINE:srpeh = "common-pc-64"
SRCREV_machine:srpeh ?= "efe20512212b0e85b5f884b1bfc8fbba2b43541a"
COMPATIBLE_MACHINE:srpeh = "srpeh"
LINUX_VERSION:srpeh = "5.15.59"

FILESEXTRAPATHS:prepend := "${THISDIR}:${THISDIR}/patches:${THISDIR}/frags:"

SRC_URI:append = " \
    file://00001-driver-core-auxiliary-bus-Add-Config-Option.patch \
    file://srp.scc \
    "

LINUX_VERSION_EXTENSION ?= "-yocto-srp-${LINUX_KERNEL_TYPE}"

# If this kernel package is not set as virtual/kernel provider,
# allow to be used as alternative kernel.
KERNEL_PACKAGE_NAME = "${@oe.utils.conditional('PREFERRED_PROVIDER_virtual/kernel', \
		      '${PN}', 'kernel', 'altkernel-${PN}', d)}"

# Alternative kernels should not provide virtual/kernel to be parsed and compiled
PROVIDES = "${@ "virtual/kernel" if (d.getVar("KERNEL_PACKAGE_NAME") == "kernel") else "" }"

# Ensure all modules are built-in
do_kernel_configcheck[postfuncs] += "builtin_modules"
builtin_modules() {
	sed -i "s/=m/=y/g" ${B}/.config
}

do_configure:append() {
	sed -i s@UTS_VERSION=\"\#\$VERSION\"@UTS_VERSION=\"${RELEASE_VERSION}\ \#\$VERSION\"@g \
	       ${STAGING_KERNEL_DIR}/scripts/mkcompile_h
}

do_install:append() {
	depmod -a -b ${D} -F ${D}/boot/System.map-${KERNEL_VERSION} ${KERNEL_VERSION}
}

do_shared_workdir:append() {
	if [ "${KERNEL_PACKAGE_NAME}" != "kernel" ]; then
		# Expose kernel tree in work-shared if not preferred provider for virtual/kernel
		rm -f ${TMPDIR}/work-shared/${MACHINE}/kernel-*-${PN}
		ln -sf ${STAGING_KERNEL_DIR} ${TMPDIR}/work-shared/${MACHINE}/kernel-source-${PN}
		ln -sf ${STAGING_KERNEL_BUILDDIR} ${TMPDIR}/work-shared/${MACHINE}/kernel-build-artifacts-${PN}
	fi
}

kernel_do_deploy:append() {
	install -d ${DEPLOYDIR}
	install -m 0644 ${KBUILD_OUTPUT}/System.map \
		${DEPLOYDIR}/System-${PN}${KERNEL_ARTIFACT_NAME}.map
	if [ "${KERNEL_PACKAGE_NAME}" = "kernel" ]; then
		cd ${DEPLOYDIR}
		rm -f System.map
		ln -sf System-${PN}${KERNEL_ARTIFACT_NAME}.map System.map
	fi
}

FILES:${KERNEL_PACKAGE_NAME}-modules += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/modules.*"

# Clean alternative kernels if virtual/kernel's clean or cleansstate is run
ALTERNATIVE_KERNELS_CLEAN = "${@':do_clean '.join(d.getVar('ALTERNATIVE_KERNELS')[1:].split(' '))}:do_clean"
ALTERNATIVE_KERNELS_CLEANSSTATE = "${@':do_cleansstate '.join(d.getVar('ALTERNATIVE_KERNELS')[1:].split(' '))}:do_cleansstate"
ALTERNATIVE_KERNELS_CLEAN_CHECK = "${@oe.utils.conditional('ALTERNATIVE_KERNELS', '', '', \
				   '${ALTERNATIVE_KERNELS_CLEAN}', d)}"
ALTERNATIVE_KERNELS_CLEANSSTATE_CHECK = "${@oe.utils.conditional('ALTERNATIVE_KERNELS', '', '', \
					'${ALTERNATIVE_KERNELS_CLEANSSTATE}', d)}"
do_clean[depends] += "${@oe.utils.conditional('PREFERRED_PROVIDER_virtual/kernel', '${PN}', \
		     '${ALTERNATIVE_KERNELS_CLEAN_CHECK}', '', d)}"
do_cleansstate[depends] += "${@oe.utils.conditional('PREFERRED_PROVIDER_virtual/kernel', '${PN}', \
			   '${ALTERNATIVE_KERNELS_CLEANSSTATE_CHECK}', '', d)}"

# Compile tools needed by external modules each time kernel is clean
do_clean[depends] += "make-mod-scripts:do_cleansstate"

INSANE_SKIP:kernel-dev = "debug-files"
