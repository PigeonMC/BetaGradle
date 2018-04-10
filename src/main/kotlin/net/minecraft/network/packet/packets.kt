package net.minecraft.network.packet

import net.minecraft.entity.Entity
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.PaintingEntity
import net.minecraft.entity.PlayerEntity
import net.minecraft.inventory.ItemStack
import net.minecraft.network.NetHandler
import net.minecraft.util.DataWatcher
import net.minecraft.util.MathHelper
import net.minecraft.world.Art
import net.minecraft.world.World
import net.minecraft.world.chunk.ChunkPosition
import java.io.DataInputStream
import java.io.DataOutputStream

/*
 * These classes are used instead of the Vanilla ones because neither the Server nor the Client
 * has all of the constructors needed, and Mixin doesn't support injecting Constructors.
 */

open class AnimationPacket(
        @JvmField var entityId: Int,
        @JvmField var animation: Int
) : Packet() {

    constructor() : this(0, 0)
    constructor(entity: Entity, animation: Int) : this(entity.entityId, animation)

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
        this.animation = input.readByte().toInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
        output.writeByte(this.animation)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleArmAnimation(this)
    }

    override fun getPacketSize(): Int = 5

}

open class AttachEntityPacket(
        @JvmField var entityId: Int,
        @JvmField var vehicleEntityId: Int
) : Packet() {

    constructor() : this(0, 0)
    constructor(entity: Entity, vehicleEntity: Entity) : this(entity.entityId, vehicleEntity.entityId)

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
        this.vehicleEntityId = input.readInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
        output.writeInt(this.vehicleEntityId)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.a(this)
    }

    override fun getPacketSize(): Int = 8

}

// TODO: BedPacket => InvalidStatePacket

open class BlockChangePacket(
        @JvmField var x: Int,
        @JvmField var y: Int,
        @JvmField var z: Int,
        @JvmField var type: Int,
        @JvmField var metadata: Int
) : Packet() {

    constructor() : this(0, 0, 0, 0, 0)

    init {
        this.isChunkDataPacket = true
    }

    override fun readPacketData(input: DataInputStream) {
        this.x = input.readInt()
        this.y = input.read()
        this.z = input.readInt()
        this.type = input.read()
        this.metadata = input.read()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.x)
        output.write(this.y)
        output.writeInt(this.z)
        output.write(this.type)
        output.write(this.metadata)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleBlockChange(this)
    }

    override fun getPacketSize(): Int = 11

}

open class BlockDigPacket(
        @JvmField var x: Int,
        @JvmField var y: Int,
        @JvmField var z: Int,
        @JvmField var face: Int,
        @JvmField var status: Int
) : Packet() {

    constructor() : this(0, 0, 0, 0, 0)

    override fun readPacketData(input: DataInputStream) {
        this.status = input.read()
        this.x = input.readInt()
        this.y = input.read()
        this.z = input.readInt()
        this.face = input.read()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.write(this.status)
        output.writeInt(this.x)
        output.write(this.y)
        output.writeInt(this.z)
        output.write(this.face)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleBlockDig(this)
    }

    override fun getPacketSize(): Int = 11

}

open class BlockItemSwitchPacket(
        @JvmField var id: Int
) : Packet() {

    constructor() : this(0)

    override fun readPacketData(input: DataInputStream) {
        this.id = input.readShort().toInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeShort(this.id)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleBlockItemSwitch(this)
    }

    override fun getPacketSize(): Int = 2

}

open class ChatPacket(
        @JvmField var message: String
) : Packet() {

    constructor() : this("")

    init {
        if (message.length > 119) message = message.substring(0, 119)
    }

    override fun readPacketData(input: DataInputStream) {
        this.message = Packet.readString(input, 119)
    }

    override fun writePacketData(output: DataOutputStream) {
        Packet.writeString(this.message, output)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleChat(this)
    }

    override fun getPacketSize(): Int = this.message.length

}

open class CloseWindowPacket(
        @JvmField var windowId: Int
) : Packet() {
    constructor() : this(0)

    override fun readPacketData(input: DataInputStream) {
        this.windowId = input.readByte().toInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeByte(this.windowId)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.a(this)
    }

    override fun getPacketSize(): Int = 1
}

open class CollectPacket(
        @JvmField var collectedEntityId: Int,
        @JvmField var collectorEntityId: Int
) : Packet() {

    constructor() : this(0, 0)

    override fun readPacketData(input: DataInputStream) {
        this.collectedEntityId = input.readInt()
        this.collectorEntityId = input.readInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.collectedEntityId)
        output.writeInt(this.collectorEntityId)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleCollect(this)
    }

    override fun getPacketSize(): Int = 8

}

open class DestroyEntityPacket(
        @JvmField var entityId: Int
) : Packet() {

    constructor() : this(0)

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleDestroyEntity(this)
    }

    override fun getPacketSize(): Int = 4

}

// TODO: DoorUpdatePacket => SoundEffectPacket

open class EntityActionPacket(
        @JvmField var entityId: Int,
        @JvmField var state: Int
) : Packet() {

    constructor() : this(0, 0)

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
        this.state = input.readByte().toInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
        output.writeByte(this.state)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.a(this)
    }

    override fun getPacketSize(): Int = 5

}

open class EntityMetadataPacket(
        @JvmField var entityId: Int,
        @JvmField var metadata: List<*>
) : Packet() {

    constructor() : this(0, listOf<Any>())

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
        this.metadata = DataWatcher.readWatchableObjects(input)
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
        DataWatcher.writeObjectsInListToStream(this.metadata, output)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.a(this)
    }

    override fun getPacketSize(): Int = 5

    fun b(): List<*> = metadata

}

open class EntityPacket(
        @JvmField var entityId: Int,
        @JvmField var x: Byte,
        @JvmField var y: Byte,
        @JvmField var z: Byte,
        @JvmField var yaw: Byte,
        @JvmField var pitch: Byte,
        @JvmField var rotating: Boolean = false
) : Packet() {

    constructor() : this(0, 0, 0, 0, 0, 0, false)

    constructor(entityId: Int) : this(entityId, 0, 0, 0, 0, 0)

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleEntity(this)
    }

    override fun getPacketSize(): Int = 4

}

open class EntityRotationPacket(
        entityId: Int,
        yaw: Byte,
        pitch: Byte
) : EntityPacket(entityId, 0, 0, 0, yaw, pitch, true) {

    override fun readPacketData(input: DataInputStream) {
        super.readPacketData(input)
        this.yaw = input.readByte()
        this.pitch = input.readByte()
    }

    override fun writePacketData(output: DataOutputStream) {
        super.writePacketData(output)
        output.writeByte(this.yaw.toInt())
        output.writeByte(this.pitch.toInt())
    }

    override fun getPacketSize(): Int = 6

}

open class EntitySpawnPacket(
        @JvmField var entityId: Int,
        @JvmField var type: Byte,
        @JvmField var x: Int,
        @JvmField var y: Int,
        @JvmField var z: Int,
        @JvmField var yaw: Byte,
        @JvmField var pitch: Byte,
        @JvmField var metaData: DataWatcher?,
        @JvmField var receivedMetadata: List<*>
) : Packet() {

    constructor() : this(0, 0, 0, 0, 0, 0, 0, null, listOf<Any>())

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
        this.type = input.readByte()
        this.x = input.readInt()
        this.y = input.readInt()
        this.z = input.readInt()
        this.yaw = input.readByte()
        this.pitch = input.readByte()
        this.receivedMetadata = DataWatcher.readWatchableObjects(input)
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
        output.writeByte(this.type.toInt())
        output.writeInt(this.x)
        output.writeInt(this.y)
        output.writeInt(this.z)
        output.writeByte(this.yaw.toInt())
        output.writeByte(this.pitch.toInt())
        this.metaData!!.writeWatchableObjects(output)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleMobSpawn(this)
    }

    override fun getPacketSize(): Int = 20

    fun getMetadata(): List<*> = this.receivedMetadata

}

open class EntityStatusPacket(
        @JvmField var entityId: Int,
        @JvmField var entityStatus: Byte
) : Packet() {

    constructor() : this(0, 0)

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
        this.entityStatus = input.readByte()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
        output.writeByte(this.entityStatus.toInt())
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.a(this)
    }

    override fun getPacketSize(): Int = 5

}

open class EntityTeleportPacket(
        @JvmField var entityId: Int,
        @JvmField var x: Int,
        @JvmField var y: Int,
        @JvmField var z: Int,
        @JvmField var yaw: Byte,
        @JvmField var pitch: Byte
) : Packet() {

    constructor() : this(0, 0, 0, 0, 0, 0)

    constructor(entity: Entity) : this(
            entity.entityId,
            MathHelper.floor(entity.x * 32.toDouble()),
            MathHelper.floor(entity.y * 32.toDouble()),
            MathHelper.floor(entity.z * 32.toDouble()),
            (entity.rotationYaw * 256.0f / 360.0f).toInt().toByte(),
            (entity.rotationPitch * 256.0f / 360.0f).toInt().toByte()
    )

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
        this.x = input.readInt()
        this.y = input.readInt()
        this.z = input.readInt()
        this.yaw = input.read().toByte()
        this.pitch = input.read().toByte()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
        output.writeInt(this.x)
        output.writeInt(this.y)
        output.writeInt(this.z)
        output.write(this.yaw.toInt())
        output.write(this.pitch.toInt())
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleEntityTeleport(this)
    }

    override fun getPacketSize(): Int = 34

}

open class EntityVelocityPacket() : Packet() {

    @JvmField
    var entityId: Int = 0
    @JvmField
    var motionX: Int = 0
    @JvmField
    var motionY: Int = 0
    @JvmField
    var motionZ: Int = 0

    inline fun inBounds(min: Double, middle: Double, max: Double): Double {
        var r = middle
        if (r < min) r = min
        if (r > max) r = max
        return r
    }

    inline fun wrap(motion: Double): Int = (inBounds(-3.9, motion, 3.9) * 8000.0).toInt()


    constructor(entityId: Int, motionX: Double, motionY: Double, motionZ: Double) : this() {
        this.entityId = entityId
        this.motionX = wrap(motionX)
        this.motionX = wrap(motionY)
        this.motionX = wrap(motionZ)
    }

    constructor(entity: Entity) : this(entity.entityId, entity.motionX, entity.motionY, entity.motionZ)

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
        this.motionX = input.readShort().toInt()
        this.motionY = input.readShort().toInt()
        this.motionZ = input.readShort().toInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
        output.writeShort(this.motionX)
        output.writeShort(this.motionY)
        output.writeShort(this.motionZ)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.a(this)
    }

    override fun getPacketSize(): Int = 10

}

open class ExplosionPacket(
        @JvmField var x: Double,
        @JvmField var y: Double,
        @JvmField var z: Double,
        @JvmField var size: Float,
        @JvmField var destroyedBlockPositions: MutableSet<ChunkPosition>
) : Packet() {

    constructor() : this(0.0, 0.0, 0.0, 0.toFloat(), mutableSetOf())

    override fun readPacketData(input: DataInputStream) {
        this.x = input.readDouble()
        this.y = input.readDouble()
        this.z = input.readDouble()
        this.size = input.readFloat()
        val var2 = input.readInt()
        this.destroyedBlockPositions = mutableSetOf()
        val var3 = this.x.toInt()
        val var4 = this.y.toInt()
        val var5 = this.z.toInt()

        for (var6 in 0 until var2) {
            val var7 = input.readByte() + var3
            val var8 = input.readByte() + var4
            val var9 = input.readByte() + var5
            this.destroyedBlockPositions.add(ChunkPosition(var7, var8, var9))
        }

    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeDouble(this.x)
        output.writeDouble(this.y)
        output.writeDouble(this.z)
        output.writeFloat(this.size)
        output.writeInt(this.destroyedBlockPositions.size)
        val x = this.x.toInt()
        val y = this.y.toInt()
        val z = this.z.toInt()
        val destroyedBlockPositions = this.destroyedBlockPositions.iterator()

        while (destroyedBlockPositions.hasNext()) {
            val chunkPosition = destroyedBlockPositions.next()
            val dx = chunkPosition.x - x
            val dy = chunkPosition.y - y
            val dz = chunkPosition.z - z
            output.writeByte(dx)
            output.writeByte(dy)
            output.writeByte(dz)
        }

    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.a(this)
    }

    override fun getPacketSize(): Int = 32 + this.destroyedBlockPositions.size * 3

}

open class FlyingPacket(
        @JvmField var x: Double,
        @JvmField var y: Double,
        @JvmField var z: Double,
        @JvmField var stance: Double,
        @JvmField var yaw: Float,
        @JvmField var pitch: Float,
        @JvmField var onGround: Boolean,
        @JvmField var moving: Boolean,
        @JvmField var rotating: Boolean
) : Packet() {

    constructor() : this(0.0, 0.0, 0.0, 0.0, 0.toFloat(), 0.toFloat(), false, false, false)

    constructor(onGround: Boolean) : this() {
        this.onGround = onGround
    }

    override fun readPacketData(input: DataInputStream) {
        this.onGround = input.read() != 0
    }

    override fun writePacketData(output: DataOutputStream) {
        output.write(if (this.onGround) 1 else 0)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleFlying(this)
    }

    override fun getPacketSize(): Int = 1

}

open class HandshakePacket(
        @JvmField var username: String
) : Packet() {

    constructor() : this("")

    override fun readPacketData(input: DataInputStream) {
        this.username = Packet.readString(input, 32)
    }

    override fun writePacketData(output: DataOutputStream) {
        Packet.writeString(this.username, output)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleHandshake(this)
    }

    override fun getPacketSize(): Int = 4 + this.username.length + 4

}

open class ItemSpawnPacket(
        @JvmField var entityId: Int,
        @JvmField var x: Int,
        @JvmField var y: Int,
        @JvmField var z: Int,
        @JvmField var rotation: Byte,
        @JvmField var pitch: Byte,
        @JvmField var roll: Byte,
        @JvmField var itemId: Int,
        @JvmField var count: Int,
        @JvmField var itemDamage: Int
) : Packet() {

    constructor() : this(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    constructor(entity: ItemEntity) : this(
            entity.entityId,
            MathHelper.floor(entity.x * 32.0),
            MathHelper.floor(entity.y * 32.0),
            MathHelper.floor(entity.z * 32.0),
            (entity.motionX * 128.0).toInt().toByte(),
            (entity.motionY * 128.0).toInt().toByte(),
            (entity.motionZ * 128.0).toInt().toByte(),
            entity.item.itemID,
            entity.item.stackSize,
            entity.item.itemDamage
    )

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
        this.itemId = input.readShort().toInt()
        this.count = input.readByte().toInt()
        this.itemDamage = input.readShort().toInt()
        this.x = input.readInt()
        this.y = input.readInt()
        this.z = input.readInt()
        this.rotation = input.readByte()
        this.pitch = input.readByte()
        this.roll = input.readByte()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
        output.writeShort(this.itemId)
        output.writeByte(this.count)
        output.writeShort(this.itemDamage)
        output.writeInt(this.x)
        output.writeInt(this.y)
        output.writeInt(this.z)
        output.writeByte(this.rotation.toInt())
        output.writeByte(this.pitch.toInt())
        output.writeByte(this.roll.toInt())
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handlePickupSpawn(this)
    }

    override fun getPacketSize(): Int = 24

}

open class KeepAlivePacket() : Packet() {

    override fun readPacketData(input: DataInputStream) {}

    override fun writePacketData(output: DataOutputStream) {}

    override fun processPacket(netHandler: NetHandler) {}

    override fun getPacketSize(): Int = 0

}

open class LoginPacket(
        @JvmField var protocolVersion: Int,
        @JvmField var username: String,
        @JvmField var mapSeed: Long,
        @JvmField var dimension: Byte
) : Packet() {

    constructor() : this(0, "", 0, 0)

    constructor(username: String, protocolVersion: Int) : this(protocolVersion, username, 0, 0)

    override fun readPacketData(input: DataInputStream) {
        this.protocolVersion = input.readInt()
        this.username = Packet.readString(input, 16)
        this.mapSeed = input.readLong()
        this.dimension = input.readByte()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.protocolVersion)
        Packet.writeString(this.username, output)
        output.writeLong(this.mapSeed)
        output.writeByte(this.dimension.toInt())
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleLogin(this)
    }

    override fun getPacketSize(): Int = 4 + this.username.length + 4 + 5

}

// TODO: World#c(FFFFFF)[B is missing
//open class MapChunkPacket() : Packet() {
//    @JvmField var x: Int = 0
//    @JvmField var y: Int = 0
//    @JvmField var z: Int = 0
//    @JvmField var xSize: Int = 0
//    @JvmField var ySize: Int = 0
//    @JvmField var zSize: Int = 0
//    @JvmField var chunk: ByteArray = byteArrayOf()
//    @JvmField var chunkSize: Int = 0
//
//    init {
//        this.isChunkDataPacket = true
//    }
//
//    constructor(var1: Int, var2: Int, var3: Int, var4: Int, var5: Int, var6: Int, var7: World): this() {
//        this.isChunkDataPacket = true
//        this.x = var1
//        this.y = var2
//        this.z = var3
//        this.xSize = var4
//        this.ySize = var5
//        this.zSize = var6
//        var7.setChunkData()
//        val var8 = var7.c(var1, var2, var3, var4, var5, var6)
//        val var9 = Deflater(-1)
//
//        try {
//            var9.setInput(var8)
//            var9.finish()
//            this.chunk = ByteArray(var4 * var5 * var6 * 5 / 2)
//            this.chunkSize = var9.deflate(this.chunk)
//        } finally {
//            var9.end()
//        }
//
//    }
//
//    override fun readPacketData(input: DataInputStream) {
//        this.x = input.readInt()
//        this.y = input.readShort().toInt()
//        this.z = input.readInt()
//        this.xSize = input.read() + 1
//        this.ySize = input.read() + 1
//        this.zSize = input.read() + 1
//        this.chunkSize = input.readInt()
//        val var2 = ByteArray(this.chunkSize)
//        input.readFully(var2)
//        this.chunk = ByteArray(this.xSize * this.ySize * this.zSize * 5 / 2)
//        val var3 = Inflater()
//        var3.setInput(var2)
//
//        try {
//            var3.inflate(this.chunk)
//        } catch (var8: DataFormatException) {
//            throw IOException("Bad compressed data format")
//        } finally {
//            var3.end()
//        }
//
//    }
//
//    override fun writePacketData(output: DataOutputStream) {
//        output.writeInt(this.x)
//        output.writeShort(this.y)
//        output.writeInt(this.z)
//        output.write(this.xSize - 1)
//        output.write(this.ySize - 1)
//        output.write(this.zSize - 1)
//        output.writeInt(this.chunkSize)
//        output.write(this.chunk, 0, this.chunkSize)
//    }
//
//    override fun processPacket(netHandler: NetHandler) {
//        netHandler.handleMapChunk(this)
//    }
//
//    override fun getPacketSize(): Int = 17 + this.chunkSize
//
//}

// TODO: MapDataPacket

open class MultiBlockChangePacket : Packet {
    @JvmField
    var chunkX: Int = 0
    @JvmField
    var chunkZ: Int = 0
    @JvmField
    var coordinates: ShortArray = shortArrayOf()
    @JvmField
    var types: ByteArray = byteArrayOf()
    @JvmField
    var metadatas: ByteArray = byteArrayOf()
    @JvmField
    var size: Int = 0

    constructor() {
        this.isChunkDataPacket = true
    }

    constructor(chunkX: Int, chunkY: Int, coordinates: ShortArray, size: Int, world: World) {
        this.isChunkDataPacket = true
        this.chunkX = chunkX
        this.chunkZ = chunkY
        this.size = size
        this.coordinates = ShortArray(size)
        this.types = ByteArray(size)
        this.metadatas = ByteArray(size)
        val var6 = world.getChunkFromChunkCoords(chunkX, chunkY)

        for (i in 0 until size) {
            val x = coordinates[i].toInt() shr 12 and 15
            val z = coordinates[i].toInt() shr 8 and 15
            val y = coordinates[i].toInt() and 255
            this.coordinates[i] = coordinates[i]
            this.types[i] = var6.getBlockID(x, y, z).toByte()
            this.metadatas[i] = var6.getBlockMetadata(x, y, z).toByte()
        }

    }

    override fun readPacketData(input: DataInputStream) {
        this.chunkX = input.readInt()
        this.chunkZ = input.readInt()
        this.size = input.readShort().toInt() and 0xffff
        this.coordinates = ShortArray(this.size)
        this.types = ByteArray(this.size)
        this.metadatas = ByteArray(this.size)

        for (var2 in 0 until this.size) {
            this.coordinates[var2] = input.readShort()
        }

        input.readFully(this.types)
        input.readFully(this.metadatas)
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.chunkX)
        output.writeInt(this.chunkZ)
        output.writeShort(this.size.toShort().toInt())

        for (var2 in 0 until this.size) {
            output.writeShort(this.coordinates[var2].toInt())
        }

        output.write(this.types)
        output.write(this.metadatas)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleMultiBlockChange(this)
    }

    override fun getPacketSize(): Int = 10 + this.size * 4

}

open class NamedEntitySpawnPacket() : Packet() {
    @JvmField
    var entityId: Int = 0
    @JvmField
    var name: String = ""
    @JvmField
    var x: Int = 0
    @JvmField
    var y: Int = 0
    @JvmField
    var z: Int = 0
    @JvmField
    var rotation: Byte = 0
    @JvmField
    var pitch: Byte = 0
    @JvmField
    var currentItem: Int = 0

    constructor(entity: PlayerEntity) : this() {
        this.entityId = entity.entityId
        this.name = entity.username
        this.x = MathHelper.floor(entity.x * 32.0)
        this.y = MathHelper.floor(entity.y * 32.0)
        this.z = MathHelper.floor(entity.z * 32.0)
        this.rotation = (entity.rotationYaw * 256.0f / 360.0f).toInt().toByte()
        this.pitch = (entity.rotationPitch * 256.0f / 360.0f).toInt().toByte()
        val var2 = entity.inventory.getCurrentItem()
        this.currentItem = var2?.itemID ?: 0
    }

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
        this.name = Packet.readString(input, 16)
        this.x = input.readInt()
        this.y = input.readInt()
        this.z = input.readInt()
        this.rotation = input.readByte()
        this.pitch = input.readByte()
        this.currentItem = input.readShort().toInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
        Packet.writeString(this.name, output)
        output.writeInt(this.x)
        output.writeInt(this.y)
        output.writeInt(this.z)
        output.writeByte(this.rotation.toInt())
        output.writeByte(this.pitch.toInt())
        output.writeShort(this.currentItem)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleNamedEntitySpawn(this)
    }

    override fun getPacketSize(): Int = 28
}

open class OpenWindowPacket() : Packet() {
    @JvmField
    var windowId: Int = 0
    @JvmField
    var inventoryType: Int = 0
    @JvmField
    var windowTitle: String = ""
    @JvmField
    var slotCount: Int = 0

    constructor(var1: Int, var2: Int, var3: String, var4: Int) : this() {
        this.windowId = var1
        this.inventoryType = var2
        this.windowTitle = var3
        this.slotCount = var4
    }

    override fun readPacketData(input: DataInputStream) {
        this.windowId = input.readByte().toInt()
        this.inventoryType = input.readByte().toInt()
        this.windowTitle = input.readUTF()
        this.slotCount = input.readByte().toInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeByte(this.windowId)
        output.writeByte(this.inventoryType)
        output.writeUTF(this.windowTitle)
        output.writeByte(this.slotCount)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.a(this)
    }

    override fun getPacketSize(): Int = 3 + this.windowTitle.length
}

open class PaintingEntityPacket() : Packet() {
    @JvmField
    var entityId: Int = 0
    @JvmField
    var x: Int = 0
    @JvmField
    var y: Int = 0
    @JvmField
    var z: Int = 0
    @JvmField
    var direction: Int = 0
    @JvmField
    var title: String = ""

    constructor(var1: PaintingEntity) : this() {
        this.entityId = var1.entityId
        this.x = var1.x
        this.y = var1.y
        this.z = var1.z
        this.direction = var1.direction
        this.title = var1.art.title
    }

    override fun readPacketData(var1: DataInputStream) {
        this.entityId = var1.readInt()
        this.title = Packet.readString(var1, Art.maxArtTitleLength)
        this.x = var1.readInt()
        this.y = var1.readInt()
        this.z = var1.readInt()
        this.direction = var1.readInt()
    }

    override fun writePacketData(var1: DataOutputStream) {
        var1.writeInt(this.entityId)
        Packet.writeString(this.title, var1)
        var1.writeInt(this.x)
        var1.writeInt(this.y)
        var1.writeInt(this.z)
        var1.writeInt(this.direction)
    }

    override fun processPacket(var1: NetHandler) {
        var1.a(this)
    }

    override fun getPacketSize(): Int = 24
}

open class PlaceBlockPacket() : Packet() {
    @JvmField
    var x: Int = 0
    @JvmField
    var y: Int = 0
    @JvmField
    var z: Int = 0
    @JvmField
    var direction: Int = 0
    @JvmField
    var itemStack: ItemStack? = null

    constructor(x: Int, y: Int, z: Int, direction: Int, itemStack: ItemStack?) : this() {
        this.x = x
        this.y = y
        this.z = z
        this.direction = direction
        this.itemStack = itemStack
    }

    override fun readPacketData(var1: DataInputStream) {
        this.x = var1.readInt()
        this.y = var1.read()
        this.z = var1.readInt()
        this.direction = var1.read()
        val var2 = var1.readShort()
        if (var2 >= 0) {
            val var3 = var1.readByte()
            val var4 = var1.readShort()
            this.itemStack = ItemStack(var2.toInt(), var3.toInt(), var4.toInt())
        } else {
            this.itemStack = null
        }

    }

    override fun writePacketData(var1: DataOutputStream) {
        var1.writeInt(this.x)
        var1.write(this.y)
        var1.writeInt(this.z)
        var1.write(this.direction)
        if (this.itemStack == null) {
            var1.writeShort(-1)
        } else {
            var1.writeShort(this.itemStack!!.itemID)
            var1.writeByte(this.itemStack!!.stackSize)
            var1.writeShort(this.itemStack!!.itemDamage)
        }

    }

    override fun processPacket(var1: NetHandler) {
        var1.handlePlace(this)
    }

    override fun getPacketSize(): Int = 15

}

open class PlayerInventoryPacket() : Packet() {
    @JvmField
    var entityId: Int = 0
    @JvmField
    var slot: Int = 0
    @JvmField
    var itemId: Int = 0
    @JvmField
    var itemDamage: Int = 0

    constructor(var1: Int, var2: Int, var3: ItemStack?) : this() {
        this.entityId = var1
        this.slot = var2
        if (var3 == null) {
            this.itemId = -1
            this.itemDamage = 0
        } else {
            this.itemId = var3.itemID
            this.itemDamage = var3.itemDamage
        }

    }

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
        this.slot = input.readShort().toInt()
        this.itemId = input.readShort().toInt()
        this.itemDamage = input.readShort().toInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
        output.writeShort(this.slot)
        output.writeShort(this.itemId)
        output.writeShort(this.itemDamage)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handlePlayerInventory(this)
    }

    override fun getPacketSize(): Int = 8
}

open class PlayerPositionPacket : FlyingPacket {
    constructor() {
        this.moving = true
    }

    constructor(var1: Double, var3: Double, var5: Double, var7: Double, var9: Boolean) {
        this.x = var1
        this.y = var3
        this.stance = var5
        this.z = var7
        this.onGround = var9
        this.moving = true
    }

    override fun readPacketData(input: DataInputStream) {
        this.x = input.readDouble()
        this.y = input.readDouble()
        this.stance = input.readDouble()
        this.z = input.readDouble()
        super.readPacketData(input)
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeDouble(this.x)
        output.writeDouble(this.y)
        output.writeDouble(this.stance)
        output.writeDouble(this.z)
        super.writePacketData(output)
    }

    override fun getPacketSize(): Int = 33

}

open class PlayerPositionRotationPacket : FlyingPacket {
    constructor() {
        this.rotating = true
        this.moving = true
    }

    constructor(var1: Double, var3: Double, var5: Double, var7: Double, var9: Float, var10: Float, var11: Boolean) {
        this.x = var1
        this.y = var3
        this.stance = var5
        this.z = var7
        this.yaw = var9
        this.pitch = var10
        this.onGround = var11
        this.rotating = true
        this.moving = true
    }

    override fun readPacketData(input: DataInputStream) {
        this.x = input.readDouble()
        this.y = input.readDouble()
        this.stance = input.readDouble()
        this.z = input.readDouble()
        this.yaw = input.readFloat()
        this.pitch = input.readFloat()
        super.readPacketData(input)
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeDouble(this.x)
        output.writeDouble(this.y)
        output.writeDouble(this.stance)
        output.writeDouble(this.z)
        output.writeFloat(this.yaw)
        output.writeFloat(this.pitch)
        super.writePacketData(output)
    }

    override fun getPacketSize(): Int = 41
}

open class PlayerRotationPacket : FlyingPacket {
    constructor() {
        this.rotating = true
    }

    constructor(var1: Float, var2: Float, var3: Boolean) {
        this.yaw = var1
        this.pitch = var2
        this.onGround = var3
        this.rotating = true
    }

    override fun readPacketData(input: DataInputStream) {
        this.yaw = input.readFloat()
        this.pitch = input.readFloat()
        super.readPacketData(input)
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeFloat(this.yaw)
        output.writeFloat(this.pitch)
        super.writePacketData(output)
    }

    override fun getPacketSize(): Int = 9
}

open class PlayNoteBlockPacket() : Packet() {
    @JvmField
    var x: Int = 0
    @JvmField
    var y: Int = 0
    @JvmField
    var z: Int = 0
    @JvmField
    var instrumentType: Int = 0
    @JvmField
    var pitch: Int = 0

    constructor(x: Int, y: Int, z: Int, instrumentType: Int, pitch: Int) : this() {
        this.x = x
        this.y = y
        this.z = z
        this.instrumentType = instrumentType
        this.pitch = pitch
    }

    override fun readPacketData(input: DataInputStream) {
        this.x = input.readInt()
        this.y = input.readShort().toInt()
        this.z = input.readInt()
        this.instrumentType = input.read()
        this.pitch = input.read()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.x)
        output.writeShort(this.y)
        output.writeInt(this.z)
        output.write(this.instrumentType)
        output.write(this.pitch)
    }

    override fun processPacket(var1: NetHandler) {
        var1.handleNotePlay(this)
    }

    override fun getPacketSize(): Int = 12
}

open class PositionPacket() : Packet() {
    @JvmField
    var a: Float = 0.toFloat()
    @JvmField
    var b: Float = 0.toFloat()
    @JvmField
    var c: Boolean = false
    @JvmField
    var d: Boolean = false
    @JvmField
    var e: Float = 0.toFloat()
    @JvmField
    var f: Float = 0.toFloat()

    override fun readPacketData(input: DataInputStream) {
        this.a = input.readFloat()
        this.b = input.readFloat()
        this.e = input.readFloat()
        this.f = input.readFloat()
        this.c = input.readBoolean()
        this.d = input.readBoolean()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeFloat(this.a)
        output.writeFloat(this.b)
        output.writeFloat(this.e)
        output.writeFloat(this.f)
        output.writeBoolean(this.c)
        output.writeBoolean(this.d)
    }

    override fun processPacket(var1: NetHandler) {
        var1.a(this)
    }

    override fun getPacketSize(): Int = 18

    fun c(): Float {
        return this.a
    }

    fun d(): Float {
        return this.e
    }

    fun e(): Float {
        return this.b
    }

    fun f(): Float {
        return this.f
    }

    fun g(): Boolean {
        return this.c
    }

    fun h(): Boolean {
        return this.d
    }
}

open class RelativeEntityPositionPacket : EntityPacket {
    constructor() {}

    constructor(var1: Int, var2: Byte, var3: Byte, var4: Byte) : super(var1) {
        this.x = var2
        this.y = var3
        this.z = var4
    }

    override fun readPacketData(input: DataInputStream) {
        super.readPacketData(input)
        this.x = input.readByte()
        this.y = input.readByte()
        this.z = input.readByte()
    }

    override fun writePacketData(output: DataOutputStream) {
        super.writePacketData(output)
        output.writeByte(this.x.toInt())
        output.writeByte(this.y.toInt())
        output.writeByte(this.z.toInt())
    }

    override fun getPacketSize(): Int = 7
}

open class RelativeEntityPositionRotationPacket : EntityPacket {
    constructor() {
        this.rotating = true
    }

    constructor(var1: Int, var2: Byte, var3: Byte, var4: Byte, var5: Byte, var6: Byte) : super(var1) {
        this.x = var2
        this.y = var3
        this.z = var4
        this.yaw = var5
        this.pitch = var6
        this.rotating = true
    }

    override fun readPacketData(input: DataInputStream) {
        super.readPacketData(input)
        this.x = input.readByte()
        this.y = input.readByte()
        this.z = input.readByte()
        this.yaw = input.readByte()
        this.pitch = input.readByte()
    }

    override fun writePacketData(output: DataOutputStream) {
        super.writePacketData(output)
        output.writeByte(this.x.toInt())
        output.writeByte(this.y.toInt())
        output.writeByte(this.z.toInt())
        output.writeByte(this.yaw.toInt())
        output.writeByte(this.pitch.toInt())
    }

    override fun getPacketSize(): Int {
        return 9
    }
}

// TODO: RespawnPacket

open class SetSlotPacket : Packet {
    @JvmField
    var windowId: Int = 0
    @JvmField
    var itemSlot: Int = 0
    @JvmField
    var itemStack: ItemStack? = null

    constructor() {}

    constructor(windowId: Int, itemSlot: Int, itemStack: ItemStack?) {
        this.windowId = windowId
        this.itemSlot = itemSlot
        this.itemStack = itemStack?.copy()
    }

    override fun readPacketData(input: DataInputStream) {
        this.windowId = input.readByte().toInt()
        this.itemSlot = input.readShort().toInt()
        val var2 = input.readShort()
        if (var2 >= 0) {
            val var3 = input.readByte()
            val var4 = input.readShort()
            this.itemStack = ItemStack(var2.toInt(), var3.toInt(), var4.toInt())
        } else {
            this.itemStack = null
        }

    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeByte(this.windowId)
        output.writeShort(this.itemSlot)
        if (this.itemStack == null) {
            output.writeShort(-1)
        } else {
            output.writeShort(this.itemStack!!.itemID)
            output.writeByte(this.itemStack!!.stackSize)
            output.writeShort(this.itemStack!!.itemDamage)
        }

    }

    override fun processPacket(var1: NetHandler) {
        var1.a(this)
    }

    override fun getPacketSize(): Int = 8
}

// TODO: SleepPacket
open class SpawnPositionPacket : Packet {
    @JvmField
    var x: Int = 0
    @JvmField
    var y: Int = 0
    @JvmField
    var z: Int = 0

    constructor() {}

    constructor(x: Int, y: Int, z: Int) {
        this.x = x
        this.y = y
        this.z = z
    }

    override fun readPacketData(input: DataInputStream) {
        this.x = input.readInt()
        this.y = input.readInt()
        this.z = input.readInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.x)
        output.writeInt(this.y)
        output.writeInt(this.z)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleSpawnPosition(this)
    }

    override fun getPacketSize(): Int = 12
}

// TODO: StatisticPacket
// TODO: TransactionPacket
// TODO: UpdateHealthPacket

open class UpdateProgressBarPacket : Packet {
    @JvmField
    var windowId: Int = 0
    @JvmField
    var progressBar: Int = 0
    @JvmField
    var progressBarValue: Int = 0

    constructor() {}

    constructor(windowId: Int, progressBar: Int, progressBarValue: Int) {
        this.windowId = windowId
        this.progressBar = progressBar
        this.progressBarValue = progressBarValue
    }

    override fun processPacket(var1: NetHandler) {
        var1.a(this)
    }

    override fun readPacketData(input: DataInputStream) {
        this.windowId = input.readByte().toInt()
        this.progressBar = input.readShort().toInt()
        this.progressBarValue = input.readShort().toInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeByte(this.windowId)
        output.writeShort(this.progressBar)
        output.writeShort(this.progressBarValue)
    }

    override fun getPacketSize(): Int = 5
}

open class UpdateSignPacket : Packet {
    @JvmField
    var x: Int = 0
    @JvmField
    var y: Int = 0
    @JvmField
    var z: Int = 0
    @JvmField
    var signLines: Array<String> = arrayOf()

    constructor() {
        this.isChunkDataPacket = true
    }

    constructor(x: Int, y: Int, z: Int, signLines: Array<String>) {
        this.isChunkDataPacket = true
        this.x = x
        this.y = y
        this.z = z
        this.signLines = signLines
    }

    override fun readPacketData(input: DataInputStream) {
        this.x = input.readInt()
        this.y = input.readShort().toInt()
        this.z = input.readInt()
        this.signLines = arrayOf()

        for (var2 in 0..3) {
            this.signLines[var2] = Packet.readString(input, 15)
        }

    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.x)
        output.writeShort(this.y)
        output.writeInt(this.z)

        for (var2 in 0..3) {
            Packet.writeString(this.signLines[var2], output)
        }

    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleSignUpdate(this)
    }

    override fun getPacketSize(): Int = this.signLines.map { it.length }.sum()
}

open class UpdateTimePacket : Packet {
    @JvmField
    var time: Long = 0

    constructor() {}

    constructor(var1: Long) {
        this.time = var1
    }

    override fun readPacketData(input: DataInputStream) {
        this.time = input.readLong()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeLong(this.time)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleUpdateTime(this)
    }

    override fun getPacketSize(): Int = 8
}

open class UseEntityPacket : Packet() {
    @JvmField
    var playerId: Int = 0
    @JvmField
    var targetEntityId: Int = 0
    @JvmField
    var isLeftClick: Int = 0

    override fun readPacketData(input: DataInputStream) {
        this.playerId = input.readInt()
        this.targetEntityId = input.readInt()
        this.isLeftClick = input.readByte().toInt()
    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.playerId)
        output.writeInt(this.targetEntityId)
        output.writeByte(this.isLeftClick)
    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleUseEntity(this)
    }

    override fun getPacketSize(): Int = 9
}

open class VehicleSpawnPacket : Packet {
    @JvmField
    var entityId: Int = 0
    @JvmField
    var x: Int = 0
    @JvmField
    var y: Int = 0
    @JvmField
    var z: Int = 0
    @JvmField
    var packetStats: Int = 0
    @JvmField
    var totalPacketsCount: Int = 0
    @JvmField
    var g: Int = 0
    @JvmField
    var type: Int = 0
    @JvmField
    var i: Int = 0

    constructor() {}

    @JvmOverloads constructor(entity: Entity, type: Int, i: Int = 0) {
        this.entityId = entity.entityId
        this.x = MathHelper.floor(entity.x * 32.0)
        this.y = MathHelper.floor(entity.y * 32.0)
        this.z = MathHelper.floor(entity.z * 32.0)
        this.type = type
        this.i = i
        if (i > 0) {
            var var4 = entity.motionX
            var var6 = entity.motionY
            var var8 = entity.motionZ
            val var10 = 3.9
            if (var4 < -var10) {
                var4 = -var10
            }

            if (var6 < -var10) {
                var6 = -var10
            }

            if (var8 < -var10) {
                var8 = -var10
            }

            if (var4 > var10) {
                var4 = var10
            }

            if (var6 > var10) {
                var6 = var10
            }

            if (var8 > var10) {
                var8 = var10
            }

            this.packetStats = (var4 * 8000.0).toInt()
            this.totalPacketsCount = (var6 * 8000.0).toInt()
            this.g = (var8 * 8000.0).toInt()
        }

    }

    override fun readPacketData(input: DataInputStream) {
        this.entityId = input.readInt()
        this.type = input.readByte().toInt()
        this.x = input.readInt()
        this.y = input.readInt()
        this.z = input.readInt()
        this.i = input.readInt()
        if (this.i > 0) {
            this.packetStats = input.readShort().toInt()
            this.totalPacketsCount = input.readShort().toInt()
            this.g = input.readShort().toInt()
        }

    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeInt(this.entityId)
        output.writeByte(this.type)
        output.writeInt(this.x)
        output.writeInt(this.y)
        output.writeInt(this.z)
        output.writeInt(this.i)
        if (this.i > 0) {
            output.writeShort(this.packetStats)
            output.writeShort(this.totalPacketsCount)
            output.writeShort(this.g)
        }

    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.handleVehicleSpawn(this)
    }

    override fun getPacketSize(): Int = if (21 + this.i > 0) 6 else 0
}

// TODO: WeatherPacket

open class WindowClickPacket : Packet {
    @JvmField
    var windowId: Int = 0
    @JvmField
    var inventorySlot: Int = 0
    @JvmField
    var mouseClick: Int = 0
    @JvmField
    var action: Short = 0
    @JvmField
    var itemStack: ItemStack? = null
    @JvmField
    var totalPacketsCount: Boolean = false

    constructor(windowId: Int, inventorySlot: Int, mouseClick: Int, totalPacketsCout: Boolean, itemStack: ItemStack?, action: Short) : super() {
        this.windowId = windowId
        this.inventorySlot = inventorySlot
        this.mouseClick = mouseClick
        this.action = action
        this.itemStack = itemStack
        this.totalPacketsCount = totalPacketsCount
    }

    override fun processPacket(var1: NetHandler) {
        var1.a(this)
    }

    override fun readPacketData(input: DataInputStream) {
        this.windowId = input.readByte().toInt()
        this.inventorySlot = input.readShort().toInt()
        this.mouseClick = input.readByte().toInt()
        this.action = input.readShort()
        this.totalPacketsCount = input.readBoolean()
        val var2 = input.readShort()
        if (var2 >= 0) {
            val var3 = input.readByte()
            val var4 = input.readShort()
            this.itemStack = ItemStack(var2.toInt(), var3.toInt(), var4.toInt())
        } else {
            this.itemStack = null
        }

    }

    override fun writePacketData(output: DataOutputStream) {
        output.writeByte(this.windowId)
        output.writeShort(this.inventorySlot)
        output.writeByte(this.mouseClick)
        output.writeShort(this.action.toInt())
        output.writeBoolean(this.totalPacketsCount)
        if (this.itemStack == null) {
            output.writeShort(-1)
        } else {
            output.writeShort(this.itemStack!!.itemID)
            output.writeByte(this.itemStack!!.stackSize)
            output.writeShort(this.itemStack!!.itemDamage)
        }

    }

    override fun getPacketSize(): Int = 11
}

open class WindowItemPacket : Packet {
    @JvmField
    var windowId: Int = 0
    @JvmField
    var itemStack: Array<ItemStack?> = arrayOf()

    constructor() {}

    constructor(windowId: Int, itemStack: List<ItemStack?>) {
        this.windowId = windowId
        this.itemStack = arrayOfNulls(itemStack.size)

        for (var3 in this.itemStack.indices) {
            val var4 = itemStack[var3]
            this.itemStack[var3] = var4?.copy()
        }

    }

    override fun readPacketData(input: DataInputStream) {
        this.windowId = input.readByte().toInt()
        val var2 = input.readShort()
        this.itemStack = arrayOfNulls(var2.toInt())

        for (var3 in 0 until var2) {
            val var4 = input.readShort()
            if (var4 >= 0) {
                val var5 = input.readByte()
                val var6 = input.readShort()
                this.itemStack[var3] = ItemStack(var4.toInt(), var5.toInt(), var6.toInt())
            }
        }

    }

    override fun writePacketData(var1: DataOutputStream) {
        var1.writeByte(this.windowId)
        var1.writeShort(this.itemStack.size)

        for (var2 in this.itemStack.indices) {
            if (this.itemStack[var2] == null) {
                var1.writeShort(-1)
            } else {
                var1.writeShort(this.itemStack[var2]!!.itemID.toShort().toInt())
                var1.writeByte(this.itemStack[var2]!!.stackSize.toByte().toInt())
                var1.writeShort(this.itemStack[var2]!!.itemDamage.toShort().toInt())
            }
        }

    }

    override fun processPacket(netHandler: NetHandler) {
        netHandler.a(this)
    }

    override fun getPacketSize(): Int = 3 + this.itemStack.size * 5
}
