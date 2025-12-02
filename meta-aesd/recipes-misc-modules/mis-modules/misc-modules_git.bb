## misc-modules_git.bb - Simplified Recipe for the misc-modules Kernel Module

# IMPORTANT: Replace the placeholders with your correct values!
# -------------------------------------------------------------------
LICENSE = ""
LIC_FILES_CHKSUM = ""
LICENSE = "CLOSED"

# Sources
# -------------------------------------------------------------------
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-MarkoHammersen.git;protocol=ssh;branch=main \		 
           file://0001-yocto-assignment-7.patch \
           file://misc-modules-init \
          "

PV = "1.0+git${SRCPV}"
SRCREV = "8cfea8e94f45e494907c31ab9e3f597fad7c229e"

S = "${WORKDIR}/git"

# Yocto Classes
# -------------------------------------------------------------------
inherit module
inherit update-rc.d

# Init Script Configuration
# -------------------------------------------------------------------
INITSCRIPT_NAME = "misc-modules-init"
# By default, ${PN} (misc-modules) is used, unless otherwise specified
INITSCRIPT_PACKAGES = "${PN}" 

# Compilation (Kernel Module specific)
# -------------------------------------------------------------------
EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"


do_compile() {
    # We use ${S} as the source directory and set M=${S} for the kernel module build
    oe_runmake -C ${S} KERNELDIR=${STAGING_KERNEL_DIR} M=${S}
}

# Installation
# -------------------------------------------------------------------
do_install() {
    # 1. Installation of the Kernel Module (.ko file)
    # Installs to /lib/modules/${KERNEL_VERSION}/extra/
    # ${nonarch_base_libdir} = /lib
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    install -m 0644 ${S}/misc-modules/*.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/

    # 2. Installation of the Init Script
    # Installs to /etc/init.d/
    # ${sysconfdir} = /etc
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/misc-modules-init ${D}${sysconfdir}/init.d/
}


# Package Files
# -------------------------------------------------------------------
# Explicitly adds the Init script to the package content
FILES:${PN} += "${sysconfdir}/init.d/misc-modules-init"
