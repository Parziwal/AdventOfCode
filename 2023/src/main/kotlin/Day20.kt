import java.io.File

fun main() {
    println("Day 20")
    println("-----------------------------------")
    println("Part One: ${partOneOfDay20()}")
    println("Part Two: ${partTwoOfDay20()}")
    println("-----------------------------------")
}

enum class Pulse {
    Low,
    High,
}

abstract class CommunicationModule(val name: String) {
    var highPulsesCount = 0
        private set
    var lowPulsesCount = 0
        private set

    val senderModules: MutableList<CommunicationModule> = mutableListOf()
    private val receiverModules: MutableList<CommunicationModule> = mutableListOf()
    protected open val nextPulses = mutableListOf<Pulse>()
    private val pulsesToSend = mutableListOf<Pulse>()

    private fun addSenderModule(module: CommunicationModule) {
        senderModules.add(module)
    }

    fun addReceiverModule(module: CommunicationModule) {
        receiverModules.add(module)
        module.addSenderModule(this)
    }

    fun prepareForSend() {
        pulsesToSend.addAll(nextPulses)
        nextPulses.clear()
    }

    fun sendPulseToConnectedModules(): Boolean {
        if (pulsesToSend.isEmpty()) {
            return false
        }

        pulsesToSend.forEach { pulse ->
            when(pulse) {
                Pulse.Low -> lowPulsesCount += receiverModules.size
                Pulse.High -> highPulsesCount += receiverModules.size
            }

            receiverModules.forEach {
                it.receivePulse(this, pulse)
            }
        }

        pulsesToSend.clear()

        return true
    }
    abstract fun receivePulse(from: CommunicationModule, pulse: Pulse)
}

class FlipFlopModule(name: String) : CommunicationModule(name) {
    private var isOn = false

    override fun receivePulse(from: CommunicationModule, pulse: Pulse) {
        when(pulse) {
            Pulse.Low -> {
                isOn = !isOn
                val nextPulse = if (isOn) Pulse.High else Pulse.Low
                nextPulses.add(nextPulse)
            }
            Pulse.High -> return
        }
    }
}

class ConjunctionModules(name: String) : CommunicationModule(name) {
    private val inputPulses = mutableMapOf<String, Pulse>()

    override fun receivePulse(from: CommunicationModule, pulse: Pulse) {
        if (inputPulses.isEmpty()) {
            inputPulses.putAll(senderModules.associate {
                it.name to Pulse.Low
            })
        }

        inputPulses[from.name] = pulse

        val nextPulse = if (inputPulses.values.all { it == Pulse.High }) {
            Pulse.Low
        } else {
            Pulse.High
        }

        nextPulses.add(nextPulse)
    }
}

class BroadcasterModule() : CommunicationModule("broadcaster") {
    override fun receivePulse(from: CommunicationModule, pulse: Pulse) {
        nextPulses.add(pulse)
    }
}

class ButtonModule(broadcasterModule: BroadcasterModule) : CommunicationModule("button") {
    init {
        addReceiverModule(broadcasterModule)
    }

    override val nextPulses: MutableList<Pulse>
        get() = mutableListOf(Pulse.Low)

    override fun receivePulse(from: CommunicationModule, pulse: Pulse) {
    }
}

class EmptyModule(name: String) : CommunicationModule(name) {
    override fun receivePulse(from: CommunicationModule, pulse: Pulse) {
    }
}

fun partOneOfDay20(): Int {
    val moduleConfiguration = File("src/main/resources/day20.txt")
        .readLines()

    val modules = mutableMapOf<String, CommunicationModule>()
    moduleConfiguration.forEach { config ->
        val moduleName = config.split(" -> ")[0].trim('%', '&')
        val newModule = when {
            config.startsWith("broadcaster") -> BroadcasterModule()
            config.startsWith("%") -> FlipFlopModule(moduleName)
            config.startsWith("&") -> ConjunctionModules(moduleName)
            else -> throw Exception()
        }
        modules[moduleName] = newModule
    }

    moduleConfiguration.forEach { config ->
        val moduleName = config.split(" -> ")[0].trim('%', '&')
        val connectedModules = config.split(" -> ")[1].split(", ")
        connectedModules.forEach {
            if (modules.contains(it)) {
                modules[moduleName]!!.addReceiverModule(modules[it]!!)
            } else {
                modules[moduleName]!!.addReceiverModule(EmptyModule(it))
            }
        }
    }

    val button = ButtonModule(modules["broadcaster"]!! as BroadcasterModule)

    repeat(1000) {
        button.prepareForSend()
        button.sendPulseToConnectedModules()
        var hasPulse = true
        while (hasPulse) {
            modules.values.forEach {
                it.prepareForSend()
            }
            hasPulse = modules.values.map {
                it.sendPulseToConnectedModules()
            }.any { it }
        }
    }

    modules["button"] = button
    val lowPulseSum = modules.values.sumOf { it.lowPulsesCount }
    val highPulseSum = modules.values.sumOf { it.highPulsesCount }

    return lowPulseSum * highPulseSum
}

fun partTwoOfDay20(): Long {
    val moduleConfiguration = File("src/main/resources/day20.txt")
        .readLines()

    val modules = mutableMapOf<String, CommunicationModule>()
    moduleConfiguration.forEach { config ->
        val moduleName = config.split(" -> ")[0].trim('%', '&')
        val newModule = when {
            config.startsWith("broadcaster") -> BroadcasterModule()
            config.startsWith("%") -> FlipFlopModule(moduleName)
            config.startsWith("&") -> ConjunctionModules(moduleName)
            else -> throw Exception()
        }
        modules[moduleName] = newModule
    }

    moduleConfiguration.forEach { config ->
        val moduleName = config.split(" -> ")[0].trim('%', '&')
        val connectedModules = config.split(" -> ")[1].split(", ")
        connectedModules.forEach {
            if (!modules.contains(it)) {
                modules[it] = EmptyModule(it)
            }
            modules[moduleName]!!.addReceiverModule(modules[it]!!)
        }
    }

    val button = ButtonModule(modules["broadcaster"]!! as BroadcasterModule)
    var iteration = 0
    val highPulseIterations = mutableMapOf("js" to 0, "qs" to 0, "dt" to 0, "ts" to 0)
    while(true) {
        button.prepareForSend()
        button.sendPulseToConnectedModules()
        iteration++

        var hasPulse = true
        while (hasPulse) {
            modules.values.forEach {
                it.prepareForSend()
            }
            hasPulse = modules.values.map {
                it.sendPulseToConnectedModules()
            }.any { it }
        }

        highPulseIterations.keys.forEach {
            if (modules[it]!!.highPulsesCount > 0 && highPulseIterations[it] == 0) {
                highPulseIterations[it] = iteration
            }
        }

        if (highPulseIterations.all { it.value != 0 }) {
            break
        }
    }

    return highPulseIterations.values
        .map { it.toLong() }
        .reduce { acc, pair ->
            acc.lcm(pair)
        }
}