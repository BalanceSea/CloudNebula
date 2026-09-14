package com.balancesea.cloudnebula

import taboolib.common.platform.Plugin
import taboolib.common.platform.function.info

object CloudNebula : Plugin() {

    override fun onEnable() {
        info("Successfully running CloudNebula!")
    }
}