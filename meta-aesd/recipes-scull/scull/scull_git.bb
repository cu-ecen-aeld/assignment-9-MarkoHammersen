## scull_git.bb - Simplified Recipe for the scull Kernel Module

# -------------------------------------------------------------------
LICENSE = ""
LIC_FILES_CHKSUM = ""
LICENSE = "CLOSED"

# Sources
# -------------------------------------------------------------------
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-MarkoHammersen.git;protocol=ssh;branch=main \		 
           file://0001-yocto-assignment-7.patch \
           file://scull-init \
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
INITSCRIPT_NAME = "scull-init"
# By default, ${PN} (scull) is used, unless otherwise specified
INITSCRIPT_PACKAGES = "${PN}" 

# Compilation (Kernel Module specific)
# -------------------------------------------------------------------
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
    install -m 0644 ${S}/scull/*.ko ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/

    # 2. Installation of the Init Script
    # Installs to /etc/init.d/
    # ${sysconfdir} = /etc
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/scull-init ${D}${sysconfdir}/init.d/
}

# Package Files
# -------------------------------------------------------------------
# Explicitly adds the Init script to the package content
FILES:${PN} += "${sysconfdir}/init.d/scull-init"
