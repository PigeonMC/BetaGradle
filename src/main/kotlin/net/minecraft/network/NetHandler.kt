package net.minecraft.network

import net.minecraft.network.packet.*
import net.minecraft.network.paticle.PreChunkPacket

abstract class NetHandler() {

    abstract fun isServerHandler(): Boolean

    open fun handleMapChunk(var1: MapChunkPacket) {}

    open fun registerPacket(var1: Packet) {}

    open fun handleErrorMessage(var1: String, var2: Array<Any>) {}

    open fun handleKickDisconnect(var1: KickDisconnectPacket) {
        this.registerPacket(var1)
    }

    open fun handleLogin(var1: LoginPacket) {
        this.registerPacket(var1)
    }

    open fun handleFlying(var1: FlyingPacket) {
        this.registerPacket(var1)
    }

    open fun handleMultiBlockChange(var1: MultiBlockChangePacket) {
        this.registerPacket(var1)
    }

    open fun handleBlockDig(var1: BlockDigPacket) {
        this.registerPacket(var1)
    }

    open fun handleBlockChange(var1: BlockChangePacket) {
        this.registerPacket(var1)
    }

    open fun handlePreChunk(var1: PreChunkPacket) {
        this.registerPacket(var1)
    }

    open fun handleNamedEntitySpawn(var1: NamedEntitySpawnPacket) {
        this.registerPacket(var1)
    }

    open fun handleEntity(var1: EntityPacket) {
        this.registerPacket(var1)
    }

    open fun handleEntityTeleport(var1: EntityTeleportPacket) {
        this.registerPacket(var1)
    }

    open fun handlePlace(var1: PlaceBlockPacket) {
        this.registerPacket(var1)
    }

    open fun handleBlockItemSwitch(var1: BlockItemSwitchPacket) {
        this.registerPacket(var1)
    }

    open fun handleDestroyEntity(var1: DestroyEntityPacket) {
        this.registerPacket(var1)
    }

    open fun handlePickupSpawn(var1: ItemSpawnPacket) {
        this.registerPacket(var1)
    }

    open fun handleCollect(var1: CollectPacket) {
        this.registerPacket(var1)
    }

    open fun handleChat(var1: ChatPacket) {
        this.registerPacket(var1)
    }

    open fun handleVehicleSpawn(var1: VehicleSpawnPacket) {
        this.registerPacket(var1)
    }

    open fun handleArmAnimation(var1: AnimationPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: EntityActionPacket) {
        this.registerPacket(var1)
    }

    open fun handleHandshake(var1: HandshakePacket) {
        this.registerPacket(var1)
    }

    open fun handleMobSpawn(var1: EntitySpawnPacket) {
        this.registerPacket(var1)
    }

    open fun handleUpdateTime(var1: UpdateTimePacket) {
        this.registerPacket(var1)
    }

    open fun handleSpawnPosition(var1: SpawnPositionPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: EntityVelocityPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: EntityMetadataPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: AttachEntityPacket) {
        this.registerPacket(var1)
    }

    open fun handleUseEntity(var1: UseEntityPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: EntityStatusPacket) {
        this.registerPacket(var1)
    }

    open fun handleHealth(var1: UpdateHealthPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: RespawnPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: ExplosionPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: OpenWindowPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: CloseWindowPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: WindowClickPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: SetSlotPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: WindowItemPacket) {
        this.registerPacket(var1)
    }

    open fun handleSignUpdate(var1: UpdateSignPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: UpdateProgressBarPacket) {
        this.registerPacket(var1)
    }

    open fun handlePlayerInventory(var1: PlayerInventoryPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: TransactionPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: PaintingEntityPacket) {
        this.registerPacket(var1)
    }

    open fun handleNotePlay(var1: PlayNoteBlockPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: StatisticPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: SleepPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: PositionPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: BedPacket) {
        this.registerPacket(var1)
    }

    open fun handleWeather(var1: WeatherPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: MapDataPacket) {
        this.registerPacket(var1)
    }

    open fun a(var1: DoorUpdatePacket) {
        this.registerPacket(var1)
    }

}
