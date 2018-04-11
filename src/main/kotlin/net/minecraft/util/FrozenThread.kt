package net.minecraft.util

import net.minecraft.Minecraft
import net.minecraft.client.MinecraftClient
import net.minecraft.server.MinecraftServer

class FrozenThread : Thread {

    @JvmField
    val minecraft: Minecraft

    constructor(client: MinecraftClient, name: String) : super(name) {
        this.minecraft = (client as Object) as Minecraft
        this.isDaemon = true
        this.start()
    }

    constructor(server: MinecraftServer) : super("MinecraftServer") {
        this.minecraft = (server as Object) as Minecraft
        this.isDaemon = true
        this.start()
    }

    override fun run() {
        while (this.minecraft.isRunning()) {
            try {
                Thread.sleep(2147483647L)
            } catch (e: InterruptedException) {
            }
        }
    }

}
