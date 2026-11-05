package com.example.vbtfatiguemonitor.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * Detecta repeticiones a partir del acelerómetro. Se filtra el eje Z (vertical),
 * integrando la aceleración durante la fase concéntrica del levantamiento.
 */
class AccelerationTracker(
    private val sensorManager: SensorManager,
    private val onRepetitionDetected: (velocity: Float) -> Unit
) : SensorEventListener {

    private var isTracking = false
    private var lastTimestamp: Long = 0
    private var currentVelocity = 0f
    private val umbralEmpuje = 12.5f // m/s² para filtrar movimiento involuntario
    private val reposo = 9.81f

    /**
     * Empieza a escuchar el acelerómetro.
     * @return false si el dispositivo no dispone de acelerómetro.
     */
    fun start(): Boolean {
        val sensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ?: return false
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

        val z = event.values[2] // Eje vertical
        val currentTime = event.timestamp

        if (z > umbralEmpuje) {
            if (lastTimestamp != 0L) {
                val dt = (currentTime - lastTimestamp) / 1_000_000_000.0f
                if (dt in 0.001f..0.5f) {
                    currentVelocity += (z - reposo) * dt // Integración simple
                }
            }
            lastTimestamp = currentTime
        } else if (z < reposo && currentVelocity > 0.2f) {
            // Fin de la fase concéntrica / levantamiento
            onRepetitionDetected(currentVelocity)
            currentVelocity = 0f
            lastTimestamp = 0
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
