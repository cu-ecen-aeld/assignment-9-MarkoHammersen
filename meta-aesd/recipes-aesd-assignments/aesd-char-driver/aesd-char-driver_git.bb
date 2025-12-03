# Recipe for AESD Character Driver Kernel Module
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

# Git source
SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-MarkoHammersen.git;protocol=ssh;branch=main \
           file://aesdchar-init"

# Versionsinformationen
PV = "1.0+git${SRCPV}"
SRCREV = "bef1b50df54a6e118e8152ef31f2387d78522567"

S = "${WORKDIR}/git/aesd-char-driver"

inherit module

EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

inherit update-rc.d
INITSCRIPT_NAME:${PN} = "aesdchar-init"
INITSCRIPT_PACKAGES = "${PN}"
SRC_URI += "file://aesdchar-init"

do_install:append() {
    install -d ${D}${bindir}
	install -m 0755 ${S}/aesdchar_load ${D}${bindir}/
    install -m 0755 ${S}/aesdchar_unload ${D}${bindir}/

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/aesdchar-init ${D}${sysconfdir}/init.d/
}
FILES:${PN} += "${bindir}/aesdchar_load"
FILES:${PN} += "${bindir}/aesdchar_unload"
FILES:${PN} += "${sysconfdir}/init.d/aesdchar-init"
