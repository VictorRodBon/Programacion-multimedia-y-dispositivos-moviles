package com.example.vbtfatiguemonitor.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class AccelerationTracker(
    private val sensorManager: SensorManager,
    private val onRepetitionDetected: (velocity: Float) -> Unit
) : SensorEventListener {

    private var isTracking = false
    private var lastTimestamp: Long = 0
    private var currentVelocity = 0f

    // Umbral basado en aceleración lineal (sin gravedad) o magnitud total
    private val umbralEmpuje = 3.5f // Aceleración extra en m/s²
    private val umbralDetente = 0.5f

    fun start(): Boolean {
        // Usar LINEAR_ACCELERATION elimina automáticamente la gravedad de los 3 ejes
        val sensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
            ?: sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ?: return false

        isTracking = true
        currentVelocity = 0f
        lastTimestamp = 0
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME)
        return true
    }

    fun stop() {
        if (!isTracking) return
        isTracking = false
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (!isTracking || event == null) return

        // Si usamos LINEAR_ACCELERATION, values ya no tiene gravedad.
        // Calculamos la magnitud total del movimiento en 3D: sqrt(x^2 + y^2 + z^2)
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val accelerationMagnitude = sqrt(x * x + y * y + z * z)

        val currentTime = event.timestamp

        if (accelerationMagnitude > umbralEmpuje) {
            if (lastTimestamp != 0L) {
                val dt = (currentTime - lastTimestamp) / 1_000_000_000.0f
                if (dt in 0.001f..0.5f) {
                    currentVelocity += accelerationMagnitude * dt
                }
            }
            lastTimestamp = currentTime
        } else if (accelerationMagnitude < umbralDetente && currentVelocity > 0.2f) {
            // Fin del movimiento / repetición detectada
            onRepetitionDetected(currentVelocity)
            currentVelocity = 0f
            lastTimestamp = 0
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
